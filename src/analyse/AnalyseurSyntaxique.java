package analyse;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import exception.ErreurDoubleDeclaration;
import exception.ErreurCle;
import exception.ErreurSyntaxique;
import repint.Acces;
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

				if (compte == 0) {
					throw e;
				}

				break;
			}

		} while (i != null);

		this.analyseTerminal("}");

		return b;

	}

	private Instruction analyseInstruction() throws ErreurSyntaxique {

		Instruction instruction = null;

		try {
			instruction = analyseES();
		} catch (ErreurSyntaxique e) {

		}

		if (instruction == null) {
			try {
				instruction = analyseAffectation();
			} catch (ErreurSyntaxique e) {

			}
		}

		if (instruction == null) {
			throw new ErreurSyntaxique("Pas d'instruction");
		}

		return instruction;

	}

	private Affectation analyseAffectation() throws ErreurSyntaxique {

		Acces a = analyseAcces();
//		Idf i = analyseAcces();

		if (TDS.getInstance().identifier(new Entree(a.getI())) instanceof SymboleTableau) {
			analyseTerminal("[");
			Expression expr = analyseExpression();
			a.ajoutExpression(expr);
			analyseTerminal("]");

		}

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

	private Acces analyseAcces() throws ErreurSyntaxique {
		Idf i = analyseIDF();
		Acces a = new Acces(i);
		return a;

		// TODO
		// Regarder ici si il faut renvoyer un IDF

	}

	private Ecrire analyseES() throws ErreurSyntaxique {
		analyseTerminal("ecrire");
//		Expression e = analyseExpression();
		Expression e = analyseIDF();
		analyseTerminal(";");

		return new Ecrire(e);
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

			System.out.println(symTab);

			tab.ajouter(new Entree(i), symTab);
		} else {
			throw new Exception("Type inconnu");
		}

	}

	private String analyseType() throws ErreurSyntaxique {

		String type = "entier";
		try {
			analyseTerminal(type);
		} catch (ErreurSyntaxique e) {

			try {
				type = "tableau";
				analyseTerminal(type);
				analyseTerminal("[");

				estCsteEntiere(uniteCourante);
				type += uniteCourante;
				uniteCourante = aLex.next();

				analyseTerminal("]");

			} catch (ErreurSyntaxique e2) {
				throw new ErreurSyntaxique("le type est mal défini");
			}

		}
		return type;

	}

	private void analyseTerminal(String string) throws ErreurSyntaxique {

		if (!this.uniteCourante.equals(string)) {
			throw new ErreurSyntaxique("Terminal " + string + " attendu");
		}

		this.uniteCourante = this.aLex.next();

	}

	private Idf analyseIDF() throws ErreurSyntaxique {

		Idf i;
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(this.uniteCourante);

		if (!matcher.matches()) {
			throw new ErreurSyntaxique("Identifiant attendu");
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
