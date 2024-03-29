package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Calcul extends Operateur {

	public Calcul(Expression n1, Expression n2, String type) {
		super(n1, n2, type);
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {

		String operateur = "";
		String txtExplication = "";
		switch (type) {
		case "+":
			operateur = "add";
			txtExplication = "l'addition";
			break;
		case "-":
			operateur = "sub";
			txtExplication = "la soustraction";
			break;
		case "*":
			operateur = "mul";
			txtExplication = "la multiplication";
			break;

		default:
			throw new ErreurGenerationCode("operateur inconnu : Calcul.class");
		}

		String s = FonctionAffichage.stringDetail("On récupère la valeur de " + expr1) + //

				expr1.toMips() + "\n" + //
				Instruction.empiler() + FonctionAffichage.stringDetail("On récupère la valeur de " + expr2) + //

				expr2.toMips() + "\n" + //
				Instruction.depilerDansV1() + //
				FonctionAffichage.stringInfos("On fait " + txtExplication + " de " + expr1 + " et " + expr2) + //
				"	" + operateur + " $v0, $v1 $v0" + "\n";

		return s;
	}

	@Override
	public void verifier() throws ErreurVerification {

		super.verifier();
		
//		if (!expr1.getType().equals(typeEntier)) {
//			throw new ErreurVerification("Entier attendu");
//		}
//
//		if (!expr2.getType().equals(typeEntier)) {
//			throw new ErreurVerification("Entier attendu");
//		}
//		
//		if (!(expr1 instanceof Nombre)) {
//			Entree entreeExpr1 = new Entree(((Acces) expr1).getI());
//			if (TDS.getInstance().identifier(entreeExpr1) == null) {
//				throw new ErreurVerification("L'expression " + expr1 + " n'est pas encore déclarée");
//			}
//		}
//
//		if (!(expr2 instanceof Nombre)) {
//			Entree entreeExpr2 = new Entree(((Acces) expr2).getI());
//			if (TDS.getInstance().identifier(entreeExpr2) == null) {
//				throw new ErreurVerification("L'expression " + expr2 + " n'est pas encore déclarée");
//			}
//		}

		if (!expr1.getType().equals(Expression.typeEntier) || !expr1.getType().equals(Expression.typeEntier)) {
			throw new ErreurVerification("Les calculs sont fait sur des entiers");
		}

	}

	@Override
	public String getType() {

		return Expression.typeEntier;

	}

	@Override
	public String toString() {
		return super.toString();
	}

}
