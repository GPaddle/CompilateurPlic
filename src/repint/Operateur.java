package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Operateur extends Expression {

	Expression expr1;
	Expression expr2;
	String type;
	static int nbCondition = 0;

	public Operateur(Expression n1, Expression n2, String type) {
		this.expr1 = n1;
		this.expr2 = n2;
		this.type = type;
	}

	@Override
	public abstract String toMips() throws ErreurGenerationCode, ErreurCle;

	@Override
	public abstract void verifier() throws ErreurVerification;

	@Override
	public abstract String getType();

	@Override
	public String toString() {
		return expr1 + " " + type + " " + expr2;
	}

}