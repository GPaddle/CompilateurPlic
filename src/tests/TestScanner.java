package tests;

import java.io.File;
import java.io.FileNotFoundException;
import analyse.AnalyseurLexical;

public class TestScanner {
	public static void main(String[] args) throws FileNotFoundException {

		AnalyseurLexical al = new AnalyseurLexical(new File("src/sources/PLIC0n3.plic"));

		String s="";
		while (!s.equals("EOF")) {
			s = al.next();
			System.out.println(s);

		}
	}

}