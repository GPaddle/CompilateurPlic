package repint;

public class Ecrire extends Instruction{
	private Expression e;

	public Ecrire(Expression e) {
		super();
		this.e = e;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ecrire " + e+" ;";
	}


}
