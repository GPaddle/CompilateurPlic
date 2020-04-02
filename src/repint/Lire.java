package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class Lire extends Instruction {

	Idf identifiant;

	public Lire(Idf exp1) {
		identifiant = exp1;
	}

	@Override
	public void verifier() throws ErreurVerification, ErreurSemantique {
		// TODO Auto-generated method stub

	}

	@Override
	public String toMips() throws ErreurGenerationCode, Exception {
		String s = 
				FonctionAffichage.stringInstruction("Lire "+identifiant)+"\n" + //
				"	li $v0 , 5 	# $v0 <- 5 (code du read entier)"+"\n" +// 
				"	syscall 	# lire ; le r?sultat est dans $V0"+"\n"+//
				identifiant.getAdresse()+"\n"+
				"	sw $v0, 0($a0)"+"\n";

		return s;
	}

	@Override
	public String toString() {
		return "lire " + identifiant;
	}

}
