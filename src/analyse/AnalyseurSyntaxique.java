package analyse;

import java.io.File;
import java.io.FileNotFoundException;
import exception.ErreurDoubleDeclaration;
import exception.ErreurSemantique;
import exception.ErreurSyntaxique;
import exception.ErreurVerification;
import plic.plic;
import repint.Acces;
import repint.AccesTableau;
import repint.Affectation;
import repint.Bloc;
import repint.Different;
import repint.Ecrire;
import repint.Egale;
import repint.Entree;
import repint.Expression;
import repint.Idf;
import repint.Inf;
import repint.InfEgale;
import repint.Instruction;
import repint.Multiplication;
import repint.Nombre;
import repint.Somme;
import repint.Soustraction;
import repint.Sup;
import repint.SupEgale;
import repint.Symbole;
import repint.SymboleEntier;
import repint.SymboleTableau;
import repint.TDS;

public class AnalyseurSyntaxique {

	File file;
	AnalyseurLexical aLex;
	private String uniteCourante;

	public AnalyseurSyntaxique(File f) throws FileNotFoundException {
		this.file = f;
		this.aLex = new AnalyseurLexical(f);
	}

	public Bloc analyse() throws ErreurSyntaxique, ErreurDoubleDeclaration, Exception {
		this.uniteCourante = this.aLex.next();

		this.analyseProg();
		this.analyseIDF();
		Bloc bloc = this.analyseBloc();
		if (!this.uniteCourante.equals("EOF")) {
			throw new ErreurSyntaxique("EOF attendu");
		}
		return bloc;

	}

	private Bloc analyseBloc() throws ErreurSyntaxique, ErreurDoubleDeclaration, Exception {
		Bloc b = new Bloc();
		this.analyseTerminal("{");

		// Iterer :
		while (uniteCourante.equals("entier") || uniteCourante.equals("tableau")) {
			this.analyseDeclaration();
		}

		Instruction i = null;
		int compte = 0;

		do {
			try {
				i = this.analyseInstruction();
				b.ajouter(i);
				compte++;

			} catch (ErreurSyntaxique e) {
				if (compte == 0 || !uniteCourante.equals("}")) {
					throw e;
				}

				break;
			}

		} while (i != null);

		this.analyseTerminal("}");

		return b;

	}

	private Instruction analyseInstruction() throws ErreurSyntaxique, ErreurSemantique, ErreurVerification {

		Instruction instruction = null;

		try {
			instruction = analyseES();
		} catch (ErreurSyntaxique e) {

		}

		if (instruction == null) {
			try {
				instruction = analyseAffectation();
			} catch (ErreurSyntaxique e) {
				throw e;
			}
		}

		if (instruction == null) {
			throw new ErreurSyntaxique("Pas d'instruction");
		}

		return instruction;

	}

	private Affectation analyseAffectation() throws ErreurSyntaxique, ErreurSemantique, ErreurVerification {

		Acces a = analyseAcces();

		analyseTerminal(":=");

		Expression e = analyseExpression();

		analyseTerminal(";");

		return new Affectation(a, e);
	}

	private Expression analyseExpression() throws ErreurSyntaxique {
		try {

			// Soit c'est un opérande
			Expression n1 = analyseOperande();
			try {
				// Soit c'est un combo operande operateur operande

				String operateur = analyseOperateur();
				Expression n2 = analyseOperande();

				switch (operateur) {
				case "+":
					return new Somme(n1, n2);
				case "-":
					return new Soustraction(n1, n2);
				case "*":
					return new Multiplication(n1, n2);
				case "<=":
					return new InfEgale(n1, n2);
				case "<":
					return new Inf(n1, n2);
				case ">=":
					return new SupEgale(n1, n2);
				case ">":
					return new Sup(n1, n2);
				case "=":
					return new Egale(n1, n2);
				case "#":
					return new Different(n1, n2);

				default:
					throw new ErreurSyntaxique("Pas encore implémenté");
				}

			} catch (ErreurSyntaxique e) {
				return n1;
			}

		} catch (ErreurSyntaxique e) {
			throw new ErreurSyntaxique("Operande invalide");
		}
	}

	private String analyseOperateur() throws ErreurSyntaxique {

		switch (uniteCourante) {
		case "+":
		case "-":
		case "*":
		case "et":
		case "ou":
		case "<":
		case ">":
		case "=":
		case "#":
		case "<=":
		case ">=":

			String retour = uniteCourante;
			this.uniteCourante = this.aLex.next();
			return retour;

		default:
			throw new ErreurSyntaxique("Operateur attendu");
		}

	}

