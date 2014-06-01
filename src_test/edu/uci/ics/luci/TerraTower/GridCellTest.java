package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
	}

}
