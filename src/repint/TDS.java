package repint;

import java.util.HashMap;
import java.util.Map;

import exception.DoubleDeclaration;

public class TDS {

	private static int cptDepl;
	private static TDS tds = null;
	private Map<Entree, Symbole> liste = new HashMap<Entree, Symbole>();

	private TDS() {
		this.setCptDepl(0);
	}

	public static void supress() {
		tds = null;
	}

	public static TDS getInstance() {
		if (tds == null) {
			tds = new TDS();
			cptDepl = 0;
		}

		return tds;

	}

	public void ajouter(Entree e, Symbole s) throws DoubleDeclaration, Exception {

		if (liste.containsKey(e)) {
			throw new DoubleDeclaration(e);
		}

		s.setDeplacement(cptDepl);

		liste.put(e, s);

		if (s.getType().equals("entier")) {
			cptDepl -= 4;
		} else {
			throw new Exception("Problème de typage");
		}
	}

	// CptDepl

	public int getCptDepl() {
		return cptDepl;
	}

	public void setCptDepl(int cptDepl) {
		TDS.cptDepl = cptDepl;
	}

	// Liste

	@Override
	public String toString() {
		String s = "\n# variables";
		for (Entree entree : liste.keySet()) {
			s += "\n#\t" + entree.toString() + " : s7 - " + liste.get(entree).getDeplacement();
		}
		return s;
	}

	public int getListeSize() {
		return liste.size();
	}

	public boolean listeContains(Object key) {
		return liste.containsKey(key);
	}

	public Symbole identifier(Entree e) {
		Symbole s = liste.get(e);
		return s;
	}

	public int getDeplacementFromIDF(Idf i) throws Exception {
//		System.out.println(i);
		for (Entree entree : liste.keySet()) {
//			System.out.println(entree.getIdf());

			if (entree.getIdf().toString().equals(i.toString())) {
				return liste.get(entree).getDeplacement();
			}
		}
		throw new Exception("Clé introuvable");
	}

	public void setListe(Map<Entree, Symbole> liste) {
		this.liste = liste;
	}
}
