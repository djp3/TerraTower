package edu.uci.ics.luci.TerraTower.gameEvents;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.TTEvent;
import edu.uci.ics.luci.TerraTower.TTEventHandler;
import edu.uci.ics.luci.TerraTower.world.WorldManager;


public class TTEventHandlerCreateWorld implements TTEventHandler{    
	
	@Override
	public JSONObject onEvent(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreateWorld event = (TTEventCreateWorld) _event;
		WorldManager wm = GlobalsTerraTower.getGlobalsTerraTower().getWorldManager();
		if(wm.worldExists(event.getName())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World with name, "+event.getName()+" already exists");
			ret.put("errors", errors);
		}else{
			if(wm.create(event.getName(),event.getPassword())){
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

	@Override
	public JSONObject checkParameters(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreateWorld event = (TTEventCreateWorld) _event;
		
		if(event.getName() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null name");
			ret.put("errors", errors);
			return ret;
		}
		
		if(event.getPassword() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World can't have a null password");
			ret.put("errors", errors);
			return ret;
		}
		
		return null;
	}
}
