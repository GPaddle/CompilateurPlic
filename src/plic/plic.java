package plic;

import java.io.File;
import java.io.FileNotFoundException;

import analyse.AnalyseurSyntaxique;
import exception.DoubleDeclaration;
import exception.ErreurSyntaxique;
import repint.Bloc;
import repint.ErreurVerification;

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
					try {
						bloc.verifier();

						String code = bloc.toMips();

						if (code.equals("")) {
							System.out.println(code);
						} else {
							System.out.println("ERREUR: " + "Problème lors de la génération du code");
						}

					} catch (ErreurVerification e) {
						System.out.println("ERREUR: " + "Erreur Verification : Problème avec " + e.getMessage());
					}
				} catch (ErreurSyntaxique e) {
					System.out.println("ERREUR: " +"Erreur Syntaxique : "+ e.getMessage());

				} catch (DoubleDeclaration e) {
					System.out.println("ERREUR: " +"Double Declaration : "+ e.getMessage());
				}
			} catch (FileNotFoundException e) {
				System.out.println("ERREUR: " +"File Not Found : "+ e.getMessage());

			}
		} else {
			System.out.println("ERREUR: Suffixe incorrect");
		}

	}

	public static void main(String[] args) throws ErreurSyntaxique {
		plic p;
		if (args.length == 1) {
			try {
				p = new plic(args[0]);
			} catch (Exception e) {
				System.out.println("ERREUR: Inconnue" + e.getMessage());
			}
		} else if (args.length == 0) {
			System.out.println("ERREUR: Fichier source absent");
			return;
		} else {
			System.out.println("ERREUR: Trop de fichiers");
		}

	}
}
