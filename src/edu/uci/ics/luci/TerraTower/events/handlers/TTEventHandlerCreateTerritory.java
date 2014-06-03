package edu.uci.ics.luci.TerraTower.events.handlers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreateTerritory;
import edu.uci.ics.luci.TerraTower.world.Territory;


public class TTEventHandlerCreateTerritory extends TTEventHandler{
	
	private double left;
	private double right;
	private int numXSplits;
	private double top;
	private double bottom;
	private int numYSplits;

	@Override
	public JSONObject checkParameters(long eventTime, TTEvent _event) {
		//Check parent 
		JSONObject ret = super.checkParameters(eventTime, _event);
		if(ret != null){
			return ret;
		}
		ret = new JSONObject();
				
		TTEventCreateTerritory event = null;
		if(_event instanceof TTEventCreateTerritory){
			event = ((TTEventCreateTerritory) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}

		//Check that left right top bottom are all correctly ordered
		left = event.getLeft();
		right = event.getRight();
		numXSplits = event.getNumXSplits();
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
		top = event.getTop();
		bottom = event.getBottom();
		numYSplits = event.getNumYSplits();
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

	@Override
	public JSONObject onEvent()  {   
		
		JSONObject ret = super.onEvent();
		if(ret.get("error").equals("true")){
			return ret;
		}
		ret = new JSONObject();
		
		Territory t = new Territory(left,right,numXSplits,bottom,top,numYSplits);
		
		wm.setTerritory(t);
		
		ret.put("error","false");
		return ret;
	}

}
