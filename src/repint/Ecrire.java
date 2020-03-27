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

		if (e instanceof Idf) {
			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((Idf) e).toString()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}
		} else if (e instanceof AccesTableau) {

			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((AccesTableau) e).getI()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}

		} else if (e instanceof Nombre || e instanceof Operateur) {
			// Ne rien tester
		} else {
			throw new ErreurVerification("Type pas encore implémenté : Ecrire.class");
		}

	}

	@Override
	public String toMips() throws Exception {

		String s = FonctionAffichage.stringInstruction("Ecrire " + e);

//		s += "	li $v0, 1\n"; //

		if (e instanceof Idf) {
			Idf e2 = (Idf) e;
			int adresse = TDS.getInstance().getDeplacementFromIDF(e2);
			s += "	lw $a0, " + adresse + "($s7)\n";

		} else if (e instanceof AccesTableau) {
			AccesTableau e3 = (AccesTableau) e;
			s += e3.getAdresse() + "\n" + //
					"	la $v0, 0($a0)\n" + //
					"	lw $a0, 0($v0)\n";
		} else if (e instanceof Operateur || e instanceof Nombre) {
			s += e.toMips();
			s += "	la $a0, 0($v0)\n";
		} else {
			throw new ErreurGenerationCode("Type pas encore pris en charge");
		}

		s += "	li $v0, 1\n"; //

		s += "	syscall\n\n"; //

		s += "	li $v0, 4\n" + //
				"	la $a0, newLine\n" + //
				"	syscall\n";
		return s;
	}

}
