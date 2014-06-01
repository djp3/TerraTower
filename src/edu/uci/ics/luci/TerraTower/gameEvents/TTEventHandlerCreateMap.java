package edu.uci.ics.luci.TerraTower.gameEvents;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.TTEvent;
import edu.uci.ics.luci.TerraTower.TTEventHandler;
import edu.uci.ics.luci.TerraTower.world.Map;
import edu.uci.ics.luci.TerraTower.world.WorldManager;


public class TTEventHandlerCreateMap implements TTEventHandler{

	@Override
	public JSONObject onEvent(TTEvent _event)  {   
		TTEventCreateMap event = (TTEventCreateMap) _event;
		
		JSONObject ret = new JSONObject();
		
		GlobalsTerraTower globalsTerraTower = GlobalsTerraTower.getGlobalsTerraTower();
		if(globalsTerraTower == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Globals wasn't initialized with GlobalsTerraTower");
			ret.put("errors", errors);
			return ret;
		}
		WorldManager wm = globalsTerraTower.getWorldManager();
		if(wm == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("WorldManager not assigned in GlobalsTerraTower");
			ret.put("errors", errors);
			return ret;
		}		
		
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
	
	@Override
	public JSONObject checkParameters(TTEvent _event) {
		JSONObject ret = new JSONObject();
		TTEventCreateMap event = (TTEventCreateMap) _event;

		if(event.getWorldName() == null){
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
		
		double left = event.getLeft();
		double right = event.getRight();
		int numXSplits = event.getNumXSplits();
		if (left >= right) {
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("left must be less than right");
			ret.put("errors", errors);
			return ret;
		}
		if (numXSplits <= 0) {
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("number of X splits must be greater than 0");
			ret.put("errors", errors);
			return ret;
		}
		double top = event.getTop();
		double bottom = event.getBottom();
		int numYSplits = event.getNumYSplits();
		if (bottom >= top) {
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("bottom must be less than top");
			ret.put("errors", errors);
			return ret;
		}
		if (numYSplits <= 0) {
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("number of Y splits must be greater than 0");
			ret.put("errors", errors);
			return ret;
		}
		
		return null;
	}
}
