package analyse;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.DoubleDeclaration;
import exception.ErreurSyntaxique;
import repint.Affectation;
import repint.Bloc;
import repint.Ecrire;
import repint.Entree;
import repint.Expression;
import repint.Idf;
import repint.Instruction;
import repint.Nombre;
import repint.Symbole;
import repint.TDS;

public class AnalyseurSyntaxique {

	File file;
	AnalyseurLexical aLex;
	private String uniteCourante;

	public AnalyseurSyntaxique(File f) throws FileNotFoundException {
		this.file = f;
		this.aLex = new AnalyseurLexical(f);
	}

	public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration, Exception {
		this.uniteCourante = this.aLex.next();

		this.analyseProg();
		this.analyseIDF();
		Bloc bloc = this.analyseBloc();
		if (!this.uniteCourante.equals("EOF")) {
			throw new ErreurSyntaxique("EOF attendu");
		}
		return bloc;

	}

	private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration, Exception {
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

		Idf i = analyseAcces();
		analyseTerminal(":=");
		Expression e = analyseExpression();
		analyseTerminal(";");

		return new Affectation(i, e);
	}

	private Expression analyseExpression() throws ErreurSyntaxique {
		Nombre n = analyseOperande();
		return n;
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

	private Idf analyseAcces() throws ErreurSyntaxique {
		Idf i = analyseIDF();
		return i;

	}

	private Ecrire analyseES() throws ErreurSyntaxique {
		analyseTerminal("ecrire");
//		Expression e = analyseExpression();
		Expression e = analyseIDF();
		analyseTerminal(";");

		return new Ecrire(e);
	}

	private void analyseDeclaration() throws ErreurSyntaxique, Exception, DoubleDeclaration {

		TDS tab = TDS.getInstance();
		String type = "entier";

		analyseTerminal(type);
		Idf i = analyseIDF();
		analyseTerminal(";");

		tab.ajouter(new Entree(i), new Symbole(type, 4));

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
