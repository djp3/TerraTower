package edu.uci.ics.luci.TerraTower.gameEvents;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.gameEvents.TTEventCreatePlayer;

public class TTEventCreatePlayerTest {

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
		String playerName = "player name";
		String password = "password";
		TTEventCreatePlayer t1 = new TTEventCreatePlayer(playerName,password);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreatePlayer t2 = new TTEventCreatePlayer(playerName,password);
		TTEventCreatePlayer t3 = new TTEventCreatePlayer(playerName,password);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreatePlayer(playerName+"x",password);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreatePlayer(null,password);
		t3 = new TTEventCreatePlayer(null,password);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreatePlayer(playerName,password+"x");
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreatePlayer(playerName,(String)null);
		t3 = new TTEventCreatePlayer(playerName,(String)null);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());

	}

	@Test
	public void testBasic() {
		String playerName = "player name";
		String password = "password";
		new TTEventCreatePlayer(playerName,password);
	}
	
	@Test
	public void testJSON() {
		String playerName = "player name";
		String password = "password";
		TTEventCreatePlayer t1 = new TTEventCreatePlayer(playerName,password);
		
		assertEquals(t1,TTEventCreatePlayer.fromJSON(t1.toJSON()));
	}

}
