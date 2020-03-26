package repint;

import Affichage.FonctionAffichage;
import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class AccesTableau extends Acces {

	private Idf idf;
	private int index = -1;
	private Expression expr2;

	public AccesTableau(String nom, Expression exp2) {
		super(nom);
		this.idf = new Idf(nom);
		this.expr2 = exp2;

		String type = exp2.getType();

		if (type.equals("nombre")) {
			this.index = Integer.parseInt("" + exp2);
		}

		// this.index = exp2.evaluer();
	}

	@Override
	public String getAdresse() throws ErreurCle, ErreurGenerationCode {
		Symbole s = TDS.getInstance().identifier(new Entree(idf));
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

		if (expr2 != null) {
			if (!expr2.getType().equals("nombre")) {
				try {
					TDS.getInstance().getDeplacementFromIDF(((Acces) expr2).getI());
				} catch (ErreurCle e) {
					throw new ErreurVerification("Clé introuvable");
				}

			}
		}

		if (s instanceof SymboleTableau) {

			if (this.expr2 == null && (this.index < 0 || this.index >= getSizeMax())) {
				throw new ErreurVerification("L'indice recherché est hors des valeurs du tableau");
			}

		} else {
			throw new ErreurVerification("Problème de typage");
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
		return "tableau";
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
