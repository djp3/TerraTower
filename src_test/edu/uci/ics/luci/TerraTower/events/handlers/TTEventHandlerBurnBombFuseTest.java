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
import edu.uci.ics.luci.TerraTower.events.TTEventBurnBombFuse;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerBurnBombFuseTest {
	
	static final String worldName = "name"+System.currentTimeMillis();
	static final String worldPassword = "password"+System.currentTimeMillis();
	static final String playerName = "name"+System.currentTimeMillis();
	static final String playerPassword = "password"+System.currentTimeMillis();
	
	double lat = 0.0;
	double lng = 0.0;
	double alt = 10.0;
	
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
		TTEventHandlerBurnBombFuse tt = new TTEventHandlerBurnBombFuse();
		
		/* Super type catches null password error*/
		TTEvent tx = new TTEvent(worldName,(String)null);
		JSONObject json = tt.checkParameters(0,tx);
		assertEquals("true",(String)json.get("error"));

		/* Super type catches lack of globals error*/
		tx = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,tx);
		assertEquals("true",(String)json.get("error"));

		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);
		
		/* Now globals are ok, but type is wrong*/
		tx = new TTEvent(worldName,worldPassword);
		json = tt.checkParameters(0,tx);
		System.err.println(json.toJSONString());
		assertEquals("true",(String)json.get("error"));
		
		/*Ok*/
		tx = new TTEventBurnBombFuse(worldName,worldPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json == null);
		
	}
	
	@Test
	public void testOnEvent() {
		TTEventHandlerBurnBombFuse tt = new TTEventHandlerBurnBombFuse();
		

		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);
		WorldManager wm = g.getWorld(worldName, worldPassword);
		wm.setTerritory(new Territory(-180,180,10,-90,90,10));
		
		/* Fail for not checking parameters */
		JSONObject json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/*Check parameters*/
		TTEventBurnBombFuse tx = new TTEventBurnBombFuse(worldName,worldPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json == null);
		
		/* Ok */
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
	}

}
