package repint;

import java.util.ArrayList;

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

	public String toMips() {
		String s = "";
		for (Instruction instruction : ali) {
			s += instruction.toMips();
		}
		return null;
	}
}
