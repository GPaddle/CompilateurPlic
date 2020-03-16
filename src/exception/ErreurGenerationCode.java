package exception;

public class ErreurGenerationCode extends Exception {
	String s;

	public ErreurGenerationCode(String s) {
		super(s);
		this.s = s;
	}
}
