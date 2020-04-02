package repint;

import exception.ErreurSemantique;

public class SymboleEntier extends Symbole {

	public SymboleEntier(int deplacement) {
		super(Expression.typeEntier, deplacement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void verifier() throws ErreurSemantique {
		
	}

}
