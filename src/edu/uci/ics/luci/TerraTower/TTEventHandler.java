package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public interface TTEventHandler {
	
	public JSONObject checkParameters(TTEvent event);
	
	public JSONObject onEvent(TTEvent event);
	
}