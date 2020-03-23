package repint;

import java.util.ArrayList;

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

	public void verifier() throws ErreurVerification {

		for (Instruction instruction : ali) {
			instruction.verifier();
		}

	}

	public String toMips() throws Exception {
		String s = "";

		TDS tab = TDS.getInstance();
		int reserve = tab.getCptDepl();

//		String newLine = System.getProperty("line.separator");

		s += ".data\n" + "	newLine: .asciiz \"\\n\"\n" //
				+ "	 messageIndex: .asciiz \"; index: \"\n" //
				+ "	 messageHorsDomaine: .asciiz \"ERREUR: valeur recherchée hors domaine: \"\n" //
				+ "	 messageTailleMax: .asciiz \"taille de la liste: \"\n" //
				+ ".text\n" //
				+ "main:\n";
		s += tab + "";
		s += "\n\n";

		s += "# creation des variables\n" //
				+ "	move $s7, $sp\n" //
				+ "	add $sp, $sp, " + reserve + "\n" //
				+ "	# on réserve 4 bits par entier présent dans la TDS\n" //
				+ "	# on réserve 4* n bits par tableau de n cases dans la TDS\n";

		for (Instruction instruction : ali) {
			s += "	" + instruction.toMips();
		}

		s += "\n\n#----------------------------\n" //
				+ "# Fonctions pour terminer le programme\n" //
				+ "exit:\n" //
				+ "	 li $v0, 17	#code de sortie du programme\n" //
				+ "	 syscall\n" //
				+ "\n";

		if (TDS.getInstance().contientTableau()) {

			s += "#----------------------------\n" //
					+ "# Fonctions pour gérer les exceptions \n" //
					+ "# Si il y a un appel hors du domaine du tableau\n\n" //
					+ "exceptionValeurHorsDomaine:\n" //
					+ "	 move $t1, $v0\n" //
					+ "	 li $v0, 4\n" //
					+ "	 # On apelle le message 'hors domaine'\n" //
					+ "	 la $a0, messageHorsDomaine\n" //
					+ "	 # Puis on l'affiche\n" //
					+ "	 syscall\n\n" //
					//
					//
					+ "	 # On affiche la taille max du tableau\n" //
					+ "	 la $a0, messageTailleMax\n" //
					+ "	 syscall\n\n" //
					//
					//
					+ "	 move $a0, $t0\n" //
					+ "	 li $v0, 1\n" //
					+ "	 syscall\n\n" //
					+ "	 # On affiche l'index désiré\n" //
					+ "	 la $a0, messageIndex\n" //
					+ "	 li $v0, 4\n" //
					+ "	 syscall\n\n" //
					+ "	 move $v0, $t1\n\n" //
					//
					//
					+ "	 # Saut de ligne\n" //
					+ "	 # On affiche $a0\n"//
					+ "	 move $a0, $v0\n" //
					+ "	 li $v0, 1\n" //
					+ "	 syscall\n\n" //
					//
					//
					+ "	 # affichage du saut de ligne \"\\n\"\n" //
					+ "	 li $v0, 4\n" //
					+ "	 la $a0, newLine\n" //
					+ "	 syscall\n" //
					//
					//
					+ "	 li $a0, 1\n" //
					+ "	 j exit\n"; //
		}
		return s;
	}
}
