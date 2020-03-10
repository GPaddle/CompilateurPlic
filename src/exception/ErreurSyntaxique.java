package exception;

public class ErreurSyntaxique extends Exception {

	String s;

	public ErreurSyntaxique(String s) {
		super(s);
		this.s = s;
	}
}
