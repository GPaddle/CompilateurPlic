package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import exception.ErreurDoubleDeclaration;
import repint.Acces;
import repint.AccesTableau;
import repint.Entree;
import repint.Symbole;
import repint.SymboleEntier;
import repint.SymboleTableau;
import repint.TDS;

public class TDSTest {

	public TDS tds;
	public Entree e1,e2;
	public Symbole s1,s2;

	@Before
	public void before() {
		tds = TDS.getInstance();
		e1 = new Entree("a");
		e2 = new Entree("tab");
		s1 = new SymboleEntier(0);
		s2 = new SymboleTableau(4,5);
	}

	@After
	public void after() {
		TDS.supress();
		e1 = null;
		e2 = null;
		s1 = null;
		s2 = null;
	}

	@Test
	public void testGetInstance() {
		assertTrue("Les deux instances devraient être égales", tds.equals(TDS.getInstance()));
	}

	@Test
	public void testAjouter() throws ErreurDoubleDeclaration, Exception {
		assertTrue("La liste devrait être vide", tds.getListeSize() == 0);
		tds.ajouter(e1, s1);
		assertTrue("La liste devrait contenir un élément", tds.getListeSize() == 1);
		assertTrue("L'entrée " + e1 + " devrait être dans la liste", tds.identifier(e1) != null);
	}
	
	@Test
	public void testAjouterDifférentsTypes() throws ErreurDoubleDeclaration, Exception {
		assertTrue("La liste devrait être vide", tds.getListeSize() == 0);
		tds.ajouter(e1, s1);
		assertTrue("La liste devrait contenir un élément", tds.getListeSize() == 1);
		tds.ajouter(e2, s2);
		assertTrue("La liste devrait contenir deux élément", tds.getListeSize() == 2);
		
		assertTrue("L'entrée " + e1 + " devrait être dans la liste", tds.identifier(e1) != null);
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}
	
	@Test
	public void testAjouterTableau() throws ErreurDoubleDeclaration, Exception {
		assertTrue("La liste devrait être vide", tds.getListeSize() == 0);
		tds.ajouter(e2, s2);
		assertTrue("La liste devrait contenir un élément", tds.getListeSize() == 1);
		
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}
	
	@Test
	public void testAdresseTableau() throws ErreurDoubleDeclaration, Exception {
		tds.ajouter(e2, s2);
		
		Acces a = new AccesTableau("tab", 2);
		
		assertTrue("L'acces devrait pointer sur -8", a.getAdresse().equals("-8"));
		
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}
	
	@Test
	public void testAccesSymbole() throws ErreurDoubleDeclaration, Exception {
		tds.ajouter(e2, s2);
		
		Acces a = new AccesTableau("tab", 2);
		
		assertTrue("tab devrait exister", TDS.getInstance().identifier(new Entree(""+a.getI()))!=null);
		
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}

	@Test
	public void testAjouter2Var() throws ErreurDoubleDeclaration, Exception {
		Entree e2 = new Entree("b");
		Symbole s2 = new SymboleEntier(-4);

		tds.ajouter(e1, s1);
		tds.ajouter(e2, s2);

		assertTrue("L'entrée " + e1 + " devrait être dans la liste", tds.identifier(e1) != null);
		assertTrue("L'entrée " + e2 + " devrait être dans la liste", tds.identifier(e2) != null);
	}

	@Test(expected = ErreurDoubleDeclaration.class)
	public void testAjouterPb() throws ErreurDoubleDeclaration, Exception {

		tds.ajouter(e1, s1);
		tds.ajouter(e1, s1);

	}

}
