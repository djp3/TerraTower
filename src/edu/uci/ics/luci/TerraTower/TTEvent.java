package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public interface TTEvent{
	
	public JSONObject toJSON();
	
	public int hashCode();
	public boolean equals(Object obj);
}
