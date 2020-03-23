package repint;

import exception.ErreurCle;
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

		if (membreDroite.getType().equals("idf")) {
			Symbole s2 = TDS.getInstance().identifier(new Entree(((Idf) membreDroite)));
			if (s2 instanceof SymboleTableau) {
				throw new ErreurVerification("Les deux cotés de l'opération doivent être des entiers");
			}
		}

	}

	@Override
	public String toMips() throws ErreurGenerationCode, ErreurCle {
		String s = "\n\n# Affectation de " + membreGauche + "\n";

		Symbole symGauche = TDS.getInstance().identifier(new Entree(membreGauche.getI()));

		String typeMembreDroite = membreDroite.getType();

		if (symGauche instanceof SymboleEntier) {
			// System.out.println("ENTIER");

			if (typeMembreDroite.equals("nombre")) {
				s += "li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + membreGauche + "\n"
						+ "sw $v0, " + TDS.getInstance().getDeplacementFromIDF(membreGauche.getI()) + "($s7)";
			} else if (typeMembreDroite.equals("idf")) {

				s += "# Affectation de type x := y\n\n";
				int deplacementVariableDestination = TDS.getInstance().getDeplacementFromIDF(membreGauche.getI());
				String deplacementVariableSource = ((Acces) membreDroite).getAdresse();

				s += "lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de " + membreGauche
						+ " dans v0\n" + "sw $v0, " + deplacementVariableDestination + "($s7)\t# on met la valeur de "
						+ membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c
			} else {
				throw new ErreurGenerationCode("erreur inconnue");
			}

		} else if (symGauche instanceof SymboleTableau) {

//TODO
//REVOIR CETTE PARTIE 

			s += "\n\n#-------- Affectation dans un tableau --------\n\n";
			if (typeMembreDroite.equals("nombre")) {
				
				s+= "#Affectation de type x [ y ] := int\n\n";

//				int vDepartListe = (TDS.getInstance().getDeplacementFromIDF(membreGauche.getI()));

				s += "li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + membreGauche + "\n"
						+ "sw $v0, " + Integer.parseInt(membreGauche.getAdresse()) + "($s7)";
			
			
				
			
			} else if (typeMembreDroite.equals("idf")) {

				s += "# Affectation de type x := y\n\n";
				int deplacementVariableDestination = Integer.parseInt(membreGauche.getAdresse());
				String deplacementVariableSource = ((Acces) membreDroite).getAdresse();

				s += "lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de " + membreDroite
						+ " dans v0\n" + "sw $v0, " + deplacementVariableDestination + "($s7)\t# on met la valeur de "
						+ membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c
			} else if (typeMembreDroite.equals("tableau")) {

				s += "# Affectation de type x [ y ] := z [ i ]\n\n";
				int deplacementVariableDestination = Integer.parseInt(membreGauche.getAdresse());
				String deplacementVariableSource = ((Acces) membreDroite).getAdresse();

//				s += membreDroite.toMips();

				s += "lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de " + membreDroite + " dans v0\n" + 
						"sw $v0, " + deplacementVariableDestination + "($s7)\t# on met la valeur de " + membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c

//				throw new ErreurGenerationCode("assignation entre tableaux pas encore opérationelle");

			} else {
				throw new ErreurGenerationCode("erreur inconnue");
			}

		}

		return s;
	}

}
