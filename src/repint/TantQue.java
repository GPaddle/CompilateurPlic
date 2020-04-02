package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class TantQue extends Iteration {

	Expression exp1;
	public static int nbBoucle = 0;

	public TantQue(Expression e1, Bloc b) {
		blocARepeter = b;
		this.exp1 = e1;
	}

	@Override
	public void verifier() throws ErreurVerification, ErreurSemantique {

		super.verifier();

		if (!exp1.getType().equals(Expression.typeBooleen)) {
			throw new ErreurVerification("L'instruction dans la boucle tant que doit être booléenne");
		}

		exp1.verifier();

	}

	@Override
	public String toMips() throws ErreurGenerationCode, Exception {
		String s = FonctionAffichage.stringInstruction("tant que (" + exp1 + ") repeter ...") + //
				exp1.toMips() + "\n" + //
				"	beq $v0, 1, tantQue" + nbBoucle + "\n" + //
				"	beq $v0, 0, finTantQue" + nbBoucle + "\n" + //
				"	tantQue" + nbBoucle + " : " + "\n"; //
		nbBoucle++;
		for (Instruction instruction : blocARepeter.getAli()) {
			s += instruction.toMips();
		}
		nbBoucle--;

		s += exp1.toMips() + "\n" + //
				"	beq $v0, 1, tantQue" + nbBoucle + "\n" + //
				"" + "\n" + //
				"	finTantQue"+nbBoucle+" : " + "\n" + //
				"" + "\n" + //
				"" + "\n";

		return s;
	}

	@Override
	public String toString() {
		String s = "Tant que (" + exp1 + ") repeter " + blocARepeter;
		return s;
	}

}
