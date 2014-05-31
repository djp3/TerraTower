package edu.uci.ics.luci.TerraTower;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


public class TTEventHandlerCreateWorld implements TTEventHandler{    
	
	@Override
	public JSONObject onEvent(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreateWorld event = (TTEventCreateWorld) _event;
		if(WorldManager.exists(event.getName())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World with name, "+event.getName()+" already exists");
			ret.put("errors", errors);
		}else{
			if(WorldManager.create(event.getName(),event.getPassword())){
				ret.put("error", "false");
			}
			else{
				ret.put("error","true");
				JSONArray errors = new JSONArray();
				errors.add("World with name, "+event.getName()+" could not be created");
				ret.put("errors", errors);
			}
		}
		return ret;
	}
}
