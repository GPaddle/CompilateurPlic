package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public abstract class Instruction {

	public abstract void verifier() throws ErreurVerification;

	public abstract String toMips() throws ErreurGenerationCode, Exception;

	public static String empiler() {
		return FonctionAffichage.stringInfos("On empile") + //
				"	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)\n"+//
				"	add $sp, $sp, -4	# Pour laisser de la place dans la pile\n"; //
	}

	public static String depiler() {

		return FonctionAffichage.stringInfos("On depile") + //
				"	add $sp, $sp 4	# On remet la pile au bon endroit\n" + //
				"	lw $v0, 0($sp)	# Pour stocker la valeur à $sp dans $v1\n";//
	}

}
