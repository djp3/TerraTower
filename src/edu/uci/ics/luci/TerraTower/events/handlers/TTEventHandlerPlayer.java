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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventPlayer;
import edu.uci.ics.luci.TerraTower.gameElements.Player;


public class TTEventHandlerPlayer extends TTEventHandler{
	
	private boolean parametersChecked = false;
	
	protected Player player;
	

	private boolean getParametersChecked() {
		return parametersChecked;
	}

	private void setParametersChecked(boolean parametersChecked) {
		this.parametersChecked = parametersChecked;
	}

	public JSONObject checkParameters(long eventTime, TTEvent _event){
		//Check parent 
		JSONObject ret = super.checkParameters(eventTime, _event);
		if(ret != null){
			return ret;
		}
		ret = new JSONObject();
						
		TTEventPlayer event = null;
		if(_event instanceof TTEventPlayer){
			event = ((TTEventPlayer) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}
		

		//Does player name exist?
		String playerName = event.getPlayerName();
		if(playerName == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Playername was null");
			ret.put("errors", errors);
			return ret;
		}
		

		//Does player password exist?
		byte[] playerHashedPassword = event.getPlayerHashedPassword();
		/* We know this can't be null*/
		/*
		if(playerHashedPassword == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player password was null");
			ret.put("errors", errors);
			return ret;
		}*/
		
		//Does player exist?
		if(!wm.playerExists(playerName)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player doesn't exist");
			ret.put("errors", errors);
			return ret;
		}
		
		//Is password correct?
		player = wm.getPlayer(playerName, playerHashedPassword);
		if(player == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player password doesn't match");
			ret.put("errors", errors);
			return ret;
		}
		
		this.setParametersChecked(true);
		
		return null;
	}
	
	
	public JSONObject onEvent(){

		JSONObject ret = super.onEvent();
		if(ret.get("error").equals("true")){
			return ret;
		}
		
		if(!this.getParametersChecked()){
			ret = new JSONObject();
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Parameters were not checked before calling onEvent");
			ret.put("errors", errors);
			return ret;
		}
			
		this.setParametersChecked(false);
		return(ret);
	}

}
