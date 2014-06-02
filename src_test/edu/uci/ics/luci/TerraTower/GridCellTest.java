package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.gameElements.Tower;

public class GridCellTest {

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
		int x = 10;
		int y = 10;
		GridCell gc = new GridCell(x,y);
		assertEquals(x,gc.getX());
		assertEquals(y,gc.getY());
		
		assertTrue(!gc.towerPresent());
		gc.addTower(new Tower(null, x, y));
		assertTrue(gc.towerPresent());
		assertTrue(gc.getTower().getOwner()==null);
		assertTrue(gc.getTower().getX()==x);
		assertTrue(gc.getTower().getY()==y);
	}
	
	
	@Test
	public void testAlts() {
		int x = 10;
		int y = 10;
		GridCell gc = new GridCell(x,y);
		assertTrue(gc.getAlts().size() == 0);
		gc.updateAltitude(10.0);
		gc.updateAltitude(15.0);
		gc.updateAltitude(20.0);
		assertTrue(gc.getAlts().size() == 3);
		assertTrue(gc.estimateAltitude() == 15.0);
	}


}
