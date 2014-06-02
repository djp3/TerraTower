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
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePlayer;


public class TTEventHandlerCreatePlayer extends TTEventHandler{    
	

	private String playerName;
	private byte[] playerHashedPassword;

	@Override
	public JSONObject checkParameters(long eventTime,TTEvent _event) {
		//Check parent 
		JSONObject ret = super.checkParameters(eventTime, _event);
		if(ret != null){
			return ret;
		}
		ret = new JSONObject();
				
		TTEventCreatePlayer event = null;
		if(_event instanceof TTEventCreatePlayer){
			event = ((TTEventCreatePlayer) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}

		//Does player name exist?
		playerName = event.getPlayerName();
		if(playerName == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Playername was null");
			ret.put("errors", errors);
			return ret;
		}

		//Does player password exist?
		playerHashedPassword = event.getPlayerHashedPassword();
		/*We know it can't be null*/
		/*
		if(playerHashedPassword == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player password was null");
			ret.put("errors", errors);
			return ret;
		}*/
		
		//Does player already exist?
		if(wm.playerExists(playerName)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player already exists can't creat them");
			ret.put("errors", errors);
			return ret;
		}
		
		return null;
	}
	
	@Override
	public JSONObject onEvent() {
		JSONObject ret = new JSONObject();
		if(!wm.createPlayer(playerName,playerHashedPassword)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Could not create player for unknown reason");
			ret.put("errors", errors);
		}
		else{
			ret.put("error", "false");
		}
		return ret;
	}

}
