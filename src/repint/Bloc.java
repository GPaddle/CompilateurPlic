package repint;

import java.util.ArrayList;

import Affichage.FonctionAffichage;
import exception.ErreurSemantique;
import exception.ErreurVerification;

public class Bloc {

	ArrayList<Instruction> ali = new ArrayList<>();

	public void ajouter(Instruction i) {
		this.ali.add(i);
	}

	@Override
	public String toString() {
		String s = "{\n";

		for (Instruction instruction : ali) {
			s += "	" + instruction + "\n";
		}

		s += "}";

		return s;
	}

	public void verifier() throws ErreurVerification, ErreurSemantique {

		for (Instruction instruction : ali) {
			instruction.verifier();
		}

	}

	public String toMips() throws Exception {
		String s = "";

		TDS tab = TDS.getInstance();
		int reserve = tab.getCptDepl();

//		String newLine = System.getProperty("line.separator");

		s += ".data\n" + //
				"	newLine: .asciiz \"\\n\"\n" + //
				"	messageIndex: .asciiz \"; index: \"\n" + //
				"	messageHorsDomaine: .asciiz \"ERREUR: valeur recherchée hors domaine: \"\n" + //
				"	messageTailleMax: .asciiz \"taille de la liste: \"\n" + //
				".text\n" + //
				"main:\n";
		s += tab + "";
		s += "\n\n";

		s += FonctionAffichage.stringInfos("creation des variables") //
				+ "	move $s7, $sp\n" //
				+ "	add $sp, $sp, " + reserve + "\n" //
				+ FonctionAffichage.stringInfos("on réserve 4 bits par entier présent dans la TDS") //
				+ FonctionAffichage.stringInfos("chaque case d'un tableau prend 4 bits");

		s += "\n\n#-----------------------------------------------------------\n";
		s += "#--------------------	Instructions :	--------------------\n";

		for (Instruction instruction : ali) {
			s += "	" + instruction.toMips();
		}

		s += "\n\n#------------------	Fin instructions :	------------------\n" + //
				"#-----------------------------------------------------------------\n" + //
				"\n\n#----------------------------\n" + //
				FonctionAffichage.stringInfos("Fonctions pour terminer le programme") + //
				"exit:\n" + //
				"	 li $v0, 17	#code de sortie du programme\n" + //
				"	 syscall\n" + //
				"\n";

		if (TDS.getInstance().contientTableau()) {

			s += "#----------------------------\n" + //
					FonctionAffichage.stringInfos("Fonctions pour gérer les exceptions") + //
					FonctionAffichage.stringInfos("Si il y a un appel hors du domaine du tableau\n") + //
					"exceptionValeurHorsDomaine:\n" + //
					"	 move $t1, $v0\n" + //
					"	 li $v0, 4\n" + //
					FonctionAffichage.stringInfos("On apelle le message 'hors domaine'") + //
					"	 la $a0, messageHorsDomaine\n" + //
					FonctionAffichage.stringInfos("Puis on l'affiche") + //
					"	 syscall\n\n" +
					//
					//
					FonctionAffichage.stringInfos("On affiche la taille max du tableau") + //
					"	 la $a0, messageTailleMax\n" + //
					"	 syscall\n\n" + //
					//
					//
					"	 move $a0, $t0\n" + //
					"	 li $v0, 1\n" + //
					//
					"	 syscall\n\n" + //
					//
					FonctionAffichage.stringInfos("On affiche l'index désiré") + //
					"	 la $a0, messageIndex\n" + //
					"	 li $v0, 4\n" + //
					"	 syscall\n\n" + //
					"	 move $v0, $t1\n\n" + //
					//
					//
					FonctionAffichage.stringInfos("Saut de ligne") + //
					FonctionAffichage.stringInfos("On affiche $a0") + //
					"	 move $a0, $v0\n" + //
					"	 li $v0, 1\n" + //
					"	 syscall\n\n" + //
					//
					//
					FonctionAffichage.stringInfos("Affichage du saut de ligne \"\\n\"") + //
					"	 li $v0, 4\n" + //
					"	 la $a0, newLine\n" + //
					"	 syscall\n" + //
					//
					//
					"	 li $a0, 1\n" + //
					"	 j exit\n"; //
		}
		return s;
	}
}
