package plic;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;

import com.sun.glass.ui.Clipboard;

import analyse.AnalyseurSyntaxique;
import exception.ErreurDoubleDeclaration;
import exception.ErreurSyntaxique;
import exception.ErreurVerification;
import repint.Bloc;

public class plic {

	public static int compteLigne = 0;
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

//								StringSelection selection = new StringSelection(code);
//								java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit()
//										.getSystemClipboard();
//								clipboard.setContents(selection, selection);
							

						} else {
							System.out.println(
									"ERREUR: " + "Problème lors de la génération du code" + "\tLigne : " + compteLigne);
						}

					} catch (ErreurVerification e) {
						System.out.println(
								"ERREUR: " + "Erreur Verification : " + e.getMessage() + "\tLigne : " + compteLigne);
					} catch (Exception e) {
						System.out.println("ERREUR: " + e.getMessage() + "\tLigne : " + compteLigne);
						e.printStackTrace();
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
