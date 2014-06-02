package edu.uci.ics.luci.TerraTower.gameEvents;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.TTEvent;
import edu.uci.ics.luci.TerraTower.TTEventHandler;
import edu.uci.ics.luci.TerraTower.world.WorldManager;


public class TTEventHandlerCreatePlayer implements TTEventHandler{    
	
	@Override
	public JSONObject onEvent(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreatePlayer event = (TTEventCreatePlayer) _event;
		WorldManager wm = GlobalsTerraTower.getGlobalsTerraTower().getWorldManager();
		if(wm.playerExists(event.getPlayerName())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player with name, "+event.getPlayerName()+" already exists");
			ret.put("errors", errors);
		}else{
			if(wm.createPlayer(event.getPlayerName(),event.getHashedPassword())){
				ret.put("error", "false");
			}
			else{
				ret.put("error","true");
				JSONArray errors = new JSONArray();
				errors.add("Player with name, "+event.getPlayerName()+" could not be created");
				ret.put("errors", errors);
			}
		}
		return ret;
	}

	@Override
	public JSONObject checkParameters(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreatePlayer event = (TTEventCreatePlayer) _event;
		
		if(event.getPlayerName() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Player can't have a null name");
			ret.put("errors", errors);
			return ret;
		}
		
		
		return null;
	}
}
