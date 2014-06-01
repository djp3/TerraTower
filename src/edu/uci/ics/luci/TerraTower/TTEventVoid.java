package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class TTEventVoid implements TTEvent{
	
	private int dummy = 1;

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		return ret;
	}
	
	static public TTEventVoid fromJSON(JSONObject in) {
		return(new TTEventVoid());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dummy;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TTEventVoid)) {
			return false;
		}
		TTEventVoid other = (TTEventVoid) obj;
		if (dummy != other.dummy) {
			return false;
		}
		return true;
	}


	
}
