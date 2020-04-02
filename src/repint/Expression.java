package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Expression {
	
	public static String typeBooleen = "booleen";
	public static String typeEntier = "entier";

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public abstract String toMips() throws ErreurGenerationCode, ErreurCle;
	
	public abstract void verifier() throws ErreurVerification;
	
	public abstract String getType();
}
