import java.io.*;
import java.util.ArrayList;

/**
 * Moulinette for Plic
 *
 * valide Plic sources files should only contain number + ".plic extension:
 * (ex: 0.plic 1.plic 101.plic ...)
 * every others names is considered invalide Plic and compiler should generate error
 *
 * Currently support Plic0
 * @author Louis
 * @version v1.1
 */
class Moulinette {
	public static void main(String[] args) throws IOException {
		System.out.println("Moulinette v1.1 by Louis");
		if (args.length != 2) {
			System.out.println("usage: java Moulinette /path/to/plic.jar /path/to/test/sources/");
			System.exit(1);
		}
		File plic = new File(args[0]);
		File dir = new File(args[1]);
		if (!args[0].endsWith(".jar")) {
			System.out.println(args[0] + " must be .jar");
			System.exit(2);
		}
		if (!plic.exists()) {
			System.out.println("Compiler not found: " + args[0]);
			System.exit(3);
		}
		if (!dir.exists()) {
			System.out.println("Test directory not found: " + args[1]);
			System.exit(4);
		}
		File dirValide = new File(dir.getAbsolutePath() + "/valide");
		File dirInvalide = new File(dir.getAbsolutePath() + "/invalide");
		if (!dirValide.exists()) {
			System.out.println("Valide sources files directory not found: " + dirValide.getPath());
			System.exit(5);
		}
		if (!dirInvalide.exists()) {
			System.out.println("Invalide sources files directory not found: " + dirInvalide.getPath());
			System.exit(6);
		}
		ArrayList<File> valide = new ArrayList<>();
		ArrayList<File> invalide = new ArrayList<>();

		for (File f : dirValide.listFiles()) {
			if (f.isFile()) {
				valide.add(f);
			}
		}
		for (File f : dirInvalide.listFiles()) {
			if (f.isFile()) {
				invalide.add(f);
			}
		}

		Runtime rt = Runtime.getRuntime();
		int success = 0;

		/**
		 * Test valide plic source
		 */
		System.out.println("\nTest valides sources files...");
		for (File f : valide) {
			System.out.print(f.getName());
			Process proc = rt.exec("java -jar \"" + plic.getAbsolutePath() + "\" \"" + f.getAbsolutePath() + "\"");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			// Read the output from the command
			String s = stdout.readLine();
			if (s == null) {
				System.out.println(" OK");
				success++;
			} else
				System.out.println(" FAIL: " + s);
		}

		/**
		 * Test invalide plic source
		 * expect "ERREUR:" on stdout
		 */
		System.out.println("\nTest invalides sources files...");
		for (File f : invalide) {
			System.out.print(f.getName());
			Process proc = rt.exec("java -jar \"" + plic.getAbsolutePath() + "\" \"" + f.getAbsolutePath() + "\"");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			// Read the output from the command
			String s = stdout.readLine();
			if (s != null && s.startsWith("ERREUR:")) {
				System.out.println(" OK");
				success++;
			} else
				System.out.println(" FAIL: " + s);
		}

		System.out.println("\nResult: " + success + "/" + (valide.size() + invalide.size()));
	}
}