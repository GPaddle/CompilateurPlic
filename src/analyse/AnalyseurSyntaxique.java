package analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.ErreurSyntaxique;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class AnalyseurSyntaxique {

	File file;
	AnalyseurLexical aLex;
	private String uniteCourante;

	public AnalyseurSyntaxique(File f) throws FileNotFoundException {
		this.file = f;
		this.aLex = new AnalyseurLexical(f);
	}

	public void analyse() throws ErreurSyntaxique {
		this.uniteCourante = this.aLex.next();

		this.analyseProg();
		this.analyseIDF();
		this.analyseBloc();
		if (!this.uniteCourante.equals("EOF")) {
			throw new ErreurSyntaxique("EOF attendu");
		}

	}

	private void analyseBloc() throws ErreurSyntaxique {

		this.analyseTerminal("{");

//Iterer :
		while (uniteCourante.equals("entier") || uniteCourante.equals("tableau")) {
			this.analyseDeclaration();
		}

		this.analyseInstruction();
//Iterer :
		while (uniteCourante.equals("ecrire")) {
			this.analyseInstruction();
		}

		this.analyseTerminal("}");

	}

	private void analyseInstruction() throws ErreurSyntaxique {
		int i = 0;
		
		try {
			analyseES();
		} catch (ErreurSyntaxique e) {
			i++;
		}
		
		try {
			analyseAffectation();
		} catch (ErreurSyntaxique e) {
			i++;
		}
		
		if (i == 2) {
			throw new ErreurSyntaxique("Pas d'instruction");
		}

	}

	private boolean analyseAffectation() throws ErreurSyntaxique {

		analyseAcces();
		analyseTerminal(":=");
		analyseExpression();
		return true;
	}

	private void analyseExpression() throws ErreurSyntaxique {
		analyseOperande();

		analyseOperateur();

		analyseOperande();

	}

	private void analyseOperateur() {

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

			this.uniteCourante = this.aLex.next();
			break;

		default:
			break;
		}

		throw new SyntaxException("Operateur attendu");
//		throw new SyntaxException("Caractère inconnu");
	}

	private void analyseOperande() throws ErreurSyntaxique {
		if (!estCsteEntiere(uniteCourante)) {
			throw new ErreurSyntaxique("Operande attendu");
		}
		uniteCourante = this.aLex.next();

	}

	private boolean estCsteEntiere(String uniteCourante2) {
		try {
			Integer.parseInt(uniteCourante2);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void analyseAcces() throws ErreurSyntaxique {
		analyseIDF();

	}

	private boolean analyseES() throws ErreurSyntaxique {
		analyseTerminal("ecrire");
		analyseExpression();
		analyseTerminal(";");
		return true;
	}

	private void analyseDeclaration() throws ErreurSyntaxique {
		analyseTerminal("entier");
		analyseIDF();
		analyseTerminal(";");

	}

	private void analyseTerminal(String string) throws ErreurSyntaxique {

		if (!this.uniteCourante.equals(string)) {
			throw new ErreurSyntaxique("Terminal " + string + " attendu");
		}

		this.uniteCourante = this.aLex.next();

	}

	private void analyseIDF() throws ErreurSyntaxique {

		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(this.uniteCourante);

		if (!matcher.matches()) {
			throw new ErreurSyntaxique("Identifiant attendu");
		}

		this.uniteCourante = this.aLex.next();
	}

	private void analyseProg() throws ErreurSyntaxique {
		if (!this.uniteCourante.equals("programme")) {
			throw new ErreurSyntaxique("Programme attendu");
		}
		this.uniteCourante = this.aLex.next();

	}
}
