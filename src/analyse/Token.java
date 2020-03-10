package analyse;

public class Token {

	public String result;
	public static int ligne;
	
	public Token(String result, int ligne) {
		super();
		this.result = result;
		Token.ligne = ligne;
	}

	public void setResult(String s) {
		this.result=s;
		
	}
	
	
	
	
	
}
