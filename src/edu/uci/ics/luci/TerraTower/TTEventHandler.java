package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public interface TTEventHandler {
	
	public JSONObject checkParameters(TTEvent event);
	
	public JSONObject onEvent(TTEvent event);

	public int hashCode();
	public boolean equals(Object obj);
}
