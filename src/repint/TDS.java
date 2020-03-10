package repint;

import exception.DoubleDeclaration;

public class TDS {

	private int cptDepl;
	private static TDS tds = null;

	private TDS() {
		this.setCptDepl(0);
	}

	public static TDS getInstance() {
		if (tds == null) {
			tds = new TDS();
		}

		return tds;

	}
	
	public void ajouter(Entree e, Symbole s) throws DoubleDeclaration{
		
	}

	public int getCptDepl() {
		return cptDepl;
	}

	public void setCptDepl(int cptDepl) {
		this.cptDepl = cptDepl;
	}
}
