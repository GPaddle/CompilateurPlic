package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Ecrire extends Instruction {
	private Expression e;

	public Ecrire(Expression e) {
		super();
		this.e = e;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ecrire " + e + " ;";
	}

	@Override
	public void verifier() throws ErreurVerification {

		String typeE = e.getType();

		if (typeE.equals("idf")) {
			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((Idf) e).toString()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}
		} else if (typeE.equals("tableau")) {

			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((AccesTableau) e).getI()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}

		} else if (typeE.equals("nombre")) {
			// Ne rien tester
		} else {
			throw new ErreurVerification("Type pas encore implémenté : Ecrire.class");
		}

	}

	@Override
	public String toMips() throws Exception {

		String s = FonctionAffichage.stringInfos("Affichage du saut de ligne");

//		s += "	li $v0, 1\n"; //

		String type = e.getType();

		switch (type) {
		case "nombre":

			s += "	li $v0, " + e.toString() + "\n" + //
					"	la $a0, 0($v0)\n";

			break;

		case "idf":

			Idf e2 = (Idf) e;
			int adresse = TDS.getInstance().getDeplacementFromIDF(e2);
			s += "	lw $a0, " + adresse + "($s7)\n";
			break;

		case "tableau":

			AccesTableau e3 = (AccesTableau) e;
			s += e3.getAdresse() + "\n" + //
			
					"	la $v0, 0($a0)\n"+//
					"	lw $a0, 0($v0)\n";
			break;

		case "somme":
		case "soustraction":
		case "multiplication":

			throw new ErreurGenerationCode("les calculs ne sont pas encore pris en charge");

		default:
			break;
		}
		s += "	li $v0, 1\n"; //

		s += "	syscall\n\n"; //

		s += "	li $v0, 4\n" + //
				"	la $a0, newLine\n" + //
				"	syscall\n";
		return s;
	}

}
