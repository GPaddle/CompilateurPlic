package repint;

import exception.ErreurGenerationCode;

public class Nombre extends Expression {
	private int val;

	public Nombre(int val) {
		super();
		this.val = val;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(val);
	}
	
	@Override
	public String toMips() throws ErreurGenerationCode {
		
		return String.valueOf(val);
		/*
		 * Convention : La fonction toMips des sous-classes de Expression génère du code
		 * qui range dans le registre $v0 la valeur de l’expression.
		 */
	}

	@Override
	public void verifier() {
		//Rien à faire
	}

	@Override
	public String getType() {
		return "nombre";
	}

}
