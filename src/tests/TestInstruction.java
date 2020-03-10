package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import repint.Affectation;
import repint.Bloc;
import repint.Ecrire;
import repint.Idf;
import repint.Instruction;
import repint.Nombre;

public class TestInstruction {

	Instruction i;
	Affectation a1, a2;
	Ecrire e1, e2, e3, e4;
	Idf id1, id2;
	Nombre n1, n2;
	Bloc b1, b2;

	@Before
	public void setUp() throws Exception {
		b1 = new Bloc();
		b2 = new Bloc();

		id1 = new Idf("a1");
		id2 = new Idf("a2");

		n1 = new Nombre(5);
		n2 = new Nombre(10);

		e1 = new Ecrire(id1);
		e2 = new Ecrire(id2);

		e3 = new Ecrire(n1);
		e4 = new Ecrire(n2);

		a1 = new Affectation(id2, n1);
		a2 = new Affectation(id2, n2);

		b1.ajouter(a1);

		b1.ajouter(a2);

		b1.ajouter(e1);
		b1.ajouter(e2);
		b1.ajouter(e3);
		b1.ajouter(e3);

	}

	@After
	public void tearDown() throws Exception {
		b1 = null;
		b2 = null;
	}

	@Test
	public void testBloc1() {
		assertEquals("Le bloc contenir 6 instructions", b1.toString(), "{\n" + "a2 := 5 ;" + "a2 := 10 ;"
				+ "ecrire a1 ;" + "ecrire a2 ;" + "ecrire 5 ;" + "ecrire 5 ;" + "\n}");
	}

	@Test
	public void testBloc2() {
		assertEquals("Le bloc doit être vide", b2.toString(), "{\n\n}");

		b2.ajouter(e1);
		assertEquals("Le bloc doit contenir 1 instructions", b2.toString(), "{\n" + "ecrire a1 ;" + "\n}");
		b2.ajouter(e3);
		assertEquals("Le bloc doit contenir 1 instructions", b2.toString(), "{\n" + "ecrire a1 ;" + "ecrire 5 ;" + "\n}");
	}

}
