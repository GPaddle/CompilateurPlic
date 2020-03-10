package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import exception.DoubleDeclaration;
import repint.Entree;
import repint.Symbole;
import repint.TDS;

public class TDSTest {

	public TDS tds;
	public Entree e;
	public Symbole s;

	@Before
	public void before() {
		tds = TDS.getInstance();
		e = new Entree("a");
		s = new Symbole("entier", 0);
	}

	@Test
	public void testGetInstance() {
		assertTrue("Les deux instances devraient être égales",tds.equals(TDS.getInstance()));
	}

	@Test
	public void testAjouter() throws DoubleDeclaration {
		tds.ajouter(e, s);
		assertTrue("L'entrée "+e+" devrait être dans la liste",tds.getListe().containsKey(e));
	}

	@Test
	public void testAjouter2Var() throws DoubleDeclaration {
		Entree e2 = new Entree("b");
		Symbole s2 = new Symbole("entier", -4);
		
		tds.ajouter(e, s);
		tds.ajouter(e2, s2);

		assertTrue("L'entrée "+e+" devrait être dans la liste",tds.getListe().containsKey(e));
		assertTrue("L'entrée "+e2+" devrait être dans la liste",tds.getListe().containsKey(e2));
	}

	@Test(expected = DoubleDeclaration.class) 
	public void testAjouterPb() throws DoubleDeclaration {
		
		tds.ajouter(e, s);
		tds.ajouter(e, s);
	
	}

}