	private Expression analyseOperande() throws ErreurSyntaxique {

		String operateur;
		Expression expr;

		if (estCsteEntiere(uniteCourante)) {

			String val = uniteCourante;
			this.uniteCourante = aLex.next();
			expr = new Nombre(Integer.parseInt(val));

		} else {

			try {

				// Soit c'est un
				// - Expression
				analyseTerminal("-");
				analyseTerminal("(");
				expr = analyseExpression();
				analyseTerminal(")");

				operateur = "-";

			} catch (ErreurSyntaxique e1) {
				try {

					// Soit c'est un
					// non Expression

					analyseTerminal("non");

					expr = analyseExpression();
					operateur = "non";

				} catch (ErreurSyntaxique e2) {

					try {

						expr = analyseAcces();

					} catch (ErreurSemantique | ErreurSyntaxique e3) {

						try {

							analyseTerminal("(");
							expr = analyseExpression();
							analyseTerminal(")");

						} catch (ErreurSyntaxique e) {
							throw new ErreurSyntaxique("Problème lors de l'analyse de l'opérande " + uniteCourante);
						}
					}
				}
			}
		}
		return expr;

	}

	private boolean estCsteEntiere(String uniteCourante2) {
		try {
			Integer.parseInt(uniteCourante2);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Acces analyseAcces() throws ErreurSyntaxique, ErreurSemantique {
		Acces i = analyseIDF();

		if (uniteCourante.equals("[")) {
			uniteCourante = aLex.next();

			Expression e = analyseExpression();

			analyseTerminal("]");
			return new AccesTableau(i.toString(), e);
		}
		return i;
	}

	private Ecrire analyseES() throws ErreurSyntaxique, ErreurVerification {
		analyseTerminal("ecrire");
//		Expression e = analyseExpression();
		Expression exp1 = analyseExpression();
		try {
			analyseTerminal("[");

			Expression exp2 = analyseExpression();

			analyseTerminal("]");

			exp1 = new AccesTableau(exp1.toString(), exp2);

		} catch (ErreurSyntaxique e2) {
			// Ne rien faire, il s'agit d'un idf seul
		}
		analyseTerminal(";");

		return new Ecrire(exp1);
	}

	private void analyseDeclaration() throws ErreurSyntaxique, Exception, ErreurDoubleDeclaration {

		TDS tab = TDS.getInstance();

		String type = analyseType();

		Idf i = analyseIDF();

		analyseTerminal(";");

		if (type.equals("entier")) {
			tab.ajouter(new Entree(i), new SymboleEntier(4));
		} else if (type.startsWith("tableau")) {

			int size = Integer.parseInt(type.split("tableau")[1]);
			Symbole symTab = new SymboleTableau(4 * size, size);

//			System.out.println(symTab);

			tab.ajouter(new Entree(i), symTab);
		} else {
			throw new Exception("Type inconnu");
		}

	}

	private String analyseType() throws ErreurSyntaxique, ErreurSemantique {

		// On vérifie si il s'agit d'un entier
		String type = "entier";
		try {
			analyseTerminal(type);
		} catch (ErreurSyntaxique e) {

			try {
				// Si ce n'est pas un entier on vérifie qu'il s'agit d'un tableau
				type = "tableau";

				// On vérifie la syntaxe "tableau [ int ]"
				analyseTerminal(type);
				analyseTerminal("[");

				if (estCsteEntiere(uniteCourante)) {

					int index = Integer.parseInt(uniteCourante);

					// On vérifie si la valeur d'initialisation du tableau est positive et non nulle
					if (index <= 0) {
						throw new ErreurSemantique("Un tableau est initialisé avec la valeur : " + uniteCourante);
					}

					type += uniteCourante;

					uniteCourante = aLex.next();

					analyseTerminal("]");
				} else {
					throw new ErreurSyntaxique(
							"Pour le moment on n'initialise un tableu qu'avec une constante entière");
				}

			} catch (ErreurSyntaxique e2) {
				// Dans le cas ou ce n'est ni un entier, ni un tableau, on soulève une exception
				throw new ErreurSyntaxique("le type est mal défini");
			}

		}
		return type;

	}

	int i = 0;

	private void analyseTerminal(String string) throws ErreurSyntaxique {

		if (!this.uniteCourante.equals(string)) {
			throw new ErreurSyntaxique("Terminal " + string + " attendu, caractère obtenu : " + uniteCourante);
		}

		if (this.uniteCourante.equals(";")) {
			plic.compteLigne++;
		}

		this.uniteCourante = this.aLex.next();

	}

	private Idf analyseIDF() throws ErreurSyntaxique {

		Idf i;
//		System.out.println(uniteCourante);
		switch (this.uniteCourante) {
		case ";":

			throw new ErreurSyntaxique("Il ne peut pas y avoir le caractère " + uniteCourante + " comme idf");
		}

//		Pattern pattern = Pattern.compile("[a-zA-Z]+");
//		Matcher matcher = pattern.matcher(this.uniteCourante);
//
//		if (!matcher.matches()) {
		if (!uniteCourante.matches("[a-zA-Z]+")) {
			throw new ErreurSyntaxique(
					"Identifiant attendu caractère obtenu : " + uniteCourante + " : AnalyseurSyntaxique.class");
		}

		i = new Idf(uniteCourante);

		this.uniteCourante = this.aLex.next();
		return i;
	}

	private void analyseProg() throws ErreurSyntaxique {
		if (!this.uniteCourante.equals("programme")) {
			throw new ErreurSyntaxique("Programme attendu");
		}
		this.uniteCourante = this.aLex.next();

	}
}
