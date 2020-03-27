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

		if (membreDroite instanceof Idf) {
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

		s += FonctionAffichage.stringInfos("Calcul de la valeur de l'expression dans $v0") + //
				membreDroite.toMips() + //
				Instruction.empiler() + "" + //
				membreGauche.getAdresse() + "\n" + //
				Instruction.depilerDansV0() + "" + //
				FonctionAffichage.stringInfos("On range $v0 à $a0") + //
				"	sw $v0, 0($a0)\n";

		// }

		return s;
	}

}
