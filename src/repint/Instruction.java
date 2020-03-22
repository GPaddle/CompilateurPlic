package repint;

import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Instruction {

	public abstract void verifier() throws ErreurVerification;

	public abstract String toMips() throws ErreurGenerationCode, Exception;

}
