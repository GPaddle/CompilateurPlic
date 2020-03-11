package repint;

public class Affectation extends Instruction {
	private Expression e;

	private Idf i;

	public Affectation(Idf i, Expression e) {
		super();
		this.e = e;
		this.i = i;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return i + " := " + e+" ;";
	}

	@Override
	protected void verifier() throws ErreurVerification {

		TDS tSymbole = TDS.getInstance();
		Symbole s = tSymbole.identifier(new Entree(i));
		
		if (s==null) {
			throw new ErreurVerification("Affectation");
		}
		
	}

	@Override
	protected String toMips() {
		// TODO Auto-generated method stub
		return null;
	}

}
