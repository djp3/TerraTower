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
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.events.TTEventVoid;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreateWorldTest {
	final static String worldName = "name";
	final static String worldPassword = "p"+System.currentTimeMillis();

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
		TTEventHandlerCreateWorld tt = new TTEventHandlerCreateWorld();
		
		/* Wrong type*/
		TTEventVoid tx = new TTEventVoid();
		JSONObject json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Null world name*/
		TTEventCreateWorld event = new TTEventCreateWorld(null,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
		/* Null globals */
		event = new TTEventCreateWorld(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		
		/* Ok */
		event = new TTEventCreateWorld(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		g.createWorld(worldName, worldPassword);
		
		/*World Exists */
		event = new TTEventCreateWorld(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
	}
	
	@Test
	public void testOnEvent() {
		TTEventHandlerCreateWorld tt = new TTEventHandlerCreateWorld();

		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		
		TTEventCreateWorld event = new TTEventCreateWorld(worldName,worldPassword);
		JSONObject json = tt.checkParameters(0,event);
		assertTrue(json == null);

		json = tt.onEvent();
		assertTrue(json != null);
		try{
			assertEquals("false",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.err.println(json.toJSONString());
			throw e;
		}
		
		/* World already exists */
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
	}

}
