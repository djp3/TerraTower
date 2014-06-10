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


import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher.HTTPRequest;

public class HandlerGetLeaderBoard extends HandlerAbstractWorld{
	
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerGetLeaderBoard.class);
		}
		return log;
	}
	
	public HandlerGetLeaderBoard(TTEventWrapperQueuer eventPublisher) {
		super(eventPublisher);
	}


	@Override
	public HandlerAbstract copy() {
		return new HandlerGetLeaderBoard(getEventPublisher());
	}
	
	/**
	 * @param parameters a map of key and value that was passed through the REST request
	 * @return a pair where the first element is the content type and the bytes are the output bytes to send back
	 */
	@Override
	public Pair<byte[], byte[]> handle(InetAddress ip, HTTPRequest httpRequestType, Map<String, String> headers, String restFunction, Map<String, String> parameters) {
		Pair<byte[], byte[]> pair = null;
		

		JSONArray errors = new JSONArray();
		
		errors.addAll(getWorldParameters(restFunction,parameters));
		
		GlobalsTerraTower globalsTerraTower = GlobalsTerraTower.getGlobalsTerraTower();
		List<Pair<Integer, Player>> leaderBoard = null;
		if(globalsTerraTower == null){
			errors.add("Problem handling "+restFunction+": globals was null");
		}
		else{
			WorldManager world = globalsTerraTower.getWorld(getWorldName(), getWorldPassword());
			if(world == null){
				errors.add("Problem handling "+restFunction+": world manager was null");
			}
			else{
				Territory territory = world.getTerritory();
				if(territory == null){
					errors.add("Problem handling "+restFunction+": territory was null");
				}
				else{
					leaderBoard = territory.getLeaderBoard();
					if(leaderBoard == null){
						errors.add("Problem handling "+restFunction+": leader board was null");
					}
				}
			}
		}
		
		
		JSONObject ret = new JSONObject();
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
		}
		else{
			JSONObject result = new JSONObject();
			for(Pair<Integer, Player> e: leaderBoard){
				result.put(e.getSecond().getPlayerName(), e.getFirst().toString());
			}
			ret.put("error", "false");
			ret.put("result", result);
		}
		
		pair = new Pair<byte[],byte[]>(HandlerAbstract.getContentTypeHeader_JSON(),wrapCallback(parameters,ret.toString()).getBytes());
		return pair;
	}
}


