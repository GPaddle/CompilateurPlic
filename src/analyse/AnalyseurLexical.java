package analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * AnalyseurLexical
 */
public class AnalyseurLexical {

	Scanner sc;
	int nbLignes = 0;
	String newLine = System.lineSeparator();

	public AnalyseurLexical(File f) throws FileNotFoundException {
		sc = new Scanner(f);
	}

	public String next() {

		String s = "";

		if (sc.hasNext()) {
			while (s.equals("")) {
				if (sc.hasNext()) {
					s = sc.next();

					s = contientCommentaire(s);
				} else {
					s = "EOF";
				}
			}
		} else {
			s = "EOF";
		}
		return s;
	}

	private String contientCommentaire(String s) {
		if (s.startsWith("//")) {
			if (sc.hasNext()) {
				sc.nextLine();

				this.nbLignes++;

			}
			s = "";
		}
		return s;
	}
}