package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class TTEventVoid implements TTEvent{

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		return ret;
	}
	
	static public TTEventVoid fromJSON(JSONObject in) {
		return(new TTEventVoid());
	}


	
}
