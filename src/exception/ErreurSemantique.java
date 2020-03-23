package exception;

public class ErreurSemantique extends Exception {

	String s;

	public ErreurSemantique(String s) {
		super(s);
		this.s = s;
	}
}
