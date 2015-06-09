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
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;
import edu.uci.ics.luci.TerraTower.events.TTEventDropBomb;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerDropBombTest {
	
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
		TTEventHandlerDropBomb tt = new TTEventHandlerDropBomb();

		/* Super type catches lack of globals error*/
		TTEvent tx = new TTEvent(worldName,worldPassword);
		JSONObject json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));

		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);
		
		/* Create a player */
		TTEventHandlerCreatePlayer tt2 = new TTEventHandlerCreatePlayer();
		TTEventCreatePlayer event2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt2.checkParameters(0,event2);
		assertTrue(json == null);
		json = tt2.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* Wrong type*/
		tx = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		json = tt.checkParameters(0,tx);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/*Territory doesn't exist*/
		TTEventDropBomb event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		WorldManager wm = g.getWorld(worldName, worldPassword);
		wm.setTerritory(new Territory(-180.0, 180.0, 10, -90.0, 90.0, 10));
		
		/*Too little time */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		tt.player.setLastBombPlacedTime(0);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*Out of bounds x */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,-190.0,alt);
		tt.player.setLastBombPlacedTime(-1 - g.getBombDelay());
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*Out of bounds x */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,-100.0,lng,alt);
		tt.player.setLastBombPlacedTime(-1 - g.getBombDelay());
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*Ok */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		tt.player.setLastBombPlacedTime(-1 - g.getBombDelay());
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		/* Add the bomb*/
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* ok to add another bomb */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		tt.player.setLastBombPlacedTime(-1 - g.getBombDelay());
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
	}
	
	@Test
	public void testOnEvent() {
		TTEventHandlerDropBomb tt = new TTEventHandlerDropBomb();

		GlobalsTerraTower g = new GlobalsTerraTower("Version test");
		Globals.setGlobals(g);
		g.createWorld(worldName, worldPassword);
		
		/* Create a player */
		TTEventHandlerCreatePlayer tt2 = new TTEventHandlerCreatePlayer();
		TTEventCreatePlayer event2 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		JSONObject json = tt2.checkParameters(0,event2);
		assertTrue(json == null);
		json = tt2.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		WorldManager wm = g.getWorld(worldName, worldPassword);
		wm.setTerritory(new Territory(-180.0, 180.0, 10, -90.0, 90.0, 10));
		
		/* Fail so we can get the player set so we can set the bomb time in the next block */
		TTEventDropBomb event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,-100.0,lng,alt);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		
		/*Ok */
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		tt.player.setLastBombPlacedTime(-1 - g.getBombDelay());
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		/* Add the bomb*/
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		/* Add the bomb again, should fail because parameters not checked*/
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
		/*Not Ok */
		/* add a second bomb fails because it's too fast*/
		event = new TTEventDropBomb(worldName,worldPassword,playerName,playerPassword,lat,lng,alt);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
		
	}

}
