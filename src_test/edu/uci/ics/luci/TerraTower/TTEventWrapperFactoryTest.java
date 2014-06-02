package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.events.TTEventType;

public class TTEventWrapperFactoryTest {

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
		TTEventWrapperFactory x = new TTEventWrapperFactory();
		TTEventWrapper a = x.newInstance();
		TTEventWrapper b = x.newInstance();
		assertEquals(a.getEventType(),x.defaultEventType);
		assertEquals(b.getEventType(),x.defaultEventType);
		assertEquals(a,b);
		
		TTEventWrapperFactory y = new TTEventWrapperFactory(TTEventType.CREATE_WORLD);
		TTEventWrapper c = y.newInstance();
		TTEventWrapper d = y.newInstance();
		assertEquals(c.getEventType(),y.defaultEventType);
		assertEquals(d.getEventType(),y.defaultEventType);
		assertEquals(a,b);
		
		assertTrue(a!=c);
		assertTrue(b!=d);
	}

}
