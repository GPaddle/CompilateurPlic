package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Somme extends Calcul {

	public Somme(Expression n1, Expression n2) {
		super(n1, n2, '+');
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {

		return super.toMips();
	}

	@Override
	public void verifier() throws ErreurVerification {
		super.verifier();
	}

	@Override
	public String getType() {
		return super.getType();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
