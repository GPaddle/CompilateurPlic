package plic;

import java.io.File;
import java.io.FileNotFoundException;

import analyse.AnalyseurSyntaxique;
import exception.DoubleDeclaration;
import exception.ErreurSyntaxique;
import repint.Bloc;

public class plic {

	File f;

	public plic(String path) {

		if (path.endsWith(".plic")) {

			f = new File(path);
			AnalyseurSyntaxique as;
			try {
				as = new AnalyseurSyntaxique(f);
				try {
					Bloc bloc = as.analyse();
					System.out.println(bloc);
				} catch (ErreurSyntaxique e) {
					// TODO Auto-generated catch block
					System.out.println("ERREUR: " + e.getMessage());

				} catch (DoubleDeclaration e) {
					// TODO Auto-generated catch block
					System.out.println("ERREUR: " + e.getMessage());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("ERREUR: " + e.getMessage());

			}
		} else {
			System.out.println("ERREUR: Suffixe incorrect");
		}

		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ErreurSyntaxique {
		plic p;
		if (args.length == 1) {
			try {
				p = new plic(args[0]);
			} catch (Exception e) {
				System.out.println("ERREUR: " + e.getMessage());
			}
		} else if (args.length == 0) {
			System.out.println("ERREUR: Fichier source absent");
			return;
		} else {
			System.out.println("ERREUR: Trop de fichiers");
		}

	}
}
