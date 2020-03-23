package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class AccesTableau extends Acces {

	private Idf idf;
	private int index;
	private Expression exp2;

	public AccesTableau(String nom, int nb) {
		super(nom);
		this.idf = new Idf(nom);
		this.index = nb;
		// TODO Auto-generated constructor stub
	}

	public AccesTableau(String nom, Expression exp2) throws ErreurVerification {
		super(nom);
		this.idf = new Idf(nom);
		this.exp2 = exp2;

		String type = exp2.getType();
		
		
		switch (type) {
		
		case "nombre":
			this.index = Integer.parseInt("" + exp2);
			break;

		default:
			throw new ErreurVerification("type : "+type+" pas encore utilisé pour l'accès au tableau : AccesTableau.class");
		}

		// this.index = exp2.evaluer();
	}

	@Override
	public String getAdresse() throws ErreurCle {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));
		int adresse = s.getDeplacement() - 4 * index;

		return adresse + "";
	}

	@Override
	public void verifier() throws ErreurVerification {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));
		if (s instanceof SymboleTableau) {

			if (this.index < 0 || this.index >= getSizeMax() ){
				throw new ErreurVerification("l'indice recherché est hors des valeurs du tableau");
			}

		} else {
			throw new ErreurVerification("Problème de typage");
		}

	}

	public String getIdf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {
		try {
			int sizeMax = getSizeMax();
			String s = "    li $v0, "+index+"\n" + 
					"    li $t0, "+sizeMax+"\n" + 
					"    bge $v0, $t0, exceptionValeurHorsDomaine\n"
					+ "lw $v0, " + getAdresse() + "($s7) # on stocke la valeur de " + idf + " dans v0\r\n";
			return s;
		} catch (ErreurCle e) {
			// TODO Auto-generated catch block
			throw new ErreurGenerationCode("Problème avec la variable (accès)");
		}
	}

	private int getSizeMax() {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));

		return ((SymboleTableau)s).getSize();
	}

	@Override
	public Idf getI() {
		return idf;
	}

	public void setIdf(Idf p_idf) {
		this.idf = p_idf;
	}

	@Override
	public String getType() {
		return "tableau";
	}

	public void setIndex(int idx) {
		this.index = idx;
	}

	@Override
	public String toString() {
		return super.toString() + " [ " + index + " ]";
	}

}
