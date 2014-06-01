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
		
		WorldManager wm = new WorldManager();
		g.setWorldManager(wm);
		assertEquals(wm, g.getWorldManager());
	}

}
