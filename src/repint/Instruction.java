package repint;

public abstract class Instruction {

	protected abstract void verifier() throws ErreurVerification;

	protected abstract String toMips();

	
	
}
