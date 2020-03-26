package repint;

import Affichage.FonctionAffichage;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Ecrire extends Instruction {
	private Expression e;

	public Ecrire(Expression e) {
		super();
		this.e = e;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ecrire " + e + " ;";
	}

	@Override
	public void verifier() throws ErreurVerification {

		String typeE = e.getType();

		if (typeE.equals("idf")) {
			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((Idf) e).toString()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}
		} else if (typeE.equals("tableau")) {

			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((AccesTableau) e).getI()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}

		} else {
			throw new ErreurVerification("Type pas encore implémenté : Ecrire.class");
		}

	}

	@Override
	public String toMips() throws Exception {
		String s = FonctionAffichage.stringInstruction("Ecrire " + e);
//				"\n\n# ------------- ecrire " + e + "\n";
		// +// " li $v0, 1 \t# on prépare l'affichage des variables";

		if (e instanceof Idf) {
//			s += "\nlw $a0, " + TDS.getInstance().getDeplacementFromIDF((Idf) e) + "($s7)\t# on affiche " + e
			int adresse = TDS.getInstance().getDeplacementFromIDF((Idf) e);
			s += "\n" + //
					e.toMips() + "\n" + //
					// " move $a0, $v0\n" + //

					"	li $v0, 1 	# on prépare l'affichage des variables\n" + //
					"	lw $a0, " + adresse + "($s7)\n" + //
					FonctionAffichage.stringInfos("on affiche " + e) + //
					"	syscall 	# ecrire";
		} else if (e instanceof Nombre) {
			throw new ErreurGenerationCode("On n'écrit pas de nombre en PLIC0");
		} else if (e instanceof AccesTableau) {

			AccesTableau aTab = (AccesTableau) e;

//			s = "\n\n# affichage de " + e + "\n\n";

			if (aTab.getIndex() > -1) {
				int deplacement = TDS.getInstance().getDeplacementFromIDF(aTab.getI())-4*aTab.getIndex();
				s += "	li $v0, 1 	# on prépare l'affichage des variables\n" + //
						"	lw $a0, " + deplacement + "($s7)	# on affiche " + e + "\n" + //
						"	syscall 	# ecrire";
			} else {
				int adrTab = TDS.getInstance().getDeplacementFromIDF(aTab.getI());

//				System.out.println(aTab.getExpr().getType());

				if (!aTab.getExpr().getType().equals("idf")) {
					throw new ErreurGenerationCode("On écrit à partir d'un idf uniquement : Ecrire.class");
				}

				int adresse = TDS.getInstance().getDeplacementFromIDF((Idf) aTab.getExpr());

				s += "	lw $v0, " + adresse + "($s7) \n" + //
						"	move $t0, $v0\n" + //
						"	la $a0, " + adrTab + "($s7)		# " + adrTab + "($s7) c'est notre tableau\n" + //
						"	mulu $t0, $t0, 4	# pour le 4*indice (parce que les entiers prennent 4 octets)\n" + //
						"	subu $a0, $a0, $t0	# on décale l'addresse du tableau de base avec $t0;\n\n" + //
						"	lw $v0, ($a0)		# on affiche " + e + "\n" + //

						"	move $a0, $v0\n" + //
						"	li $v0, 1\n" + //
						"	syscall\n" + //

						"# On saute une ligne\n" + //
						"	li $v0, 4\n" + //
						"	la $a0, newLine\n" + //
						"	syscall\n" +
//						"	li $v0, 4\n"+
//						"	la $a0,($)\n"+
//						"	syscall\n"+
						"	li $a0, 0";
			}

//			throw new ErreurGenerationCode("On n'écrit pas de tableau en PLIC1");
		} else {
			throw new ErreurGenerationCode("cas inconnu");
		}

//		String newLine = System.getProperty("line.separator");

		s += "\n\n# Affichage du saut de ligne \n" + //
				"	li $v0, 4\n" + //
				"	la $a0, newLine\n" + //
				"	syscall\n";
		return s;
	}

}
