package repint;

import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Affectation extends Instruction {

	private Expression membreDroite;
	private Acces membreGauche;

	public Affectation(Acces i, Expression e) {
		super();
		this.membreDroite = e;
		this.membreGauche = i;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return membreGauche + " := " + membreDroite + " ;";
	}

	@Override
	public void verifier() throws ErreurVerification {

		TDS tSymbole = TDS.getInstance();
		Symbole s = tSymbole.identifier(new Entree(membreGauche.getI()));

		if (s == null) {
			throw new ErreurVerification("Affectation");
		}

	}

	@Override
	public String toMips() throws Exception {
		String s = "\n\n# Affectation de " + membreGauche + "\n";

		Symbole sym = TDS.getInstance().identifier(new Entree(membreGauche.getI()));

		if (sym instanceof SymboleEntier) {
			// System.out.println("ENTIER");

			if (membreDroite instanceof Nombre) {
				s += "li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + membreGauche + "\n" + "sw $v0, "
						+ TDS.getInstance().getDeplacementFromIDF(membreGauche.getI()) + "($s7)";
			} else if (membreDroite instanceof Acces) {
				
				s+="# Affectation de type x := y\n\n";
				int deplacementVariableDestination = TDS.getInstance().getDeplacementFromIDF(membreGauche.getI());
				String deplacementVariableSource = ((Acces) membreDroite).getAdresse();

				s += "lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de "+membreGauche+" dans v0\n" +
				"sw $v0, " + deplacementVariableDestination+ "($s7)\t# on met la valeur de " + membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c
			} else {
				throw new ErreurGenerationCode("ERREUR: erreur inconnue");
			}

		} else if (sym instanceof SymboleTableau) {

			throw new ErreurGenerationCode("ERREUR: Pas de tableaux pour le moment");
			// System.out.println("TABLEAU");
			/*
			 * String adresse = new Acces(i.getI()).getAdresse();
			 * 
			 * System.out.println(i); System.out.println(i); System.out.println(i);
			 * 
			 * if (e instanceof Nombre) { s += "" + e + "\n" + "# on stocke " + e + " dans "
			 * + i + "\n" + "sw $v0, " + TDS.getInstance().getDeplacementFromIDF(i.getI()) +
			 * "($s7)"; } else if (e instanceof Acces) { throw new
			 * ErreurGenerationCode("ERREUR: Pas d'affectation entre 2 variables pour le moment Acces"
			 * ); } else if (e instanceof Idf) { //On calcul l'adresse à laquelle il faut
			 * mettre la valeur stockée dans e throw new
			 * ErreurGenerationCode("ERREUR: Pas d'affectation entre 2 variables pour le moment IDF"
			 * ); } else { throw new ErreurGenerationCode("ERREUR: erreur inconnue"); }
			 */

		}

		return s;
	}

}
