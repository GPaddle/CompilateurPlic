package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Idf extends Acces {

	public Idf(String nom) {
		super(nom);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Idf other = (Idf) obj;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nom;
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {

		// Récupérer l'adresse de l'IDF
		// Récupérer la valeur à cette adresse
		// Mettre cette valeur dans v0
		String s = FonctionAffichage.stringInfos("On range la valeur de " + this + " dans $v0")+
				"	lw $v0, " + getAdresse() + "($s7)\n";

//		throw new ErreurGenerationCode("Revoir to Mips de IDF");
		return s;
	}

	@Override
	public void verifier() throws ErreurVerification {
		try {
			TDS.getInstance().getDeplacementFromIDF(this);
		} catch (ErreurCle e) {
			throw new ErreurVerification("IDF inconnu");
		}

	}

	@Override
	public String getType() {
		return "idf";
	}

	@Override
	public String getAdresse() throws ErreurCle {
		TDS table = TDS.getInstance();
		return "" + table.getDeplacementFromIDF(this);
	}

	@Override
	public Idf getI() {
		return this;
	}

}
