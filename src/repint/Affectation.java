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
	//Fonction vérifier
	//Fonction toMips

}
