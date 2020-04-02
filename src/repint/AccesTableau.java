package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class AccesTableau extends Acces {

	private Idf idf;
	private int index = -1;
	private Expression expr2;

	public AccesTableau(String nom, Expression exp2) {
		super(nom);
		this.idf = new Idf(nom);
		this.expr2 = exp2;


		if (exp2 instanceof Nombre) {
			this.index = Integer.parseInt("" + exp2);
		}

		// this.index = exp2.evaluer();
	}

	@Override
	public String getAdresse() throws ErreurCle, ErreurGenerationCode {
		String adresse;

		int sizeMax = getSizeMax();

		adresse = expr2.toMips() + //

				"	bltz $v0, exceptionValeurHorsDomaine" + //
				FonctionAffichage.stringDetail("On fait le test pour savoir si la valeur de " + expr2 + " est < 0") + //
				"	bge $v0, " + sizeMax + ", exceptionValeurHorsDomaine" + //
				FonctionAffichage.stringDetail("On fait le test pour savoir si la valeur de " + expr2 + " est >= len("
						+ idf + ") = " + sizeMax)
				+ //
				"	li $t0, -4\n" + //
				"	mult $v0, $t0\n" + //
				"	mflo $v0\n" + //
				idf.getAdresse() + //
				"	add $a0, $a0 $v0";

		return adresse;
	}

	@Override
	public void verifier() throws ErreurVerification {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));

		expr2.verifier();
		
		if (expr2 != null) {
		if (!expr2.getType().equals(typeEntier)) {
			throw new ErreurVerification("L'accès doit être de type entier");
		}			
		}

		if (s instanceof SymboleTableau) {

			if (this.expr2 == null && (this.index < 0 || this.index >= getSizeMax())) {
				throw new ErreurVerification("L'indice recherché est hors des valeurs du tableau");
			}

		} else {
			throw new ErreurVerification("Problème de typage");
		}
		
		if (expr2 instanceof Nombre) {
			try {
				int index = Integer.parseInt(((Nombre)expr2).toString());
				if (index<0) {					
					throw new ErreurVerification("Acces dans un tableau avec valeur négative impossible : AccesTableau");
				}else if (index>=this.getSizeMax()) {					
					throw new ErreurVerification("Acces dans un tableau avec index >= taille max impossible: AccesTableau");
				}
			} catch (NumberFormatException e) {
				throw new ErreurVerification("Probleme de typage, le nombre n'en est pas un ... : AccesTableau");
			}
		}

	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {

		String s = null;
		s = getAdresse() + FonctionAffichage.stringDetail("On range dans $a0 l'adresse cible : " + this) + //
				"	lw $v0, ($a0)\n" + // On range dans $a0 la valeur du tableau
				"";

		return s;
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
		return super.getType();
	}

	public void setIndex(int idx) {
		this.index = idx;
	}

	public int getIndex() {
		return index;
	}

	public Expression getExpr() {
		return expr2;
	}

	@Override
	public String toString() {

		return super.toString() + " [ " + (index == -1 ? expr2 : index) + " ]";
	}

}
