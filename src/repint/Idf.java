package repint;

public class Idf extends Expression{

	private String nom;
	
	public Idf(String nom) {
		super();
		this.nom = nom;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nom;
	}

}
