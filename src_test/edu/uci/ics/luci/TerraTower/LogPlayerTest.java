/*
	Copyright 2014
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java TerraTower game, i.e. "TerraTower"

    TerraTower is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		t.setDaemon(false); //Force clean shutdown
		t.start();
		while(t.isAlive()){
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}
		for(EventHandlerResultChecker r: lp.getResults()){
			Object semaphore = r.getSemaphore();
			synchronized(semaphore){
				while(r.getResults() == null){
					try {
						semaphore.wait();
					} catch (InterruptedException e) {
					}
				}
				try{
					assertTrue(r.getResults().get("error").equals("false"));
				}
				catch(AssertionError e){
					System.err.println("Problem replaying log: \n"+r.getResults());
				}
			}
		}
		long numEventsFired = lp.getNumberEventsFired();
		assertEquals(numEvents,numEventsFired);
		assertEquals(0,lp.getNumberEventsRemaining());
		
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
