package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import exception.ErreurDoubleDeclaration;
import repint.Entree;
import repint.Symbole;
import repint.SymboleEntier;
import repint.TDS;

public class TDSTest {

	public TDS tds;
	public Entree e;
	public Symbole s;

	@Before
	public void before() {
		tds = TDS.getInstance();
		e = new Entree("a");
		s = new SymboleEntier(0);
	}

	@After
	public void after() {
		TDS.supress();
		e = null;
		s = null;
	}

	@Test
	public void testGetInstance() {
		assertTrue("Les deux instances devraient être égales", tds.equals(TDS.getInstance()));
	}

	@Test
	public void testAjouter() throws ErreurDoubleDeclaration, Exception {
		assertTrue("La liste devrait être vide", tds.getListeSize() == 0);
		tds.ajouter(e, s);
		assertTrue("La liste devrait contenir un élément", tds.getListeSize() == 1);
		assertTrue("L'entrée " + e + " devrait être dans la liste", tds.identifier(e) != null);
	}

	@Test
	public void testAjouter2Var() throws ErreurDoubleDeclaration, Exception {
		Entree e2 = new Entree("b");
		Symbole s2 = new SymboleEntier(-4);

		tds.ajouter(e, s);
		tds.ajouter(e2, s2);

		assertTrue("L'entrée " + e + " devrait être dans la liste", tds.identifier(e) != null);
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}

	@Test(expected = ErreurDoubleDeclaration.class)
	public void testAjouterPb() throws ErreurDoubleDeclaration, Exception {

		tds.ajouter(e, s);
		tds.ajouter(e, s);

	}

}
