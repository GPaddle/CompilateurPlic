package repint;

public class Nombre extends Expression{
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
}
