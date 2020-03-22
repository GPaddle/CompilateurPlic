package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Expression {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public abstract String toMips() throws ErreurGenerationCode, ErreurCle;
	
	public abstract void verifier() throws ErreurVerification;
	
	public abstract String getType();
}
