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


package edu.uci.ics.luci.TerraTower.webhandlers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.PasswordUtils;
import edu.uci.ics.luci.TerraTower.ResultChecker;
import edu.uci.ics.luci.TerraTower.TTEventWrapper;
import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;
import edu.uci.ics.luci.TerraTower.TerraTower;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePowerUp;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateTerritory;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerShutdown;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.WebUtil;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;

public class WebHandlerTests {
	
	private static int testPort = 9020;
	private static synchronized int testPortPlusPlus(){
		int x = testPort;
		testPort++;
		return(x);
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		Globals.setGlobals(null);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	

	final static String TEST_VERSION ="-999";
	final static String worldName = "earth_"+System.currentTimeMillis();
	final static String worldPassword = "earthPassword_"+System.currentTimeMillis();
	final static String playerName = "Player_Name"+System.currentTimeMillis();
	final static String playerPassword = "Player_Password"+System.currentTimeMillis();

	private WebServer ws = null;

	HashMap<String,HandlerAbstract> requestHandlerRegistry;
	

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	


	private void startAWebServer(int port, TTEventWrapperQueuer eventPublisher) {
		try {
			requestHandlerRegistry = new HashMap<String, HandlerAbstract>();
			requestHandlerRegistry.put(null, new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			requestHandlerRegistry.put("", new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			requestHandlerRegistry.put("version", new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			requestHandlerRegistry.put("build_tower", new HandlerBuildTower(eventPublisher));
			requestHandlerRegistry.put("drop_bomb", new HandlerDropBomb(eventPublisher));
			requestHandlerRegistry.put("redeem_power_up", new HandlerRedeemPowerUp(eventPublisher));
			requestHandlerRegistry.put("get_leader_board", new HandlerGetLeaderBoard(eventPublisher));
			requestHandlerRegistry.put("get_game_state", new HandlerGetGameState());
			requestHandlerRegistry.put("shutdown", new HandlerShutdown(Globals.getGlobals()));
			
			RequestDispatcher requestDispatcher = new RequestDispatcher(requestHandlerRegistry);
			ws = new WebServer(requestDispatcher, port, false, new AccessControl());
			ws.start();
			Globals.getGlobals().addQuittable(ws);
		} catch (RuntimeException e) {
			fail("Couldn't start webserver"+e);
		}
	}
	
	public TTEventWrapperQueuer startATestSystem(){
		List<TTEventWrapper> events = new ArrayList<TTEventWrapper>();
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		GlobalsTerraTower globals = new GlobalsTerraTower(TEST_VERSION);
		GlobalsTerraTower.setGlobals(globals);
		globals.setTesting(true);
		
		TTEventWrapperQueuer eventPublisher = TerraTower.createEventQueue(logFileName);     
		globals.addQuittable(eventPublisher);
		
		
		TTEventCreateWorld ttEvent1 = new TTEventCreateWorld(worldName,worldPassword);
		ResultChecker resultChecker = new ResultChecker(false);
		TTEventWrapper event = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent1,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		TTEventCreateTerritory ttEvent2 = new TTEventCreateTerritory(worldName,worldPassword,-117.8442599,-117.8411751,100,33.6446338,33.6472217,100);
		resultChecker = new ResultChecker(false);
		event = new TTEventWrapper(TTEventType.CREATE_TERRITORY,ttEvent2,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		
		TTEventCreatePlayer ttEvent3 = new TTEventCreatePlayer(worldName,worldPassword,playerName,playerPassword);
		resultChecker = new ResultChecker(false);
		event = new TTEventWrapper(TTEventType.CREATE_PLAYER,ttEvent3,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		
		PowerUp pup = new PowerUp("code",-1000L,-1000L,-1000L,false);
		TTEventCreatePowerUp ttEvent8 = new TTEventCreatePowerUp(worldName,worldPassword,pup);
		resultChecker = new ResultChecker(false);
		event = new TTEventWrapper(TTEventType.CREATE_POWER_UP,ttEvent8,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResultOK() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try{
			assertTrue(resultChecker.getResultOK());
		}
		catch(AssertionError e){
			System.err.println(resultChecker.getResults().toJSONString());
			throw e;
		}
		
		return eventPublisher;
	}

	@Test
	public void testWebServer() {
		
		TTEventWrapperQueuer publisher = startATestSystem();
		
		startAWebServer(testPortPlusPlus(),publisher);
		
		/* Check the version */
		String responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		JSONObject response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertEquals(TEST_VERSION,response.get("version"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		/* Build a tower without world name */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		
		/* Build a tower without world password */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		

		

		
		/* Build a tower without player name */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		

		

		
		/* Build a tower without player password */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		/* Build a tower without lat */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		

		
		
		
		
		/* Build a tower without lng */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("lat", "0.0");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		
		
		
		
		/* Build a tower without alt */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("lat", "0.0");
			params.put("lng", "0.0");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		/* Build weird numbers */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("lat", "woof");
			params.put("lng", "bark");
			params.put("alt", "meow");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		

		/* Build a tower OK! */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("lat", "33.645");
			params.put("lng", "-117.842");
			params.put("alt", "10.0");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/build_tower", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		/* Drop a bomb*/
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("lat", "33.645");
			params.put("lng", "-117.842");
			params.put("alt", "10.0");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/drop_bomb", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		
		
		
		
		/* Game State without a player password */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/get_game_state", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("true",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		} catch (AssertionError e){
			System.err.println(response.toJSONString());
			throw e;
		}
		
		
		
		
		
		/* Game State */
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/get_game_state", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.err.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		} catch (AssertionError e){
			System.err.println(response.toJSONString());
			throw e;
		}
		
		
		
		
		
		
		/* Get Leader Board*/
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			
			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/get_leader_board", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		} catch (AssertionError e){
			System.err.println(response.toJSONString());
			throw e;
		}
		
		
		/* Redeem a power up*/
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("world_name", worldName);
			params.put("world_password", worldPassword);
			params.put("player_name", playerName);
			params.put("player_password", playerPassword);
			params.put("code", "code");

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/redeem_power_up", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception"+e);
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		} catch (AssertionError e){
			System.err.println(response.toJSONString());
			throw e;
		}
		
		
		
		
		
		
		responseString = null;
		try {
			HashMap<String, String> params = new HashMap<String, String>();

			responseString = WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/shutdown", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		//System.out.println(responseString);
		

		response = null;
		try {
			response = (JSONObject) JSONValue.parse(responseString);
			assertEquals("false",response.get("error"));
			assertEquals("true",response.get("shutdown"));
		} catch (ClassCastException e) {
			fail("Bad JSON Response");
		}
		
		assertTrue(Globals.getGlobals().isQuitting());

	}
	
	
	@Test
	public void testJSONConstruction() {
		
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		Player player = new Player("a",PasswordUtils.hashPassword("b"));
		Tower tower = new Tower(player,0,0);
		t.addTower(tower);
			
		t.stepTowerTerritoryGrowth(5, 2);
		Pair<Player, Integer> owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
			
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
			
		owner = t.index(-3.5,-3.5).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
			
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
			
		owner = t.index(-5.0,-4.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
			
		owner = t.index(-3.5,-3.5).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
			
			
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		long time = System.currentTimeMillis();
		JSONObject x = HandlerGetGameState.constructJSON(t, player.getPlayerName());
		//System.err.println("Elapsed time:"+(System.currentTimeMillis()-time));
		//System.err.println("Size = "+x.toString().length());
		//System.err.println("Size = "+x.toJSONString().length());
		//System.err.println("Size = "+x.toJSONString(JSONStyle.MAX_COMPRESS).length());
		time = System.currentTimeMillis();
		for(int i = 0 ; i < 1000; i++){
			HandlerGetGameState.constructJSON(t, player.getPlayerName()).toJSONString(JSONStyle.MAX_COMPRESS).getBytes();
		}
		//System.err.println("Elapsed time:"+((System.currentTimeMillis()-time)/1000.0));
			
	}



}
