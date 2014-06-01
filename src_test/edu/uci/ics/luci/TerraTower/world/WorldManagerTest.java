package edu.uci.ics.luci.TerraTower.world;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class WorldManagerTest {

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
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager();
		
		assertTrue(!wm.worldExists(name));
		assertTrue(!wm.worldExists(name));
		
		assertTrue(wm.create(name,password));
		assertTrue(wm.worldExists(name));
		assertTrue(!wm.create(name,password));
		
		assertTrue(wm.passwordGood(name, password));
		assertTrue(!wm.passwordGood(name+"x", password));
		
		assertTrue(!wm.addMap(name+"x", password, new Map(-1,1,10,-1,1,10)));
		assertTrue(!wm.addMap(name, password+"x", new Map(-1,1,10,-1,1,10)));
		assertTrue(wm.addMap(name, password, new Map(-1,1,10,-1,1,10)));
		assertTrue(!wm.addMap(name, password, new Map(-1,1,10,-1,1,10)));
		
		assertTrue(wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password)));
		assertTrue(wm.playerExists("player"+name));
		
		assertTrue(!wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password)));
	}

}
