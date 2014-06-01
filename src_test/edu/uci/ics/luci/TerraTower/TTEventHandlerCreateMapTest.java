package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreateMapTest {

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
		TTEventHandlerCreateMap tt = new TTEventHandlerCreateMap();
		String name = "name";
		String password = "password";
		double left = -180.0;
		double right = 180.0;
		int numXSplits = 10;
		double bottom = -90.0;
		double top = 90.0;
		int numYSplits = 10;
		TTEventCreateMap event = new TTEventCreateMap(name,password,left,right,numXSplits,bottom,top,numYSplits);
		
		JSONObject json = tt.checkParameters(event);
		assertTrue(json == null);
		
		event.setWorldName(null);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		event.setWorldName(name);
		event.setPassword(null);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		event.setPassword(password);
		event.setLeft(right);
		event.setRight(left);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		event.setLeft(left);
		event.setRight(right);
		event.setTop(bottom);
		event.setBottom(top);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		event.setTop(top);
		event.setBottom(bottom);
		event.setNumXSplits(-10);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		event.setNumXSplits(numXSplits);
		event.setNumYSplits(-10);
		json = tt.checkParameters(event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.setWorldManager(new WorldManager());
		
		//Set up the world
		TTEventHandlerCreateWorld tw = new TTEventHandlerCreateWorld();
		String name = "name";
		String password = "password";
		TTEventCreateWorld wevent = new TTEventCreateWorld(name,password);
	
		
		//Test the map
		TTEventHandlerCreateMap tt = new TTEventHandlerCreateMap();
		double left = -180.0;
		double right = 180.0;
		int numXSplits = 10;
		double bottom = -90.0;
		double top = 90.0;
		int numYSplits = 10;
		TTEventCreateMap event = new TTEventCreateMap(name,password,left,right,numXSplits,bottom,top,numYSplits);
		
		//Try to make a map without a world
		JSONObject json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		//set up the world
		json = tw.onEvent(wevent);
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		//Try to make a map with a bad password
		event.setPassword(password+"x");
		json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		//Try to make a map
		event.setPassword(password);
		json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		//Try to make a second map
		json = tt.onEvent(event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
	}

}
