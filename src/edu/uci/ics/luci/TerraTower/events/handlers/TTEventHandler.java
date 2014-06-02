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

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.world.WorldManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class TTEventHandler {
	
	protected WorldManager wm = null;
	
	protected WorldManager getWorldManager(){
		return wm;
	}
	
	/** 
	 * 
	 * @param eventTime
	 * @param event
	 * @return JSONObject with description of error or null if no errors
	 */
	public JSONObject checkParameters(long eventTime, TTEvent event){
		
		JSONObject ret = new JSONObject();
		
		//Make sure the world name is not null
		if(event.getWorldName() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null name");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure the world password is not null
		if(event.getWorldHashedPassword() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null password");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure globals is not null
		GlobalsTerraTower g = GlobalsTerraTower.getGlobalsTerraTower();
		if(g == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Global variables have not been initiazed");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure world exists 
		if(!g.worldExists(event.getWorldName())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("A world with name "+event.getWorldName()+" does not exist");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure world password is good 
		wm = g.getWorld(event.getWorldName(), event.getWorldHashedPassword());
		if(wm == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Password for world "+event.getWorldName()+" didn't match");
			ret.put("errors", errors);
			return ret;
		}
		return null;
	}
	
	
	public JSONObject onEvent(){
		JSONObject ret = new JSONObject();
		ret.put("error","false");
		return ret;
	}

}
