package repint;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import exception.ErreurDoubleDeclaration;
import exception.ErreurCle;

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

	public void ajouter(Entree e, Symbole s) throws ErreurDoubleDeclaration, Exception {

		if (liste.containsKey(e)) {
			throw new ErreurDoubleDeclaration(e);
		}

		s.setDeplacement(cptDepl);

		liste.put(e, s);

		String s2 = s.getType();

		if (s2.equals("entier")) {
			cptDepl -= 4;
		} else if (s2.startsWith("tableau")) {
			try {
				int taille = ((SymboleTableau) s).getSize();
				cptDepl -= 4 * taille;
			} catch (NumberFormatException e2) {
				throw new Exception("Problème dans la déclaration du tableau");
			}

//			throw new Exception("Not yet implemented TDS : tableau");

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
		String s = "\n# Variables\n" ;
		String s2 = "#	Nom 	| Adresse ($s7) \n#";
		s+=s2;
		for (int i = 0; i < s2.length(); i++) {
			s+='-';
		}
		s+="-----\n";
		for (Entree entree : liste.keySet()) {
			int adresse = liste.get(entree).getDeplacement();
			String adresseString = adresse > (-10) ? " " + adresse : "" + adresse;
			adresseString = adresse == 0 ? " " + adresseString : adresseString;
			s += "#	" + entree.toString() + " 	| " + adresseString + "\n";
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

	public int getDeplacementFromIDF(Idf i) throws ErreurCle {
//		System.out.println(i);
		for (Entree entree : liste.keySet()) {
//			System.out.println(entree.getIdf());

			if (entree.getIdf().toString().equals(i.toString())) {
				return liste.get(entree).getDeplacement();
			}
		}
		throw new ErreurCle("Clé introuvable");
	}

	public void setListe(Map<Entree, Symbole> liste) {
		this.liste = liste;
	}

	public boolean contientTableau() {
		for (Entree entree : liste.keySet()) {
			if (this.identifier(entree).getType().equals("tableau")) {
				return true;
			}
		}
		return false;
	}
}
