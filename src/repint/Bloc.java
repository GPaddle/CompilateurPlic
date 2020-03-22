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
			s += "\t" + instruction + "\n";
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

		s += ".data\n" + "\tnewLine: .asciiz \"\\n\"\n" + ".text\n" + "\tmain:";
		s += tab + "";
		s += "\n\n";

		s += "# creation des variables\n" + "move $s7, $sp\n" + "add $sp, $sp, " + reserve
				+ "\n # on réserve 4 bits par entier présent dans la TDS"
				+ "\n # on réserve 4* n bits par tableau de n cases dans la TDS";

		for (Instruction instruction : ali) {
			s += instruction.toMips();
		}
		return s;
	}
}
