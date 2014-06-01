package edu.uci.ics.luci.TerraTower.gameEvents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreatePlayerTest {

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
	public void testParameters() {
		TTEventHandlerCreatePlayer tt = new TTEventHandlerCreatePlayer();
		String playerName = "player name";
		String password = "password";
		TTEventCreatePlayer event = new TTEventCreatePlayer(playerName,password);
		
		JSONObject json = tt.checkParameters(event);
		assertTrue(json == null);
		
		event = new TTEventCreatePlayer(null,password);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		event = new TTEventCreatePlayer(playerName,(String)null);
		json = tt.checkParameters(event);
		assertTrue(json == null);
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.setWorldManager(new WorldManager());
		
		TTEventHandlerCreatePlayer tt = new TTEventHandlerCreatePlayer();
		String playerName = "player name";
		String password = "password";
		TTEventCreatePlayer event = new TTEventCreatePlayer(playerName,password);
		
		JSONObject json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		
	}

}
