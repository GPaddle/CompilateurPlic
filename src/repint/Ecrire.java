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

	@Override
	protected void verifier() throws ErreurVerification {

		if (e instanceof Idf) {
			TDS tSymbole = TDS.getInstance();
			Symbole s = null;
			s= tSymbole.identifier(new Entree( ( (Idf)e).toString() ) );
			if (s==null) {
				throw new ErreurVerification("ES");
			}
		}
		
		

		
	}

	@Override
	protected String toMips() {
		return null;
	}


}
