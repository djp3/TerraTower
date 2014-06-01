package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TTEventCreateWorldTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testEquals() {
		String worldName = "name";
		String password = "password";
		TTEventCreateWorld t1 = new TTEventCreateWorld(worldName,password);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreateWorld t2 = new TTEventCreateWorld(worldName,password);
		TTEventCreateWorld t3 = new TTEventCreateWorld(worldName,password);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreateWorld(worldName+"x",password);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateWorld(null,password);
		t3 = new TTEventCreateWorld(null,password);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateWorld(worldName,password+"x");
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateWorld(worldName,null);
		t3 = new TTEventCreateWorld(worldName,null);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());

	}

	@Test
	public void testBasic() {
		String worldName = "name";
		String password = "password";
		new TTEventCreateWorld(worldName,password);
	}
	
	@Test
	public void testJSON() {
		String worldName = "name";
		String password = "password";
		TTEventCreateWorld t1 = new TTEventCreateWorld(worldName,password);
		
		assertEquals(t1,TTEventCreateWorld.fromJSON(t1.toJSON()));
	}

}
