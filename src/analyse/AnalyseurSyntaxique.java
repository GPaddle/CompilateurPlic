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
import repint.Condition;
import repint.Different;
import repint.Ecrire;
import repint.Egale;
import repint.Entree;
import repint.Et;
import repint.Expression;
import repint.Idf;
import repint.Inf;
import repint.InfEgale;
import repint.Instruction;
import repint.Lire;
import repint.Multiplication;
import repint.Nombre;
import repint.Non;
import repint.Ou;
import repint.Pour;
import repint.Somme;
import repint.Soustraction;
import repint.Sup;
import repint.SupEgale;
import repint.Symbole;
import repint.SymboleEntier;
import repint.SymboleTableau;
import repint.TDS;
import repint.TantQue;

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
		while (uniteCourante.equals(Expression.typeEntier) || uniteCourante.equals("tableau")) {
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

	private Instruction analyseInstruction() throws ErreurDoubleDeclaration, Exception {

		Instruction instruction = null;
		try {
			instruction = analyseES();
		} catch (ErreurSyntaxique e0) {
			try {
				instruction = analyseIteration();
			} catch (ErreurSyntaxique e1) {
				try {
					instruction = analyseCondition();
				} catch (ErreurSyntaxique e2) {
					try {
						instruction = analyseAffectation();
					} catch (ErreurSyntaxique e3) {
						throw e3;
					}
				}
			}
		}

		if (instruction == null) {
			throw new ErreurSyntaxique("Pas d'instruction");
		} else {
			instruction.verifier();
		}

		return instruction;

	}

	private Instruction analyseIteration() throws ErreurDoubleDeclaration, Exception {
		Instruction i = null;

		if (uniteCourante.equals("pour")) {
			analyseTerminal("pour");
			try {

				Idf idf = analyseIDF();
				analyseTerminal("dans");
				Expression e1 = analyseExpression();
				analyseTerminal("..");
				Expression e2 = analyseExpression();
				analyseTerminal("repeter");
				Bloc b = analyseBloc();
				i = new Pour(idf, e1, e2, b);
			} catch (ErreurSyntaxique e1) {
				throw new ErreurSyntaxique("Une boucle pour a été débutée");
			}
		} else if (uniteCourante.equals("tantque")) {
			analyseTerminal("tantque");
			try {
				analyseTerminal("(");
				Expression e1 = analyseExpression();
				analyseTerminal(")");
				analyseTerminal("repeter");
				Bloc b = analyseBloc();

				i = new TantQue(e1, b);

			} catch (ErreurSyntaxique e1) {
				throw new ErreurSyntaxique("Une boucle tant que a été débutée");
			}
		} else {
			throw new ErreurSyntaxique("Pas de déclaration de boucle");
		}

		return i;
	}

	private Instruction analyseCondition() throws ErreurDoubleDeclaration, Exception {
		Instruction i = null;
		if (uniteCourante.equals("si")) {
			analyseTerminal("si");
		} else {
			throw new ErreurSyntaxique("Il n'y a pas de condition");
		}

		try {

			analyseTerminal("(");
			Expression e = analyseExpression();
			analyseTerminal(")");
			analyseTerminal("alors");
			Bloc b = analyseBloc();

			i = new Condition(e, b);
			try {
				analyseTerminal("sinon");
				try {
					Bloc b2 = analyseBloc();
					i = new Condition(e, b, b2);
				} catch (Exception e2) {
					throw new ErreurSyntaxique("Un bloc SINON a été ouvert mais jamais terminé");
				}
			} catch (ErreurSyntaxique e2) {
				// Il s'agit d'un simple bloc SI
			}
		} catch (Exception e) {
			throw new ErreurSyntaxique("Une condition si a débuté");
		}

		return i;
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
				case "et":
					return new Et(n1, n2);
				case "ou":
					return new Ou(n1, n2);

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

//		String operateur;
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

				expr = new Soustraction(new Nombre(0), expr);

			} catch (ErreurSyntaxique e1) {
				try {

					// Soit c'est un
					// non Expression

					analyseTerminal("non");

					expr = analyseExpression();
					expr = new Non(expr);

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

			if (!e.getType().equals(Expression.typeEntier)) {
				throw new ErreurSemantique("l'expression doit être de type entier");
			}

			analyseTerminal("]");
			return new AccesTableau(i.toString(), e);
		}
		return i;
	}

	private Instruction analyseES() throws ErreurSyntaxique, ErreurVerification {

		if (uniteCourante.equals("ecrire")) {
			Expression exp1 = null;
			analyseTerminal("ecrire");
			exp1 = analyseExpression();
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
		} else if (uniteCourante.equals("lire")) {
			try {
				Idf exp1;
				analyseTerminal("lire");
				exp1 = analyseIDF();
				analyseTerminal(";");
				return new Lire(exp1);
			} catch (ErreurSyntaxique e2) {
				throw new ErreurSyntaxique("Il y a un problème dans la lecture");
			}
		} else {
			throw new ErreurSyntaxique("Il n'y a pas eu de lecture ou d'écriture");
		}

	}

	private void analyseDeclaration() throws ErreurSyntaxique, Exception, ErreurDoubleDeclaration {

		TDS tab = TDS.getInstance();

		String type = analyseType();

		Idf i = analyseIDF();

		analyseTerminal(";");

		if (type.equals(Expression.typeEntier)) {
			tab.ajouter(new Entree(i), new SymboleEntier(4));
		} else if (type.startsWith("tableau")) {

			int size = Integer.parseInt(type.split("tableau")[1]);
			Symbole symTab = new SymboleTableau(4 * size, size);

			tab.ajouter(new Entree(i), symTab);
		} else {
			throw new Exception("Type inconnu");
		}

	}

	private String analyseType() throws ErreurSyntaxique, ErreurSemantique {

		// On vérifie si il s'agit d'un entier
		String type = Expression.typeEntier;
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

		String s1 = uniteCourante;

		if (!this.uniteCourante.equals(string)) {
			throw new ErreurSyntaxique("Terminal " + string + " attendu, caractère obtenu : " + uniteCourante);
		}
		
		switch (uniteCourante) {
		case ";":
		case "{":
		case "}":

			plic.compteLigne++;
			break;

		}

		this.uniteCourante = this.aLex.next();
//		System.out.println(s1 + " | " + uniteCourante);
	}

	private Idf analyseIDF() throws ErreurSyntaxique {

		Idf i;
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
