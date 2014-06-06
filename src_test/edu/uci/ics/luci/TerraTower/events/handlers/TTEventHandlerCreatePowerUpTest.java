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
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;
import edu.uci.ics.luci.utility.Globals;

public class TTEventHandlerCreatePowerUpTest {
	
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
		TTEventHandlerCreatePowerUp tt = new TTEventHandlerCreatePowerUp();
		

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
		
		
		/*Null Power Up*/
		TTEventCreatePowerUp event = new TTEventCreatePowerUp(worldName,worldPassword,null);
		json = tt.checkParameters(0,event);
		assertTrue(json != null);
		assertEquals(json.get("error"),"true");
		
		
		/*Ok! */
		PowerUp pup = new PowerUp("code",-1000L,-100L,-1000L,false);
		event = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		json = tt.checkParameters(0,event);
		assertTrue(json == null);
	}
	
	
	
	@Test
	public void testOnEvent() {
		TTEventHandlerCreatePowerUp tt = new TTEventHandlerCreatePowerUp();
		
		/* Fail for not checking parameters */
		JSONObject json = tt.onEvent();
		assertTrue(json != null);
		assertEquals("true",(String)json.get("error"));
	
		/*Ok! */
		PowerUp pup = new PowerUp("code",-1000L,-100L,-1000L,false);
		TTEventCreatePowerUp event = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		json = tt.checkParameters(0,event);
		try{
			assertTrue(json == null);
		}
		catch(AssertionError e){
			System.out.println(json.toJSONString());
			throw e;
		}
		
		json = tt.onEvent();
		assertTrue(json != null);
		try{
			assertEquals("false",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.err.println(json.toJSONString());
			throw e;
		}
		
		/*Not Ok! pup exists!*/
		pup = new PowerUp("code",-1000L,-100L,-1000L,false);
		event = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		json = tt.checkParameters(0,event);
		try{
			assertTrue(json != null);
			assertEquals("true",(String)json.get("error"));
		}
		catch(AssertionError e){
			System.out.println(json.toJSONString());
			throw e;
		}
		
	}

}