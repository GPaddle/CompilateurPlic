package plic;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;

import analyse.AnalyseurSyntaxique;
import exception.ErreurDoubleDeclaration;
import exception.ErreurSyntaxique;
import exception.ErreurVerification;
import repint.Bloc;

public class plic {

	public static int compteLigne = 0;
	private static boolean perso;

	private File f;

	public plic(String path) {

		if (path.endsWith(".plic")) {

			f = new File(path);
			AnalyseurSyntaxique as;
			try {
				as = new AnalyseurSyntaxique(f);
				compteLigne = 0;
				try {
					Bloc bloc = as.analyse();
					try {

						bloc.verifier();

						String code = bloc.toMips();
//						String code = bloc.toString();

						if (!code.equals("")) {
							System.out.println("# " + path + "\n");
							System.out.println(code);

							if (perso) {
								try {

									StringSelection selection = new StringSelection(code);
									Clipboard clipboard = Toolkit.getDefaultToolkit()
											.getSystemClipboard();
									clipboard.setContents(selection, selection);

									System.out.println("# Copié");
								} catch (Exception e) {

									System.out.println("# Erreur lors de la copie");

								}
							}

						} else {
							System.out.println(
									"ERREUR: " + "Problème lors de la génération du code" + "\tLigne : " + compteLigne);
						}

					} catch (ErreurVerification e) {
						System.out.println(
								"ERREUR: " + "Erreur Verification : " + e.getMessage() + "\tLigne : " + compteLigne);
					} catch (Exception e) {
						System.out.println("ERREUR: " + e.getMessage() + "\tLigne : " + compteLigne);
					}
				} catch (ErreurSyntaxique e) {
					System.out
							.println("ERREUR: " + "Erreur Syntaxique : " + e.getMessage() + "\tLigne : " + compteLigne);

				} catch (ErreurDoubleDeclaration e) {
					System.out.println(
							"ERREUR: " + "Double Declaration : " + e.getMessage() + "\tLigne : " + compteLigne);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("ERREUR: " + e1.getMessage() + "\tLigne : " + compteLigne);
				}
			} catch (FileNotFoundException e) {
				System.out.println("ERREUR: " + "File Not Found : " + e.getMessage());

			}
		} else {
			System.out.println("ERREUR: Suffixe incorrect");
		}

	}

	public static void main(String[] args) throws ErreurSyntaxique {
		plic p;
		if (args.length == 1 || (args.length == 2 && args[1].equals("Eclipse"))) {
			try {

				perso = (args.length == 2 && args[1].equals("Eclipse"));

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
