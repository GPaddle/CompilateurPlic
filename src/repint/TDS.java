package repint;

import java.util.HashMap;
import java.util.Map;

import exception.DoubleDeclaration;

public class TDS {

	private int cptDepl;
	private static TDS tds = null;
	private Map<Entree, Symbole> liste = new HashMap<Entree, Symbole>();

	private TDS() {
		this.setCptDepl(0);
	}

	public static TDS getInstance() {
		if (tds == null) {
			tds = new TDS();
		}

		return tds;

	}

	public void ajouter(Entree e, Symbole s) throws DoubleDeclaration {
		cptDepl++;
		if (liste.containsKey(e)) {
			throw new DoubleDeclaration(e);
		}
		
		liste.put(e, s);

	}

	public int getCptDepl() {
		return cptDepl;
	}

	public void setCptDepl(int cptDepl) {
		this.cptDepl = cptDepl;
	}

	public Map<Entree, Symbole> getListe() {
		return liste;
	}

	public void setListe(Map<Entree, Symbole> liste) {
		this.liste = liste;
	}
}
