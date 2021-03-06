/*
	Copyright 2014-2015
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
package edu.uci.ics.luci.TerraTower.events.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateTerritory;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreateTerritoryTest {
	
	final static String TEST_VERSION ="-999";
	
	static final String worldName = "name"+System.currentTimeMillis();
	static final String worldPassword = "password"+System.currentTimeMillis();
	static final String playerName = "name"+System.currentTimeMillis();
	static final String playerPassword = "password"+System.currentTimeMillis();
	
	double left = -180.0;
	double right = 180.0;
	int numXSplits = 10;
	double bottom = -90.0;
	double top = 90.0;
	int numYSplits = 10;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Globals.setGlobals(null);
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
	public void testParameters() {
		TTEventHandlerCreateTerritory tt = new TTEventHandlerCreateTerritory();
		

		/* Super type catches lack of globals error*/
		TTEvent tx = new TTEvent(worldName,worldPassword);
		JSONObject json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Wrong type*/
		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);
		
		tx = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		
		/*Left and right backwards*/
		TTEventCreateTerritory event = new TTEventCreateTerritory(worldName,worldPassword,right,left,numXSplits,bottom,top,numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*NumXSplits bad */
		event = new TTEventCreateTerritory(worldName,worldPassword,left,right,-numXSplits,bottom,top,numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*top and bottom mixed up*/
		event = new TTEventCreateTerritory(worldName,worldPassword,left,right,numXSplits,top,bottom,numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*NumYSplits bad */
		event = new TTEventCreateTerritory(worldName,worldPassword,left,right,numXSplits,bottom,top,-numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*Ok! */
		event = new TTEventCreateTerritory(worldName,worldPassword,left,right,numXSplits,bottom,top,numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower globals = new GlobalsTerraTower(TEST_VERSION,true);
		GlobalsTerraTower.setGlobals(globals);
		
		TTEventHandlerCreateWorld ttWorld = new TTEventHandlerCreateWorld();
		TTEventCreateWorld eventWorld = new TTEventCreateWorld(worldName,worldPassword);
		JSONObject json = ttWorld.checkParameters(0, eventWorld);
		assertTrue(json == null);
		ttWorld.onEvent();
		
		TTEventHandlerCreateTerritory tt = new TTEventHandlerCreateTerritory();

		/*Ok! */
		TTEventCreateTerritory event = new TTEventCreateTerritory(worldName,worldPassword,left,right,numXSplits,bottom,top,numYSplits);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		//set up the world
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
	}

}
