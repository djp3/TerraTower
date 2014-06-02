package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class GlobalsTerraTowerTest {

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
	public void test() {
		String version = ""+System.currentTimeMillis();
		
		GlobalsTerraTower g = new GlobalsTerraTower(version);
		assertEquals(g.getSystemVersion(),version);
		Globals.setGlobals(g);
		assertEquals(g, GlobalsTerraTower.getGlobalsTerraTower());
		
		String worldName = "earth";
		String worldPassword = "earthpassword";
		assertTrue(g.createWorld(worldName, worldPassword));
		assertTrue(!g.createWorld(worldName, worldPassword));
		assertTrue(g.clearWorld(worldName+"x", worldPassword));
		assertTrue(g.clearWorld(worldName+"x", PasswordUtils.hashPassword(worldPassword)));
		assertTrue(!g.clearWorld(worldName, worldPassword+"x"));
		assertTrue(!g.clearWorld(worldName, PasswordUtils.hashPassword(worldPassword+"x")));
		
		assertTrue(g.clearWorld(worldName, worldPassword));
		assertTrue(g.createWorld(worldName, PasswordUtils.hashPassword(worldPassword)));
		assertTrue(!g.createWorld(worldName, PasswordUtils.hashPassword(worldPassword)));
		
		assertTrue(g.getWorld(worldName+"x", worldPassword) == null);
		assertTrue(g.getWorld(worldName, worldPassword+"x") == null);
		
		assertTrue(g.getWorld(worldName+"x", PasswordUtils.hashPassword(worldPassword)) == null);
		assertTrue(g.getWorld(worldName, PasswordUtils.hashPassword(worldPassword+"x")) == null);
		
		WorldManager wm = new WorldManager(worldPassword);
		try{
			g.setWorld(worldName, wm);
			fail("this should throw an exception because it exists already");
		}
		catch(IllegalArgumentException e){
			//expected
		}
		catch(RuntimeException e){
			fail(""+e);
		}
		
		assertTrue(g.clearWorld(worldName,worldPassword));
		g.setWorld(worldName, wm);
		
		WorldManager b = g.getWorld(worldName,worldPassword);
		assertEquals(wm, b);
		
		b = g.getWorld(worldName,PasswordUtils.hashPassword(worldPassword));
		assertEquals(wm, b);
		
		assertTrue(g.clearWorld(worldName, PasswordUtils.hashPassword(worldPassword)));
	}

}
