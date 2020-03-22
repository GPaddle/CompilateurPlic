package repint;

import exception.ErreurCle;
import exception.ErreurGenerationCode;
import exception.ErreurVerification;

public class Acces extends Expression {

	Idf i;
	Expression expr;

	public Idf getI() {
		return i;
	}

	public void setI(Idf i) {
		this.i = i;
	}

	public Acces(Idf p_i) {
		this.i = p_i;
	}

	@Override
	public String toString() {
		return i+"";
	}

	public String toMips() throws ErreurGenerationCode {

		try {
			return getAdresse();
		} catch (ErreurCle e) {
			// TODO Auto-generated catch block
			throw new ErreurGenerationCode("Problème avec la variable (accès)");
		}

	}

	public String getAdresse() throws ErreurCle {
		TDS table = TDS.getInstance();
		Symbole s1 = table.identifier(new Entree(i));

//		System.out.println(s1);

		return "" + table.getDeplacementFromIDF(this.i);

	}

	public String getAdresse(int index) throws ErreurCle {
		TDS table = TDS.getInstance();
		Symbole s1 = table.identifier(new Entree(i));

		System.out.println("-----------");
		System.out.println(s1);
		System.out.println("-----------");

		int index2 = table.getDeplacementFromIDF(this.i) - 4 * index;

		// TODO
		// Revoir cette partie (faite rapidement)
		return "" + index2;
	}

	@Override
	public void verifier() throws ErreurVerification {

		i.verifier();

	}

	public void ajoutExpression(Expression expr) {
		this.expr = expr;
	}

	@Override
	public String getType() {
		return "acces";
	}

}
