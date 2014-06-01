package edu.uci.ics.luci.TerraTower.world;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		
		assertTrue(!WorldManager.worldExists(name));
		assertTrue(!WorldManager.worldExists(name));
		
		assertTrue(WorldManager.create(name,password));
		assertTrue(WorldManager.worldExists(name));
		assertTrue(!WorldManager.create(name,password));
	}

}
