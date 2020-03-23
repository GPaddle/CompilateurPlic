package repint;

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
		} else if(typeE.equals("tableau")){

			TDS tSymbole = TDS.getInstance();
			Symbole s = tSymbole.identifier(new Entree(((AccesTableau) e).getI()));
			if (s == null) {
				throw new ErreurVerification("ES");
			}
			
		}else {
			throw new ErreurVerification("Type pas encore implémenté : Ecrire.class");
		}

	}

	@Override
	public String toMips() throws Exception {
		String s = "\n\n# affichage de " + e + "\n\n" + "li $v0, 1 \t# on prépare l'affichage des variables";

		if (e instanceof Idf) {
			s += "\nlw $a0, " + TDS.getInstance().getDeplacementFromIDF((Idf) e) + "($s7)\t# on affiche " + e
					+ "\nsyscall \t# ecrire";
		} else if (e instanceof Nombre) {
			throw new ErreurGenerationCode("On n'écrit pas de nombre en PLIC0");
		} else if (e instanceof AccesTableau) {
			
			s= "\n\n# affichage de " + e + "\n\n" + "li $v0, 1 \t# on prépare l'affichage des variables";
			
			s += "\nlw $a0, " + ((AccesTableau) e).getAdresse() + "($s7)\t# on affiche " + e
					+ "\nsyscall \t# ecrire";
			
			
//			throw new ErreurGenerationCode("On n'écrit pas de tableau en PLIC1");
		} else {
			throw new ErreurGenerationCode("cas inconnu");
		}

//		String newLine = System.getProperty("line.separator");

		s += "\n\n#affichage du saut de ligne \n" 
				+ "\nli $v0, 4" 
				+ "\nla $a0, newLine" 
				+ "\nsyscall";
		return s;
	}

}
