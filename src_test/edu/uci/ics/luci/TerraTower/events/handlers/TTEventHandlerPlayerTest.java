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
import edu.uci.ics.luci.TerraTower.events.TTEventPlayer;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerPlayerTest {

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
		TTEventHandlerPlayer tt = new TTEventHandlerPlayer();
		
		TTEventPlayer event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword);
		
		/* Fail in the super class */
		JSONObject json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);

		
		/* Fail for the wrong type */
		json = tt.checkParameters(0,new TTEvent(worldName,worldPassword));
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Fail for bad player name */
		event = new TTEventPlayer(worldName,worldPassword,null,playerPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Fail because player doesn't exist*/
		event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Create the player */
		TTEventHandlerCreatePlayer tt2 = new TTEventHandlerCreatePlayer();
		TTEventCreatePlayer event2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt2.checkParameters(0,event2);
		assertTrue(json == null);
		json = tt2.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* Fail because player password is wrong*/
		event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword+"x");
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/* Ok! */
		event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);

		
	}
	
	@Test
	public void testOnEvent() {
		TTEventHandlerPlayer tt = new TTEventHandlerPlayer();
		TTEventPlayer event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword);
		
		GlobalsTerraTower g = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);

		
		/* Create the player */
		TTEventHandlerCreatePlayer tt2 = new TTEventHandlerCreatePlayer();
		TTEventCreatePlayer event2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		JSONObject json = tt2.checkParameters(0,event2);
		assertTrue(json == null);
		json = tt2.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* Ok! */
		event = new TTEventPlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
	}
}
