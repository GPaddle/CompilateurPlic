package plic;

import java.io.File;
import java.io.FileNotFoundException;

import analyse.AnalyseurSyntaxique;
import exception.ErreurSyntaxique;

public class plic {

	File f;

	public plic(String path) {

		if (!path.endsWith(".plic")) {
			try {
				throw new ErreurExtension();
			} catch (ErreurExtension e) {
				// TODO Auto-generated catch block
//				System.out.println(e.getMessage());
			}
		}

		f = new File(path);
		AnalyseurSyntaxique as;
		try {
			as = new AnalyseurSyntaxique(f);
			try {
				as.analyse();
				System.out.println("Programme ok");
			} catch (ErreurSyntaxique e) {
				// TODO Auto-generated catch block
//				System.out.println(e.toString());
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			System.out.println(e.getMessage());
			
		}

		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws ErreurSyntaxique {
		plic p;
		if (args.length == 1) {
			try {
				p = new plic(args[0]);
			} catch (Exception e) {
//				System.out.println(e.getMessage());
}
		} else if (args.length == 0 ){
			System.out.println("ERREUR: Fichier source absent");
			return;
		}else {
			System.out.println("ERREUR: Trop de fichiers");
		}

	}
}
