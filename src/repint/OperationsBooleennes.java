package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class OperationsBooleennes extends Operateur {

	public OperationsBooleennes(Expression n1, Expression n2, String type) {
		super(n1, n2, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {
		String s = expr2.toMips() + //
				Instruction.empiler() + //

				expr1.toMips() + //
				Instruction.depilerDansV1() + //

				FonctionAffichage.stringInfos("On effectue " + this);//

		switch (type) {
		case "et":
			s += "	and $v0, $v0, $v1\n";
			break;
		case "ou":
			s += "	or $v0, $v0, $v1\n";
			break;
		case "non":
			s += "	beqz $v0, alors" + nbCondition + "\n" + //
					"	li $v0, 0\n" + //
					"	b finsi" + nbCondition + "\n" + //
					"	alors" + nbCondition + " : \n" + //
					"		li $v0, 1 \n" + //
					"	finsi" + nbCondition + " : \n\n";//
			break;

		default:
			throw new ErreurGenerationCode("Operateur inconnu");
		}

		return s;
	}

	@Override
	public void verifier() throws ErreurVerification {
		
		super.verifier();
		
		if (!expr1.getType().equals(Expression.typeBooleen)) {
			throw new ErreurVerification(expr1 + " doit être de type booleen");
		}

		if (!expr2.getType().equals(Expression.typeBooleen)) {
			throw new ErreurVerification(expr2 + " doit être de type booleen");
		}
		

	}

	@Override
	public String getType() {
		return Expression.typeBooleen;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
