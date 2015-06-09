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
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePowerUp;
import edu.uci.ics.luci.TerraTower.events.TTEventRedeemPowerUp;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerRedeemPowerUpTest {
	
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
		TTEventHandlerRedeemPowerUp tt = new TTEventHandlerRedeemPowerUp();

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
		
		/*Code doesn't exist*/
		TTEventRedeemPowerUp event = new TTEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,"code");
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/*Power Up is null */
		event = new TTEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,null);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		/* Create a good up power Up */
		TTEventHandlerCreatePowerUp tt3 = new TTEventHandlerCreatePowerUp();
		
		TTEventCreatePowerUp event3 = new TTEventCreatePowerUp(worldName,worldPassword,"code2",-1000L,-100L,-1000L);
		json = tt3.checkParameters(0,event3);
		try{
			assertTrue(json == null);
		}
		catch(AssertionError e){
			System.out.println(json.toJSONString());
			throw e;
		}
		
		json = tt3.onEvent();
		assertTrue(json != null);
		try{
			assertEquals("false",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.err.println(json.toJSONString());
			throw e;
		}
		
		/*OK!*/
		event = new TTEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,"code2");
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
	}
	
	@Test
	public void testOnEvent() {
		TTEventHandlerRedeemPowerUp tt = new TTEventHandlerRedeemPowerUp();

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
		
		/* Create 2 good up power Ups */
		TTEventHandlerCreatePowerUp tt3 = new TTEventHandlerCreatePowerUp();
		
		TTEventCreatePowerUp event3 = new TTEventCreatePowerUp(worldName,worldPassword,"code2",-1000L,-1000L,-1000L);
		json = tt3.checkParameters(0,event3);
		try{
			assertTrue(json == null);
		}
		catch(AssertionError e){
			System.out.println(json.toJSONString());
			throw e;
		}
		
		json = tt3.onEvent();
		assertTrue(json != null);
		try{
			assertEquals("false",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.err.println(json.toJSONString());
			throw e;
		}
		
		event3 = new TTEventCreatePowerUp(worldName,worldPassword,"code3",-g.getTowerDelay(),
				-g.getBombDelay(),
				-g.getBombFuse());
		json = tt3.checkParameters(0,event3);
		try{
			assertTrue(json == null);
		}
		catch(AssertionError e){
			System.out.println(json.toJSONString());
			throw e;
		}
		
		json = tt3.onEvent();
		assertTrue(json != null);
		try{
			assertEquals("false",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.err.println(json.toJSONString());
			throw e;
		}
		
		/*OK!*/
		TTEventRedeemPowerUp event = new TTEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,"code2");
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		long bombDelay = tt.player.getBombDelay();
		long bombFuse = tt.player.getBombFuse();
		long towerDelay = tt.player.getTowerDelay();
		
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		assertTrue(bombDelay - tt.player.getBombDelay() == 1000L);
		assertTrue(bombFuse - tt.player.getBombFuse() == 1000L);
		assertTrue(towerDelay - tt.player.getTowerDelay() == 1000L);
		
		/*OK!*/
		event = new TTEventRedeemPowerUp(worldName,worldPassword,playerName,playerPassword,"code3");
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
		
		json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("false",(String)json.get("error"));
		
		assertTrue(tt.player.getBombDelay() == 0L);
		assertTrue(tt.player.getBombFuse() == 0L);
		assertTrue(tt.player.getTowerDelay() == 0L);
		
	}

}
