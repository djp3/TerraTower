package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TTEventCreatePlayerTest {
	
	static final String worldName = "wname";
	static final String worldPassword = "wpassword";
	static final String playerName = "player name";
	static final String playerPassword = "password";

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
		TTEventCreatePlayer t1 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreatePlayer t2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		TTEventCreatePlayer t3 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreatePlayer(worldName,worldPassword,playerName+"x",playerPassword);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreatePlayer(worldName,worldPassword,null,playerPassword);
		t3 = new TTEventCreatePlayer(worldName,worldPassword,null,playerPassword);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword+"x");
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,(String)null);
		t3 = new TTEventCreatePlayer(worldName,worldPassword,playerName,(String)null);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());

	}

	@Test
	public void testBasic() {
		new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
	}
	
	@Test
	public void testJSON() {
		TTEventCreatePlayer t1 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		
		assertEquals(t1,TTEventCreatePlayer.fromJSON(t1.toJSON()));
	}

}
