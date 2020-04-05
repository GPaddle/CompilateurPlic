package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class Pour extends Iteration {

	Idf identifiant;
	Expression e1, e2;

	public static int nbBoucle = 1;
	public static int idUnique = 1;

	public Pour(Idf idf, Expression e1, Expression e2, Bloc b) {
		blocARepeter = b;
		this.identifiant = idf;
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public void verifier() throws ErreurVerification, ErreurSemantique {

		super.verifier();

		this.identifiant.verifier();

		if (!e1.getType().equals(Expression.typeEntier)) {
			throw new ErreurVerification("L'expression " + e1 + " doit être de type entière");
		}

		if (!e2.getType().equals(Expression.typeEntier)) {
			throw new ErreurVerification("L'expression " + e2 + " doit être de type entière");
		}

		this.e1.verifier();
		this.e2.verifier();

	}

	@Override
	public String toMips() throws ErreurGenerationCode, Exception {
		int idDepart = idUnique++;
		String s = FonctionAffichage
				.stringInstruction("Pour " + identifiant + " dans " + e1 + " .. " + e2 + " repeter ...") + //
				e2.toMips() + "\n" + //
				Instruction.empiler() + "\n" + //
				e1.toMips() + "\n" + //
				Instruction.depilerDansV1() + "\n" + //
				// Si la valeur de e2 est plus grande ou égale à e1, on entre dans la boucle
				"	bge $v1, $v0, bouclePour" + idDepart+"a"+nbBoucle + "\n" + //
				"	b finBouclePour" + idDepart+"a"+nbBoucle + "\n" + //
				"	bouclePour" + idDepart+"a"+nbBoucle + " : " + "\n" + //
				"	"+identifiant.getAdresse()+"\n"+//
				"	sw $v0, 0($a0)" + "\n" + //
				"	add $v0, $v0, 1" + "\n" + //
				Instruction.empiler() + //
				"	move $v0, $v1" + "\n" + //
				Instruction.empiler(); //
		nbBoucle++;
		for (Instruction instruction : blocARepeter.getAli()) {
			s += instruction.toMips();
		}
		nbBoucle--;
		
		s += Instruction.depilerDansV1() + "\n" + //
				Instruction.depilerDansV0() + "\n" + //
				"	blt $v1, $v0, finBouclePour" + idDepart+"a"+nbBoucle + "\n" + //
				"	bge $v1, $v0, bouclePour" + idDepart+"a"+nbBoucle + "\n" + //
				"	finBouclePour" + idDepart+"a"+nbBoucle + " : " + "\n";//

		
		
		return s;
	}

	@Override
	public String toString() {
		String s = "	Pour " + identifiant + " dans " + e1 + " .. " + e2 + "\n" + //
				"		repeter " + blocARepeter;
		return s;
	}

}
