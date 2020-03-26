package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Calcul extends Expression {

	Expression expr1;
	Expression expr2;
	char type;

	public Calcul(Expression n1, Expression n2, char type) {
		this.expr1 = n1;
		this.expr2 = n2;
		this.type = type;
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {

		String operateur = "";
		String txtExplication = "";
		switch (type) {
		case '+':
			operateur = "add";
			txtExplication = "l'addition";
			break;
		case '-':
			operateur = "sub";
			txtExplication = "la soustraction";
			break;
		case '*':
			operateur = "mul";
			txtExplication = "la multiplication";
			break;

		default:
			throw new ErreurGenerationCode("operateur inconnu : Calcul.class");
		}

		String s = FonctionAffichage.stringDetail("On récupère la valeur de " + expr1) + //

				expr1.toMips() + "\n" + //

				Instruction.empiler() +
				FonctionAffichage.stringDetail("On récupère la valeur de " + expr2) + //

				expr2.toMips() + "\n" + //
				Instruction.depiler() + //
				FonctionAffichage.stringInfos("On fait " + txtExplication + " de " + expr1 + " et " + expr2) + //
				"	" + operateur + " $v0, $v0 $v1" + "\n";

		return s;
	}

	@Override
	public void verifier() throws ErreurVerification {

		if (!expr1.getType().equals("nombre")) {
			Entree entreeExpr1 = new Entree(((Acces) expr1).getI());
			if (TDS.getInstance().identifier(entreeExpr1) == null) {
				throw new ErreurVerification("L'expression " + expr1 + " n'est pas encore déclarée");
			}
		}

		if (!expr2.getType().equals("nombre")) {
			Entree entreeExpr2 = new Entree(((Acces) expr2).getI());
			if (TDS.getInstance().identifier(entreeExpr2) == null) {
				throw new ErreurVerification("L'expression " + expr2 + " n'est pas encore déclarée");
			}
		}
	}

	@Override
	public String getType() {

		String typeRetour = null;

		switch (type) {
		case '+':
			typeRetour = "somme";
			break;
		case '-':
			typeRetour = "soustraction";
			break;
		case '*':
			typeRetour = "multiplication";
			break;

		default:
			break;
		}

		return typeRetour;

	}

	@Override
	public String toString() {
		return expr1 + "" + type + expr2;
	}

}
