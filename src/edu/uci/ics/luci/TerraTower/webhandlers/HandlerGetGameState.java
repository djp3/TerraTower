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


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.world.GridCell;
import edu.uci.ics.luci.TerraTower.world.Territory;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;
import edu.uci.ics.luci.utility.webserver.input.request.Request;
import edu.uci.ics.luci.utility.webserver.output.channel.Output;
import edu.uci.ics.luci.utility.webserver.output.response.Response;

public class HandlerGetGameState extends HandlerAbstractPlayer{
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerVersion.class);
		}
		return log;
	}
	
	static Map<Pair<String, Integer>, Response> responseCache = Collections.synchronizedMap(new HashMap<Pair<String, Integer>,Response>());
	static Map<String,Pair<String,Integer>> responseCacheKey = Collections.synchronizedMap(new HashMap<String, Pair<String, Integer>>());

	public HandlerGetGameState() {
		super(null);
	}

	@Override
	public HandlerAbstract copy() {
		return new HandlerGetGameState();
	}
	
	/**
	 * @param parameters a map of key and value that was passed through the REST request
	 * @return a pair where the first element is the content type and the bytes are the output bytes to send back
	 */
	@Override
	public Response handle(Request request, Output o) {
		
		JSONObject ret = new JSONObject();
		
		Response response = o.makeOutputChannelResponse();
		long time = System.currentTimeMillis();
		
		JSONArray errors = new JSONArray();
		
		errors.addAll(getWorldParameters(request.getCommand(),request.getParameters()));
		errors.addAll(getPlayerParameters(request.getCommand(),request.getParameters()));
		
		GlobalsTerraTower globalsTerraTower = GlobalsTerraTower.getGlobalsTerraTower();
		Territory territory = null;
		if(globalsTerraTower == null){
			errors.add("Problem handling "+request.getCommand()+": globals was null");
		}
		else{
			WorldManager world = globalsTerraTower.getWorld(getWorldName(), getWorldPassword());
			if(world == null){
				errors.add("Problem handling "+request.getCommand()+": world manager was null");
			}
			else{
				territory = world.getTerritory();
				if(territory == null){
					errors.add("Problem handling "+request.getCommand()+": territory was null");
				}
			}
		}
		
		
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
			response.setStatus(Response.Status.OK);
			response.setDataType(Response.DataType.JSON);
			response.setBody(wrapCallback(request.getParameters(),ret.toString()));
		}
		else{
			Pair<String, Integer> p = new Pair<String,Integer>(getPlayerName(),territory.hashCode());
			if(responseCache.containsKey(p)){
				getLog().info("Cache Hit:"+responseCache.size()+" entries,"+responseCacheKey.size()+" key entries");
				response = responseCache.get(p);
			}
			else{
				getLog().info("Cache Miss:"+responseCache.size()+" entries,"+responseCacheKey.size()+" key entries");
				/* get the key associated with the player for the cache probe we just missed */
				Pair<String, Integer> oldP = responseCacheKey.get(getPlayerName());
				/* remove the cache entry */
				if(oldP != null){
					responseCache.remove(oldP);
				}
				/* Make the new response */
				ret = constructJSON(territory,getPlayerName());
				response.setStatus(Response.Status.OK);
				response.setDataType(Response.DataType.JSON);
				response.setBody(wrapCallback(request.getParameters(),ret.toString()));
				/* Store it in the cache */
				responseCacheKey.put(getPlayerName(), p);
				responseCache.put(p,response);
			}
		}
		
		getLog().info("Done handling get_game_state "+(System.currentTimeMillis()-time));
		return response;
	}

	static public JSONObject constructJSON(Territory territory,String playerName) {
		JSONObject ret = new JSONObject();
		
		JSONObject response = new JSONObject();
		response.put("origin_x", territory.getLeft());
		response.put("origin_y", territory.getBottom());
			
		response.put("num_x_splits", territory.getNumXSplits());
		response.put("num_y_splits", territory.getNumYSplits());
			
		response.put("step_x_meters", territory.getStepXMeters());
		response.put("step_y_meters", territory.getStepYMeters());
		JSONArray result = new JSONArray();
		for(int x = 0; x < territory.getNumXSplits();x++){
			for(int y = 0; y < territory.getNumYSplits();y++){
				JSONObject jCell = new JSONObject();
				GridCell cell = territory.index(x,y);
				jCell.put("index_x", x+"");
				jCell.put("index_y", y+"");
				//jCell.put("left",""+(x*territory.getStepX()+territory.getLeft()));
				//jCell.put("right",""+((x+1)*territory.getStepX()+territory.getLeft()));
				//jCell.put("top",""+((y+1)*territory.getStepX()+territory.getBottom()));
				//jCell.put("bottom",""+((y)*territory.getStepX()+territory.getBottom()));
				jCell.put("alt",""+cell.estimateAltitude());
				jCell.put("land_owner", cell.getOwner().getFirst().getPlayerName());
				jCell.put("land_claim", cell.getOwner().getSecond());
				if(cell.towerPresent()){
					if(cell.getTower().getOwner().getPlayerName().equals(playerName)){
						jCell.put("tower", "true");
					}
					else{
						jCell.put("tower", "unknown");
					}
				}
				else{
					jCell.put("tower", "unknown");
				}
				boolean bombPresent = false;
				if(cell.numBombsPresent()> 0){
					bombPresent = true;
					/* Only make bombs you own visible */
					/*
					for(Entry<Long, List<Bomb>> e:cell.getBombs().entrySet()){
							for(Bomb b:e.getValue()){
							if(b.getOwner().getPlayerName().equals(getPlayerName())){
								bombPresent = true;
							}
						}
					}*/
				}
				if(bombPresent){
					jCell.put("bomb", "true");
				}
				else{
					jCell.put("bomb", "unknown");
				}
				result.add(jCell);
			}
		}
		response.put("result", result);
		ret.put("error", "false");
		ret.put("data", response);
		
		return ret;
	}
}


