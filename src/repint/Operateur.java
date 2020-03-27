package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Operateur extends Expression {

	Expression expr1;
	Expression expr2;
	String type;

	public Operateur(Expression n1, Expression n2, String type) {
		this.expr1 = n1;
		this.expr2 = n2;
		this.type = type;
	}

	@Override
	public abstract String toMips() throws ErreurGenerationCode, ErreurCle;
//	{
//
//		String operateur = "";
//		String txtExplication = "";
//		switch (type) {
//		case '+':
//			operateur = "add";
//			txtExplication = "l'addition";
//			break;
//		case '-':
//			operateur = "sub";
//			txtExplication = "la soustraction";
//			break;
//		case '*':
//			operateur = "mul";
//			txtExplication = "la multiplication";
//			break;
//
//		default:
//			throw new ErreurGenerationCode("operateur inconnu : Operateur.class");
//		}
//
//		String s = FonctionAffichage.stringDetail("On récupère la valeur de " + expr1) + //
//
//
//				expr1.toMips() + "\n" + //
//				Instruction.empiler() +
//				FonctionAffichage.stringDetail("On récupère la valeur de " + expr2) + //
//
//				expr2.toMips() + "\n" + //
//				Instruction.depilerDansV1() + //
//				FonctionAffichage.stringInfos("On fait " + txtExplication + " de " + expr1 + " et " + expr2) + //
//				"	" + operateur + " $v0, $v1 $v0" + "\n";
//
//		return s;
//	}

	@Override
	public abstract void verifier() throws ErreurVerification;
//	{
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
//		
//		if (!expr1.getType().equals("entier") || !expr1.getType().equals("entier")) {			
//			throw new ErreurVerification("Les calculs sont fait sur des entiers");
//		}
//		
//	}

	@Override
	public abstract String getType();
//	{
//
//		return "entier";
//
//	}

	@Override
	public String toString() {
		return expr1 + type + expr2;
	}

}