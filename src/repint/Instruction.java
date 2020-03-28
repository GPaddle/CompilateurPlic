package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public abstract class Instruction {

	public abstract void verifier() throws ErreurVerification, ErreurSemantique;

	public abstract String toMips() throws ErreurGenerationCode, Exception;

	public static String empiler() {
		return FonctionAffichage.stringInfos("On empile") + //
				"	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)\n"+//
				"	add $sp, $sp, -4	# Pour laisser de la place dans la pile\n"; //
	}

	public static String depilerDansV0() {

		return FonctionAffichage.stringInfos("On depile") + //
				"	add $sp, $sp 4	# On remet la pile au bon endroit\n" + //
				"	lw $v0, 0($sp)	# Pour stocker la valeur à $sp dans $v1\n";//
	}

	public static String depilerDansV1() {
		
		return FonctionAffichage.stringInfos("On depile") + //
				"	add $sp, $sp 4	# On remet la pile au bon endroit\n" + //
				"	lw $v1, 0($sp)	# Pour stocker la valeur à $sp dans $v1\n";//
	}

}
