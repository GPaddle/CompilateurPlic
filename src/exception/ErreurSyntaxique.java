package exception;

public class ErreurSyntaxique extends Exception {

	public ErreurSyntaxique(String string) {
		System.out.println("ERREUR: "+string);
	}

}
