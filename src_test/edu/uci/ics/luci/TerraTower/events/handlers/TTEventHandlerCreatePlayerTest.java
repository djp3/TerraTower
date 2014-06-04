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
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreatePlayerTest {
	
	static final String worldName = "name"+System.currentTimeMillis();
	static final String worldPassword = "password"+System.currentTimeMillis();
	static final String playerName = "name"+System.currentTimeMillis();
	static final String playerPassword = "password"+System.currentTimeMillis();

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
		TTEventHandlerCreatePlayer tt = new TTEventHandlerCreatePlayer();
		
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
		
		/* null player */
		//tx = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		tx = new TTEventCreatePlayer(worldName,worldPassword,null,playerPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Ok! */
		tx = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json == null);
		
		//Execute creation
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* Fail parameters because player exists */
		tx = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		//Fail to Execute creation because player exists
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
	}
	
	@Test
	public void testOnEvent() {
		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);

		/* Ok! */
		TTEventHandlerCreatePlayer tt = new TTEventHandlerCreatePlayer();
		TTEventCreatePlayer tx = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		JSONObject json = tt.checkParameters(0,tx);
		assertTrue(json == null);
		
		//Execute creation
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
				
		//Fail to Execute creation because player exists
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		
	}

}
