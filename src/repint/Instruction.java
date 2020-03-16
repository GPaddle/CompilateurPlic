package repint;

import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Instruction {

	protected abstract void verifier() throws ErreurVerification;

	protected abstract String toMips() throws ErreurGenerationCode, Exception;

}
