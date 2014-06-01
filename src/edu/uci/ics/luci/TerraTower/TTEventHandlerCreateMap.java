package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.world.Map;
import edu.uci.ics.luci.TerraTower.world.WorldManager;


public class TTEventHandlerCreateMap implements TTEventHandler{

	@Override
	public JSONObject onEvent(TTEvent _event)  {   
		TTEventCreateMap event = (TTEventCreateMap) _event;
		
		JSONObject ret = new JSONObject();
		
		WorldManager wm = GlobalsTerraTower.getGlobalsTerraTower().getWorldManager();
		
		Map map = new Map(event.getLeft(),
							event.getRight(),
							event.getNumXSplits(),
							event.getBottom(),
							event.getTop(),
							event.getNumYSplits());
		
		String worldName = event.getWorldName();
		String password = event.getPassword();
		
		if(!wm.worldExists(worldName)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World with name, "+worldName+" already exists");
			ret.put("errors", errors);
		}
		else if(!wm.passwordGood(worldName, password)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("World exist with name, "+worldName+" but bad password: "+password);
			ret.put("errors", errors);
		}
		else if(wm.mapExists(worldName)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Map already exists for world with name, "+worldName);
			ret.put("errors", errors);
		}
		else{
			if(!wm.addMap(event.getWorldName(),event.getPassword(),map)){
				ret.put("error","true");
				JSONArray errors = new JSONArray();
				errors.add("Making a new map failed for world with name, "+worldName);
				ret.put("errors", errors);
			}
			else{
				ret.put("error","false");
			}
		}
		return ret;
	}
}
