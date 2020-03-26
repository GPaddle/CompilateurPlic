package repint;

import Affichage.FonctionAffichage;
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
		String s = FonctionAffichage.stringInstruction(membreGauche + " := " + membreDroite);

		Symbole symGauche = TDS.getInstance().identifier(new Entree(membreGauche.getI()));

		String typeMembreDroite = membreDroite.getType();

	/*	if (symGauche instanceof SymboleEntier) {
			// System.out.println("ENTIER");


			switch (typeMembreDroite) {

			case "nombre":
			case "idf":
			case "somme":
			case "soustraction":
			case "multiplication":

				s += membreDroite.toMips();
				s += FonctionAffichage.stringInfos("On assigne " + membreGauche);
				break;

			default:
				throw new ErreurGenerationCode("Type pas encore pris en charge : " + typeMembreDroite);
			}

			String adresseAffectation = membreGauche.toMips();

			s += adresseAffectation + " # on met la valeur de " + //
					membreDroite + " dans " + membreGauche + "";

		} else if (symGauche instanceof SymboleTableau) {
*/
			
			
			/*
			 * Acces := Expression /////////////////////////////////////////////////////////
			 * 
			 * Code qui calcule la valeur de l’expression dans $v0 /////////////////////////
			 * Empiler $v0 /////////////////////////////////////////////////////////////////
			 * Code qui calcule l’adresse de l’accès dans $a0 //////////////////////////////
			 * Dépiler dans $v0 ////////////////////////////////////////////////////////////
			 * Ranger $v0 à l’adresse contenue dans $a0 ////////////////////////////////////
			 */

			s += FonctionAffichage.stringInfos("Calcul de la valeur de l'expression dans $v0")+//
					membreDroite.toMips() + //
					Instruction.empiler() + "" + //
					membreGauche.getAdresse() + "\n" + //
					Instruction.depiler() + "" + //
					FonctionAffichage.stringInfos("On range $v0 à $a0") + //
					"	sw $v0, 0($a0)\n";


		//}

		return s;
	}

}
