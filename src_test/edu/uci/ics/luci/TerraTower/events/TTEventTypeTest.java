package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.events.TTEventType;


public class TTEventTypeTest {

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
	public void testEnum() {
		
		for(TTEventType t : TTEventType.values()){
			assertEquals(t, TTEventType.fromString(TTEventType.toString(t)));
		}
	}
	

}
