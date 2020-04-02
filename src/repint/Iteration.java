package repint;

import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public abstract class Iteration extends Instruction {

	Bloc blocARepeter;
	
	@Override
	public void verifier() throws ErreurVerification, ErreurSemantique {
		blocARepeter.verifier();
	}

	@Override
	public abstract String toMips() throws ErreurGenerationCode, Exception;
}
