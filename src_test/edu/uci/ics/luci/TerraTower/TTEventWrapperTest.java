package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TTEventWrapperTest {

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
	public void testBasics() {
		
		TTEventWrapper t1;
		try{
			t1 = new TTEventWrapper(TTEventType.VOID,null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}
		
		try{
			t1 = new TTEventWrapper(null,null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}
		
		TTEventCreateWorld ttEvent1 = new TTEventCreateWorld("name","password");
		t1 = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent1);
		
		assertEquals(t1.getEventType(), TTEventType.CREATE_WORLD);
		assertEquals(t1.getEvent(), ttEvent1);
		
		TTEventCreateWorld ttEvent2 = new TTEventCreateWorld("name","password2");
		TTEventWrapper t2 = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent2);
		
		assertEquals(t2.getEventType(), TTEventType.CREATE_WORLD);
		assertEquals(t2.getEvent(), ttEvent2);
		
		assertTrue(!t1.equals(t2));
		
		t1.set(t2);
		assertEquals(t1,t1);
		assertEquals(t1,t2);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("foo"));
		
		assertEquals(t1,TTEventWrapper.fromJSON(t1.toJSON()));
		
		
	}

}
