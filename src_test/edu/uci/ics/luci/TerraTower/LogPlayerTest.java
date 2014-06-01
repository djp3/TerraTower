package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.LogPlayer.MyResultListener;
import edu.uci.ics.luci.utility.Globals;

public class LogPlayerTest {

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

	public void test(boolean realTime) throws IOException {
		GlobalsTerraTower gtt = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(gtt);
		TTEventWrapperQueuer q = TerraTower.createEventQueue(null);
		gtt.addQuittable(q);
		
		LogPlayer lp = new LogPlayer("test/LogPlayerTest.log",q,realTime);
		long numEvents = lp.getNumberEventsRemaining();
		Thread t = new Thread(lp);
		t.start();
		while(t.isAlive()){
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}
		long numEventsFired = lp.getNumberEventsFired();
		assertEquals(numEvents,numEventsFired);
		assertEquals(0,lp.getNumberEventsRemaining());
		for(MyResultListener r: lp.getResults()){
			while(r.getResultOK() == null){};
			try{
				assertTrue(r.getResultOK());
			}
			catch(AssertionError e){
				System.err.println("Problem replaying log: \n"+r.getErrors());
			}
			
		}
		
		gtt.setQuitting(true);
	}
	
	@Test
	public void testFast() throws IOException{
		test(false);
	}
	
	@Test
	public void testRealTime() throws IOException{
		test(true);
	}

}
