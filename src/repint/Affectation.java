package repint;

import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Affectation extends Instruction {

	private Expression e;
	private Idf i;

	public Affectation(Idf i, Expression e) {
		super();
		this.e = e;
		this.i = i;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return i + " := " + e + " ;";
	}

	@Override
	protected void verifier() throws ErreurVerification {

		TDS tSymbole = TDS.getInstance();
		Symbole s = tSymbole.identifier(new Entree(i));

		if (s == null) {
			throw new ErreurVerification("Affectation");
		}

	}

	@Override
	protected String toMips() throws Exception {
		String s = "\n\n# affectation de " + i + "\n" + "li $v0,";

		if (e instanceof Nombre) {
			s += "" + e + "\n" + "# on stocke " + e + " dans " + i + "\n" + "sw $v0, "
					+ TDS.getInstance().getDeplacementFromIDF(i) + "($s7)";
		} else if (e instanceof Idf) {
			throw new ErreurGenerationCode("ERREUR: Pas d'affectation entre 2 variables pour le moment");
		} else {
			throw new ErreurGenerationCode("ERREUR: erreur inconnue");
		}

		return s;
	}

}
