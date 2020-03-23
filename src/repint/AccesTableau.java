package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class AccesTableau extends Acces {

	private Idf idf;
	private int index = -1;
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

		if (type.equals("nombre")) {
			this.index = Integer.parseInt("" + exp2);
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

		if (exp2 != null) {
			if (!exp2.getType().equals("nombre")) {
				try {
					TDS.getInstance().getDeplacementFromIDF(((Acces) exp2).getI());
				} catch (ErreurCle e) {
					throw new ErreurVerification("Clé introuvable");
				}
					
				
			}
		}

		if (s instanceof SymboleTableau) {
			
			if (this.exp2==null && (this.index < 0 || this.index >= getSizeMax())) {
				throw new ErreurVerification("L'indice recherché est hors des valeurs du tableau");
			}

		} else {
			throw new ErreurVerification("Problème de typage");
		}

	}

	public Idf getIdf() {
		// TODO Auto-generated method stub
		return idf;
	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {
		try {
			int sizeMax = getSizeMax();
			String s;


			
			if (index != -1) {
				s = "    li $v0, " + index + "\n" + //
						"    li $t0, " + sizeMax + "\n" + //
						"    bge $v0, $t0, exceptionValeurHorsDomaine\n" + //
						"lw $v0, " + getAdresse() + "($s7) # on stocke la valeur de " + idf + " dans v0\r\n";
			} else {
				s = "" + exp2.toMips();
			}

			return s;
		} catch (ErreurCle e) {
			// TODO Auto-generated catch block
			throw new ErreurGenerationCode("Problème avec la variable (accès)");
		}
	}

	private int getSizeMax() {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));

		return ((SymboleTableau) s).getSize();
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

	public int getIndex() {
		return index;
	}
	
	public Expression getExpr() {
		return exp2;
	}

	@Override
	public String toString() {
		
		return super.toString() + " [ " + (index==-1 ?exp2:index)+ " ]";
	}

}
