package repint;

import exception.ErreurSemantique;

public class SymboleTableau extends Symbole {

	private int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public SymboleTableau(int p_deplacement, int p_size) {
		super("tableau", p_deplacement);
		size = p_size;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return super.toString() + "\nsize : " + size + "\n--------";
	}

	@Override
	public void verifier() throws ErreurSemantique {
		if (size <= 0) {
			throw new ErreurSemantique("problème sur la taille de la liste");
		}

	}

}
