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
				s += "	li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + //
						membreGauche + "\n" + //
						"	sw $v0, " + TDS.getInstance().getDeplacementFromIDF(membreGauche.getI()) + "($s7)";
			} else if (typeMembreDroite.equals("idf")) {

				s += "# Affectation de type x := y\n\n";
				int deplacementVariableDestination = TDS.getInstance().getDeplacementFromIDF(membreGauche.getI());
				String deplacementVariableSource = ((Acces) membreDroite).getAdresse();

				s += "	lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de " + //
						membreGauche + " dans v0\n" + //
						"	sw $v0, " + deplacementVariableDestination + "($s7)\t# on met la valeur de " + //
						membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c
			} else {
				throw new ErreurGenerationCode("erreur inconnue");
			}

		} else if (symGauche instanceof SymboleTableau) {

//TODO
//REVOIR CETTE PARTIE 

			s += "\n\n#-------- Affectation dans un tableau --------\n\n";
			if (typeMembreDroite.equals("nombre")) {

				AccesTableau aTab = ((AccesTableau) membreGauche);

				s += "#Affectation de type x [ y ] := int\n\n";

				int sizeMax = ((SymboleTableau) symGauche).getSize();
				

				if (aTab.getIndex() == -1 && aTab.getExpr() != null) {
					// On fait une affectation de type tab [ acces ]

//					String adrTab = aTab.getAdresse();

					int adresseTableau = TDS.getInstance().getDeplacementFromIDF(aTab.getI());

					int adresseIdentifiantInterne = TDS.getInstance().getDeplacementFromIDF((Idf) aTab.getExpr());

					s += "	lw $v0, " + adresseIdentifiantInterne + "($s7) \n" + //
							"	move $t0, $v0\n" + //
							"	la $a0, " + adresseTableau + "($s7)		# " + adresseTableau
							+ "($s7) c'est notre tableau\n" + //
							"	mulu $t0, $t0, 4	# pour le 4*indice (parce que les entiers prennent 4 octets)\n" + //
							"	subu $a0, $a0, $t0	# on décale l'addresse du tableau de base avec $t0;\n\n" + //

							"	li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + membreGauche
							+ "\n" + //
							"	sw $v0, ($a0)";


				} else if (aTab.getIndex() >= 0 && aTab.getExpr() == null) {
					// On fait une affectation de type tab [ int ]
	/*				
int indice = aTab.getIndex();


					s += "# " + membreGauche + " := " + membreDroite + "\n" + //
							"	li $v0, " + indice + "\n" + //
							"	li $t0, " + sizeMax + "\n" + //
							"	bge $v0, $t0, exceptionValeurHorsDomaine\n" + //
							"# si on dépasse l'indice max du tableau \n" + //
							"	li $t1, 0\n" + //
							"	blt $v0, $t1, exceptionValeurHorsDomaine\n" + //
							"# si on a un indice négatif\n";
*/					
					s += "	li $v0," + membreDroite + "\t" + "# on stocke " + membreDroite + " dans " + membreGauche
							+ "\n" + //
							"	sw $v0, " + Integer.parseInt(membreGauche.getAdresse()) + "($s7)";
				} else {
					throw new ErreurGenerationCode("Probleme sur l'intérieur des [ ] dans Affectation.class");
				}

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

				s += "lw $v0, " + deplacementVariableSource + "($s7)\t# on stocke la valeur de " + membreDroite
						+ " dans v0\n" + "sw $v0, " + deplacementVariableDestination + "($s7)\t# on met la valeur de "
						+ membreDroite + " dans " + membreGauche + "";
				// c := b --> on met la valeur de b dans c

//				throw new ErreurGenerationCode("assignation entre tableaux pas encore opérationelle");

			} else {
				throw new ErreurGenerationCode("erreur inconnue");
			}

		}

		return s;
	}

}
