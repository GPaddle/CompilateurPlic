package repint;

public class Symbole {

	private String type;
	private int deplacement;

	public Symbole(String type, int deplacement) {
		super();
		this.type = type;
		this.deplacement = deplacement;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(int deplacement) {
		this.deplacement = deplacement;
	}

}