package exception;

import repint.Entree;

@SuppressWarnings("serial")
public class ErreurDoubleDeclaration extends Exception {

	public ErreurDoubleDeclaration() {
		super();
		System.out.println("ERREUR: La déclaration a été faite deux fois");
	}

	public ErreurDoubleDeclaration(Entree e) {
		super();
		System.out.println("ERREUR: La déclaration de la variable \"" + e.getIdf() + "\" a été faite deux fois");
	}

}
