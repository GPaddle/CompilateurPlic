package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Comparaison extends Operateur {

	public Comparaison(Expression n1, Expression n2, String type) {
		super(n1, n2, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {
		String typeBranche;
//		String destination="conditionRefusee";
		switch (type) {
		case ">":
			typeBranche = "bgt";
			break;
		case "<":
			typeBranche = "blt";
			break;
		case ">=":
			typeBranche = "bge";
			break;
		case "<=":
			typeBranche = "ble";
			break;
		case "=":
			typeBranche = "beq";
			break;
		case "#":
			typeBranche = "bne";
			break;

		default:
			throw new ErreurGenerationCode("Operateur inconnu");
		}

		String s = expr2.toMips() + //
				Instruction.empiler() + //

				expr1.toMips() + //
				Instruction.depilerDansV1() + //
				
				FonctionAffichage.stringInfos("On effectue " + this) + //

				"	" + typeBranche + " $v0, $v1, alors" + nbCondition + "\n" + //
				"	li $v0, 0 \n" + //
				"	b finsi" + nbCondition + " \n" + //
				"alors" + nbCondition + " :\n" + //
				"	li $v0, 1 \n" + //
				"finsi" + nbCondition + " :\n" ;

		nbCondition++;

		return s;
	}

	@Override
	public void verifier() throws ErreurVerification {
		if (!expr1.getType().equals("entier")) {
			throw new ErreurVerification(expr1 + " doit être de type entier");
		}

		if (!expr2.getType().equals("entier")) {
			throw new ErreurVerification(expr2 + " doit être de type entier");
		}

	}

	@Override
	public String getType() {
		return "booleen";
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
