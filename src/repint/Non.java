package repint;

public class Non extends OperationsBooleennes {

	public Non(Expression n1) {
		super(n1, new Egale(new Nombre(1), new Nombre(1)), "non");
	}
	
	@Override
	public String toString() {
		return "non "+expr1;
	}
//		
//	@Override
//	public String toMips() throws ErreurGenerationCode, ErreurCle {
//		return super.toMips();
//	}
//	
//	@Override
//	public void verifier() throws ErreurVerification {
//		super.verifier();
//	}

}
