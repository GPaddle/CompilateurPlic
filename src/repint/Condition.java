package repint;

import java.util.ArrayList;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class Condition extends Instruction {

	Expression exp1;
	Bloc conditionSi, conditionSinon;

	public static int nbCondition = 0;

	public Condition(Expression exp1, Bloc conditionSi) {
		super();
		this.exp1 = exp1;
		this.conditionSi = conditionSi;
		this.conditionSinon = null;
	}

	public Condition(Expression exp1, Bloc conditionSi, Bloc conditionSinon) {
		super();
		this.exp1 = exp1;
		this.conditionSi = conditionSi;
		this.conditionSinon = conditionSinon;
	}

	@Override
	public void verifier() throws ErreurVerification, ErreurSemantique {
		exp1.verifier();

		if (!exp1.getType().equals(Expression.typeBooleen)) {
			throw new ErreurSemantique("la condition doit être booleenne");
		}

		conditionSi.verifier();

		if (conditionSinon != null) {
			conditionSinon.verifier();
		}

	}

	@Override
	public String toMips() throws ErreurGenerationCode, Exception {

		nbCondition++;

		String s = FonctionAffichage.stringInstruction("si " + exp1 + " alors ... ") + //
				exp1.toMips() + "\n" + //
				FonctionAffichage.stringInfos("si non " + exp1 + " alors") + //
				"	li $t0, 1 \n" + //
				"	beq $v0, $t0, conditionSi" + nbCondition + "\n";//

		if (conditionSinon != null) {
			ArrayList<Instruction> ali = conditionSinon.getAli();
			for (Instruction instruction : ali) {
				s += instruction.toMips() + "\n";
			}
		}
		s += "	b finSi" + nbCondition;//
		s += FonctionAffichage.stringInfos("si " + exp1 + " alors") + //
				"	conditionSi" + nbCondition + " : \n"; //

		ArrayList<Instruction> ali = conditionSi.getAli();
		for (Instruction instruction : ali) {
			s += instruction.toMips() + "\n";
		}
		s += FonctionAffichage.stringInfos("Sorti de la boucle") ; //
		s += "	b finSi" + nbCondition + "\n";//

		s += "	finSi" + nbCondition + " : \n";

		return s;
	}

	@Override
	public String toString() {
		String s = "si ( " + exp1 + " ) alors " + conditionSi;

		if (conditionSinon != null) {
			s += " sinon " + conditionSinon;
		}
		return s;
	}

}
