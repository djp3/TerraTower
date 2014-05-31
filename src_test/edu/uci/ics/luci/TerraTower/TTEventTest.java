package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.TTEvent.TTEventType;

public class TTEventTest {

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
	
	@Test
	public void testBasics() {
		
		TTEvent t1 = new TTEvent(TTEventType.VOID);
		assertEquals(t1.getEventType(), TTEventType.VOID);
		
		TTEvent t2 = new TTEvent(TTEventType.CREATE_WORLD);
		assertEquals(t2.getEventType(), TTEventType.CREATE_WORLD);
		
		assertTrue(!t1.equals(t2));
		
		t1.set(t2);
		assertEquals(t1,t1);
		assertEquals(t1,t2);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("foo"));
		
		assertEquals(t1,TTEvent.fromJSON(t1.toJSON()));
		
		
	}

}
