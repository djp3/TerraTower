package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.gameEvents.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreateWorldTest {

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
		TTEventHandlerCreateWorld tt = new TTEventHandlerCreateWorld();
		String name = "name";
		String password = "password";
		TTEventCreateWorld event = new TTEventCreateWorld(name,password);
		
		JSONObject json = tt.checkParameters(event);
		assertTrue(json == null);
		
		event = new TTEventCreateWorld(null,password);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		event = new TTEventCreateWorld(name,null);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.setWorldManager(new WorldManager());
		
		TTEventHandlerCreateWorld tt = new TTEventHandlerCreateWorld();
		String name = "name";
		String password = "password";
		TTEventCreateWorld event = new TTEventCreateWorld(name,password);
		
		JSONObject json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		
	}

}
