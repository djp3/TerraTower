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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateWorld;


public class TTEventHandlerCreateWorld extends TTEventHandler{    
	

	private GlobalsTerraTower g = null;
	private String worldName = null;
	private byte[] worldHashedPassword = null;
	
	@Override
	public JSONObject checkParameters(long eventTime, TTEvent _event) {
		//Don't Check parent because parent checks if world exists see TTEventHandlerPlaceTower for example
		JSONObject ret = new JSONObject();
		
		TTEventCreateWorld event = null;
		if(_event instanceof TTEventCreateWorld){
			event = ((TTEventCreateWorld) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}

		//Make sure the world name is not null
		worldName = event.getWorldName();
		if(worldName == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null name");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure the world password is not null
		worldHashedPassword = event.getWorldHashedPassword();
		/*We know it's not null*/
		/*
		if(worldHashedPassword == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null password");
			ret.put("errors", errors);
			return ret;
		}*/
		
		//Make sure globals is not null
		g = GlobalsTerraTower.getGlobalsTerraTower();
		if(g == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Global variables have not been initiazed");
			ret.put("errors", errors);
			return ret;
		}
		
		//Make sure world doesn't exist
		if(g.worldExists(event.getWorldName())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("A world with name "+event.getWorldName()+" already exists");
			ret.put("errors", errors);
			return ret;
		}
		
		return null;
	}
	
	@Override
	public JSONObject onEvent() {
		JSONObject ret = new JSONObject();
		
		if(!g.createWorld(worldName, worldHashedPassword)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Unable to create a world for an unknown reason");
			ret.put("errors", errors);
			return ret;
		}
		
		ret.put("error", "false");
		return ret;
	}

}
