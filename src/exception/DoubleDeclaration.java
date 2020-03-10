package exception;

import repint.Entree;

public class DoubleDeclaration extends Exception {

	public DoubleDeclaration() {
		super();
		System.out.println("La déclaration a été faite deux fois");
	}

	public DoubleDeclaration(Entree e) {
		super();
		System.out.println("La déclaration de la variable \"" + e.getIdf() + "\" a été faite deux fois");
	}

}
