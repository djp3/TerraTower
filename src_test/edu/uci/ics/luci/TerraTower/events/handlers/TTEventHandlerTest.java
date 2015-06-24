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
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerTest {

	static final String worldName = "name";
	static final String worldPassword = "password";
	
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
		TTEventHandler tt = new TTEventHandler();
		
		TTEvent event = new TTEvent(null,worldPassword);
		
		JSONObject json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		event = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		
		event = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
		g.createWorld(worldName, worldPassword);
		
		event = new TTEvent(worldName,worldPassword+"x");
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
		event = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		
		TTEventHandler tt = new TTEventHandler();
		
		g.createWorld(worldName, worldPassword);
		
		TTEvent event = new TTEvent(worldName,worldPassword);
		JSONObject json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
	}
}
