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

package edu.uci.ics.luci.TerraTower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import edu.uci.ics.luci.TerraTower.events.TTEventBurnBombFuse;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePowerUp;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateTerritory;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;
import edu.uci.ics.luci.TerraTower.events.TTEventStepTowerTerritoryGrowth;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerBuildTower;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerDropBomb;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerGetGameState;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerGetLeaderBoard;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerRedeemPowerUp;
import edu.uci.ics.luci.TerraTower.webhandlers.HandlerShutdown;
import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;

public class TerraTower {
	private static int port = 9021;
	
	public final static String VERSION="0.1";

	private static transient volatile Logger log = null;

	
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(TerraTower.class);
		}
		return log;
	}
	
	
	private static TTEventWrapperQueuer eventPublisher;
	
	/**
	 * Create Event Disruptor
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static TTEventWrapperQueuer createEventQueue(String logFile) {
		// Executor that will be used to construct new threads for consumers
	    Executor executor = Executors.newCachedThreadPool();
	
	    // The factory for the event
	    TTEventWrapperFactory factory = new TTEventWrapperFactory();
	
	    // Specify the size of the ring buffer, must be power of 2.
	    int bufferSize = 1024;
	
	    // Construct the Disruptor
	    Disruptor<TTEventWrapper> disruptor = new Disruptor<TTEventWrapper>(factory, bufferSize, executor);
	
	    // Connect the handler
	    disruptor.handleEventsWith(new TTEventWrapperHandler());
	        
	    // Start the Disruptor, starts all threads running
	    disruptor.start();
	
	    // Get the ring buffer from the Disruptor to be used for publishing.
	    RingBuffer<TTEventWrapper> ringBuffer = disruptor.getRingBuffer();
	
	    TTEventWrapperQueuer localEventPublisher = new TTEventWrapperQueuer(disruptor,ringBuffer,logFile);
	    
	    return(localEventPublisher);
	}

	public static void main(String[] args) throws ConfigurationException {

		/* Try and turn off log messages about the log messaging system */
		System.setProperty("Log4jDefaultStatusLevel","error");
		
		/* Get configurations */
		Configuration config = new PropertiesConfiguration( "TerraTower.properties");

		/* Set up the global variable */
		Globals.setGlobals(new GlobalsTerraTower(VERSION));
		
		/* Set up an event queue with logging */
		String logFileName = config.getString("event.logfile");
		eventPublisher = createEventQueue(logFileName);
		Globals.getGlobals().addQuittable(eventPublisher);
		
		/* Create a world */
		String worldName = config.getString("world.name"); 
		String worldPassword = config.getString("world.password"); 
		EventHandlerResultChecker rc = new EventHandlerResultChecker();
		TTEventWrapper wrapper = new TTEventWrapper(TTEventType.CREATE_WORLD, new TTEventCreateWorld(worldName,worldPassword),rc);
		eventPublisher.onData(wrapper);
		rc.block();
		if(rc.getResults().get("error").equals("true")){
			getLog().error("Couldn't create a world:"+rc.getResults().get("errors"));
		}
		
		
		/* Create a territory */
		Double west = config.getDouble("world.longitude.west");
		Double east = config.getDouble("world.longitude.east");
		Double north = config.getDouble("world.latitude.north");
		Double south = config.getDouble("world.latitude.south");
		Integer numXSplits = config.getInt("world.xsplits");
		Integer numYSplits = config.getInt("world.ysplits");
		rc = new EventHandlerResultChecker();
		wrapper = new TTEventWrapper(TTEventType.CREATE_TERRITORY, new TTEventCreateTerritory(worldName,worldPassword,west,east,numXSplits,south,north,numYSplits),rc);
		eventPublisher.onData(wrapper);
		rc.block();
		if(rc.getResults().get("error").equals("true")){
			getLog().error("Couldn't create a territory:"+rc.getResults().get("errors"));
		}
		
		/*Create players */
		List<Object> players = config.getList("player.name");
		List<Object> passwords = config.getList("player.password");
		if(players.size()!= passwords.size()){
			getLog().error("number of players and number of passwords not equal");
		}
		List<EventHandlerResultChecker> results = new ArrayList<EventHandlerResultChecker>();
		for(int i = 0; i < players.size(); i++){
			rc = new EventHandlerResultChecker();
			results.add(rc);
			wrapper = new TTEventWrapper(TTEventType.CREATE_PLAYER, new TTEventCreatePlayer(worldName,worldPassword,players.get(i).toString(),(String)passwords.get(i).toString()),rc);
			eventPublisher.onData(wrapper);
		}
		for(EventHandlerResultChecker r :results){
			r.block();
			if(r.getResults().get("error").equals("true")){
				getLog().error("Couldn't create a player:"+r.getResults().get("errors"));
			}
		}
		
		
		
		/* Create powerups */
		List<Object> powerUps = config.getList("power_up");
		results.clear();
		for(int i = 0; i < powerUps.size(); i++){
			rc = new EventHandlerResultChecker();
			results.add(rc);
			JSONObject json;
			PowerUp pup = null;
			try{
				json = (JSONObject) JSONValue.parse(powerUps.get(i).toString());
				pup = PowerUp.fromJSON(json);
				wrapper = new TTEventWrapper(TTEventType.CREATE_POWER_UP, new TTEventCreatePowerUp(worldName,worldPassword,pup.getCode(),pup.getTowerDelayDelta(),pup.getBombDelayDelta(),pup.getBombFuseDelta()),rc);
				eventPublisher.onData(wrapper);
			}
			catch(NullPointerException e){
				getLog().error("Power up did not parse correctly:"+powerUps.get(i).toString()+"\n"+e);
			}
		}
		for(EventHandlerResultChecker r :results){
			r.block();
			if(r.getResults().get("error").equals("true")){
				getLog().error("Couldn't create a power up:"+r.getResults().get("errors"));
			}
		}

		WebServer ws = null;
		HashMap<String, HandlerAbstract> requestHandlerRegistry;

		try {
			requestHandlerRegistry = new HashMap<String, HandlerAbstract>();
			requestHandlerRegistry.put("", new HandlerVersion(VERSION));
			requestHandlerRegistry.put("version", new HandlerVersion(VERSION));
			requestHandlerRegistry.put("build_tower", new HandlerBuildTower(eventPublisher));
			requestHandlerRegistry.put("drop_bomb", new HandlerDropBomb(eventPublisher));
			requestHandlerRegistry.put("redeem_power_up", new HandlerRedeemPowerUp(eventPublisher));
			requestHandlerRegistry.put("get_leader_board", new HandlerGetLeaderBoard(eventPublisher));
			requestHandlerRegistry.put("get_game_state", new HandlerGetGameState());
			requestHandlerRegistry.put("shutdown", new HandlerShutdown(Globals.getGlobals()));

			RequestDispatcher dispatcher = new RequestDispatcher(
					requestHandlerRegistry);
			ws = new WebServer(dispatcher, port, false, new AccessControl());
			ws.start(1000);

			Globals.getGlobals().addQuittable(ws);

		} catch (RuntimeException e) {
			e.printStackTrace();
			Globals.getGlobals().setQuitting(true);
			return;
		}

		
		long clockStep = config.getLong("clock.step");
		/* Start the game server */
		long lastTime = System.currentTimeMillis();
		while(!Globals.getGlobals().isQuitting()){
			/*In case Thread.sleep is interrupted we wrap in a loop */
			
			while(System.currentTimeMillis() - lastTime < clockStep){
				try {
					Thread.sleep(GlobalsTerraTower.ONE_SECOND);
				} catch (InterruptedException e) {
				}
			}
			
			/* Step the tower growth by one */
			rc = new EventHandlerResultChecker();
			wrapper = new TTEventWrapper(TTEventType.STEP_TOWER_TERRITORY_GROWTH, new TTEventStepTowerTerritoryGrowth(worldName,worldPassword),rc);
			eventPublisher.onData(wrapper);
			rc.block();
			if(rc.getResults().get("error").equals("true")){
				getLog().error("Couldn't step tower territory:"+rc.getResults().get("errors"));
			}
			
			/* Burn the bomb fuses */
			rc = new EventHandlerResultChecker();
			wrapper = new TTEventWrapper(TTEventType.BURN_BOMB_FUSE, new TTEventBurnBombFuse(worldName,worldPassword),rc);
			eventPublisher.onData(wrapper);
			rc.block();
			if(rc.getResults().get("error").equals("true")){
				getLog().error("Couldn't burn the bomb fuse:"+rc.getResults().get("errors"));
			}
			
			lastTime = System.currentTimeMillis();
		}
		
	}
		
}

