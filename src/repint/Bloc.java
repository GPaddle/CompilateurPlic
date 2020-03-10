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
			s += "\t"+instruction+"\n";
		}

		s += "}";

		return s;
	}
}
