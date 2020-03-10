package plic;

public class ErreurExtension extends Exception {

	public ErreurExtension() {
		System.err.println("ERREUR: Suffixe incorrect");
	}
}
