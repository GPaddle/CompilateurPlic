package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Acces extends Expression {

	protected Expression expr;
	protected String nom;

	public Acces(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		return this.nom;
	}

	public abstract String toMips() throws ErreurGenerationCode, ErreurCle ;

	public abstract String getAdresse() throws ErreurCle, ErreurGenerationCode;

	@Override
	public abstract void verifier() throws ErreurVerification;

	public void ajoutExpression(Expression expr) {
		this.expr = expr;
	}

	public Expression getExpression() {
		return this.expr;
	}

	public abstract Idf getI();

	@Override
	public String getType() {
		return Expression.typeEntier;
	}

}
