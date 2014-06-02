package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.TerraTower.events.TTEventVoid;



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

	private class MyTTEventHandlerResultListener implements TTEventHandlerResultListener{

		@Override
		public void onFinish(JSONObject result) {
		}
		
	}

	@Test
	public void testBasics() {
		
		TTEventWrapper t1;
		try{
			//t1 = new TTEventWrapper(TTEventType.VOID,new TTEventVoid(),(TTEventHandlerResultListener) null);
			t1 = new TTEventWrapper(null,new TTEventVoid(),(TTEventHandlerResultListener) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception"+e);
		}
		
		try{
			//t1 = new TTEventWrapper(TTEventType.VOID,new TTEventVoid(),(TTEventHandlerResultListener) null);
			t1 = new TTEventWrapper(null,new TTEventVoid(),(List<TTEventHandlerResultListener>) null);
			fail("Should throw an exception");
		}catch(IllegalArgumentException e){
			//ok
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			t1 = new TTEventWrapper(TTEventType.VOID,null,(TTEventHandlerResultListener) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		try{
			t1 = new TTEventWrapper(TTEventType.VOID,null,(List<TTEventHandlerResultListener>) null);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
		
		TTEventCreateWorld ttEvent1 = new TTEventCreateWorld("name","password");
		TTEventHandlerResultListener rl = new MyTTEventHandlerResultListener();
		List<TTEventHandlerResultListener> list = new ArrayList<TTEventHandlerResultListener>();
		list.add(rl);
		try{
			t1 = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent1,rl);
			t1 = new TTEventWrapper(TTEventType.CREATE_TERRITORY,ttEvent1,list);
		}catch(RuntimeException e){
			fail("Should not throw an exception");
		}
	}

	@Test
	public void testResetEventHandler() {
		for(TTEventType tet : TTEventType.values()){
			TTEventWrapper t1 = new TTEventWrapper(tet,new TTEventVoid(),new ResultChecker(false));
			TTEventWrapper t2 = new TTEventWrapper(tet,new TTEventVoid(),new ResultChecker(false));
			t1.resetEvent();
			t1.resetEventHandler();
			try{
				assertTrue(t1.getHandler() != null);
				t2.set(t1);
				assertEquals(t1,t2);
				assertEquals(t1.hashCode(),t2.hashCode());
				assertEquals(t1,t1.fromJSON(t1.toJSON()));
			}
			catch(AssertionError e){
				System.err.println("Failed trying to handle: "+tet);
				throw e;
			}
		}
	}
	
	@Test
	public void testEquals() {
		TTEventWrapper t1 = new TTEventWrapper(TTEventType.VOID,new TTEventVoid(),new ResultChecker(false));
		TTEventWrapper t2 = new TTEventWrapper(TTEventType.VOID,new TTEventVoid(),new ResultChecker(false));
		assertEquals(t1,t1);
		assertTrue(t1.hashCode() == t2.hashCode());
		assertEquals(t1,t2);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("foo"));
		
		
		t2 = new TTEventWrapper(TTEventType.CREATE_WORLD,new TTEventCreateWorld("a","b"),new ResultChecker(false));
		t1.set(t2);
		assertEquals(t1,t1);
		assertTrue(t1.equals(t2));
		
		t2 = new TTEventWrapper(TTEventType.CREATE_WORLD,null,new ResultChecker(false));
		assertEquals(t2,t2);
		assertTrue(t1.hashCode() != t2.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		
		t2 = new TTEventWrapper(TTEventType.CREATE_WORLD,new TTEventCreateWorld("c","d"),new ResultChecker(false));
		assertEquals(t2,t2);
		assertTrue(t1.hashCode() != t2.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		
	}
	

}
