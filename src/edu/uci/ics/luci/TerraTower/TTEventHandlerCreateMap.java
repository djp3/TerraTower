package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;


public class TTEventHandlerCreateMap implements TTEventHandler{

	@Override
	public JSONObject onEvent(TTEvent _event)  {   
		TTEventCreateMap event = (TTEventCreateMap) _event;
		Map map = new Map(event.getLeft(),
							event.getRight(),
							event.getNumXSplits(),
							event.getBottom(),
							event.getTop(),
							event.getNumYSplits());
		JSONObject foo = null;
		return foo;
	}
}
