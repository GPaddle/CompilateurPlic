package analyse;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import exception.ErreurDoubleDeclaration;
import exception.ErreurSemantique;
import exception.ErreurCle;
import exception.ErreurSyntaxique;
import exception.ErreurVerification;
import plic.plic;
import repint.Acces;
import repint.AccesTableau;
import repint.Affectation;
import repint.Bloc;
import repint.Ecrire;
import repint.Entree;
import repint.Expression;
import repint.Idf;
import repint.Instruction;
import repint.Nombre;
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
			Nombre n = analyseOperande();
			return n;
		} catch (ErreurSyntaxique e) {
			try {
				Acces a = analyseAcces();
				return a;
			} catch (ErreurSyntaxique e2) {
				try {

					Idf i = analyseIDF();
					TDS.getInstance().getDeplacementFromIDF(i);
					return i;
				} catch (ErreurCle e1) {
					throw new ErreurSyntaxique("Problème de sémantique : l'identifiant est inconnu");
				}
			} catch (Exception e3) {
				throw new ErreurSyntaxique("Problème sur l'expression");
			}

		}
	}

	/*
	 * private void analyseOperateur() {
	 * 
	 * switch (uniteCourante) { case "+": case "-": case "*": case "et": case "ou":
	 * case "<": case ">": case "=": case "#": case "<=": case ">=":
	 * 
	 * this.uniteCourante = this.aLex.next(); break;
	 * 
	 * default: break; }
	 * 
	 * throw new SyntaxException("Operateur attendu"); // throw new
	 * SyntaxException("Caractère inconnu"); }
	 */

	private Nombre analyseOperande() throws ErreurSyntaxique {
		if (!estCsteEntiere(uniteCourante)) {
			throw new ErreurSyntaxique("Operande attendu");
		}
		int uc = Integer.parseInt(this.uniteCourante);
		uniteCourante = this.aLex.next();
		return new Nombre(uc);

	}

	private boolean estCsteEntiere(String uniteCourante2) {
		try {
			Integer.parseInt(uniteCourante2);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Acces analyseAcces() throws ErreurSyntaxique, ErreurSemantique, ErreurVerification {
		Acces i = analyseIDF();
		try {
// On vérifie la structure [ x ]
			analyseTerminal("[");

			Expression expression = analyseExpression();

			int valeur = -1;

			if (expression.getType() == "nombre") {

				valeur = Integer.parseInt(expression.toString());

				Symbole s = TDS.getInstance().identifier(new Entree((Idf) i));
//				if (s instanceof SymboleTableau) {
//					if (valeur < 0 || valeur > ((SymboleTableau) s).getSize()) {
//						throw new ErreurSemantique("Valeur hors des bornes dans le tableau " + i);
//					}
//				}

			} else if (expression.getType() == "idf") {

				TDS.getInstance().identifier(new Entree((Idf) expression));

//				Expression expr2 = analyseAcces();
//				throw new ErreurSyntaxique("Pas encore implémenté accès via IDF AnalyseAcces");

			} else if (expression.getType() == "tableau") {

				throw new ErreurSyntaxique("Pas encore implémenté accès via tableau AnalyseAcces");

			} else {

				throw new ErreurSyntaxique("Pas encore implémenté AnalyseAcces");

			}
			analyseTerminal("]");

			if (valeur != -1) {

				i = new AccesTableau(i.toString(), valeur);

			} else {
				i = new AccesTableau(i.toString(), expression);
			}

		} catch (ErreurSyntaxique e) {
			// Ne rien faire sinon
			// Car il s'agit donc d'un IDF
		}
		i.verifier();
		return i;

	}

	private Ecrire analyseES() throws ErreurSyntaxique, ErreurVerification {
		analyseTerminal("ecrire");
//		Expression e = analyseExpression();
		Expression exp1 = analyseIDF();
		try {
			analyseTerminal("[");

			Expression exp2 = analyseExpression();

			analyseTerminal("]");

			exp1 = new AccesTableau(exp1.toString(), exp2);

		} catch (ErreurSyntaxique e2) {
			// Ne rien faire, il s'agit d'un idf seul
		} catch (ErreurVerification e) {
			throw e;
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
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(this.uniteCourante);

		if (!matcher.matches()) {
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
