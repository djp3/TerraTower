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


package edu.uci.ics.luci.TerraTower.webhandlers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.http.client.utils.URIBuilder;
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
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.WebUtil;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerError;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerShutdown;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;
import edu.uci.ics.luci.utility.webserver.input.channel.socket.HTTPInputOverSocket;

public class WebHandlerTests {
	
	@BeforeClass
	public static void setUpClass() throws Exception {
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

	

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private static int testPort = 9020;
	public static synchronized int testPortPlusPlus(){
		int x = testPort;
		testPort++;
		return(x);
	}
	

	public static WebServer startAWebServerSocket(Globals globals,int port,boolean secure) {
		try {
			HTTPInputOverSocket inputChannel = new HTTPInputOverSocket(port,secure);
			HashMap<String, HandlerAbstract> requestHandlerRegistry = new HashMap<String,HandlerAbstract>();
			
			// Null is a default Handler
			requestHandlerRegistry.put(null,new HandlerError(Globals.getGlobals().getSystemVersion()));
				
			RequestDispatcher requestDispatcher = new RequestDispatcher(requestHandlerRegistry);
			AccessControl accessControl = new AccessControl();
			accessControl.reset();
			WebServer ws = new WebServer(inputChannel, requestDispatcher, accessControl);
			ws.start();
			globals.addQuittable(ws);
			return ws;
		} catch (RuntimeException e) {
			fail("Couldn't start webserver"+e);
		}
		return null;
	}


	private void startAWebServer(int port, TTEventWrapperQueuer eventPublisher) {
		try {
			boolean secure = false;
			ws = startAWebServerSocket(Globals.getGlobals(),port,secure);
			ws.getRequestDispatcher().updateRequestHandlerRegistry(null, new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("", new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/", new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/version", new HandlerVersion(Globals.getGlobals().getSystemVersion()));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/build_tower", new HandlerBuildTower(eventPublisher));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/drop_bomb", new HandlerDropBomb(eventPublisher));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/redeem_power_up", new HandlerRedeemPowerUp(eventPublisher));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/get_leader_board", new HandlerGetLeaderBoard(eventPublisher));
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/get_game_state", new HandlerGetGameState());
			ws.getRequestDispatcher().updateRequestHandlerRegistry("/shutdown", new HandlerShutdown(Globals.getGlobals()));
			
		} catch (RuntimeException e) {
			fail("Couldn't start webserver"+e);
		}
	}
	
	public TTEventWrapperQueuer startATestSystem(){
		List<TTEventWrapper> events = new ArrayList<TTEventWrapper>();
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		GlobalsTerraTower globals = new GlobalsTerraTower(TEST_VERSION,true);
		GlobalsTerraTower.setGlobals(globals);
		
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
		
		
		TTEventCreatePowerUp ttEvent8 = new TTEventCreatePowerUp(worldName,worldPassword,"code",-1000L,-1000L,-1000L);
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
		
		int port = testPortPlusPlus();
		startAWebServer(port,publisher);
		
		/* Check the version */
		String responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder()
										.setScheme("http")
										.setHost("localhost")
										.setPort(ws.getInputChannel().getPort())
										.setPath("/");

			responseString = WebUtil.fetchWebPage(uriBuilder,null,null,null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		}catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder()
											.setScheme("http")
											.setHost("localhost")
											.setPort(ws.getInputChannel().getPort())
											.setPath("/build_tower");

			responseString = WebUtil.fetchWebPage(uriBuilder,null,null,null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		}catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IO Exception");
		}
		// System.out.println(responseString);
		

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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("lat","0.0");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("lat","0.0")
					.setParameter("lng","0.0");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("lat","woof")
					.setParameter("lng","bark")
					.setParameter("alt","meow");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/build_tower")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("lat","33.645")
					.setParameter("lng","-117.842")
					.setParameter("alt","10.0");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/drop_bomb")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("lat","33.645")
					.setParameter("lng","-117.842")
					.setParameter("alt","10.0");

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/get_game_state")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			if(response != null){
				System.err.println(response.toJSONString());
			}
			else{
				System.err.println("response is null");
			}
			throw e;
		}
		
		
		
		/* Deal with rate limit */
		/*
		System.out.print("Waiting 30 seconds to defeat rate limit");
		for(int i = 0 ; i < 30;i++){
			try {
				Thread.sleep(1*1000);
				System.out.print(".");
			} catch (InterruptedException e1) {
			}
		}
		
		System.out.println(".");
		*/
		
		/* Game State */
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/get_game_state")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			if(response != null){
				System.err.println(response.toJSONString());
			}
			else{
				System.err.println("response is null");
			}
			throw e;
		}
		
		
		
		
		
		
		/* Get Leader Board*/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/get_leader_board")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword);

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			if(response != null){
				System.err.println(response.toJSONString());
			}
			else{
				System.err.println("response is null");
			}
			throw e;
		}
		
		
		/* Redeem a power up*/
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/redeem_power_up")
					.setParameter("world_name", worldName)
					.setParameter("world_password", worldPassword)
					.setParameter("player_name", playerName)
					.setParameter("player_password", playerPassword)
					.setParameter("code", "code");
					

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
			if(response != null){
				System.err.println(response.toJSONString());
			}
			else{
				System.err.println("response is null");
			}
			throw e;
		}
		
		
		
		
		
		
		responseString = null;
		try {
			URIBuilder uriBuilder = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(ws.getInputChannel().getPort())
					.setPath("/shutdown");
					

			responseString = WebUtil.fetchWebPage(uriBuilder, null, null, null, 30 * 1000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail("Bad URL");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("URLSyntaxException "+e);
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
		
		//long time = System.currentTimeMillis();
		//JSONObject x = HandlerGetGameState.constructJSON(t, player.getPlayerName());
		//t.deepCopy();
		//System.err.println("Elapsed time:"+(System.currentTimeMillis()-time));
		//System.err.println("Size = "+x.toString().length());
		//System.err.println("Size = "+x.toJSONString().length());
		//System.err.println("Size = "+x.toJSONString(JSONStyle.MAX_COMPRESS).length());
		//time = System.currentTimeMillis();
		//for(int i = 0 ; i < 1000; i++){
			//HandlerGetGameState.constructJSON(t, player.getPlayerName()).toJSONString(JSONStyle.MAX_COMPRESS).getBytes();
			//t.deepCopy();
		//}
		//System.err.println("Elapsed time:"+((System.currentTimeMillis()-time)/1000.0));
			
	}



}
