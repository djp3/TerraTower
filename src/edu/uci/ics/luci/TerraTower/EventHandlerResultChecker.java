package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class EventHandlerResultChecker implements TTEventHandlerResultListener{
	
	private Object semaphore = new Object();
	private JSONObject results;
	
	public Object getSemaphore() {
		return semaphore;
	}
	
	public JSONObject getResults() {
		return results;
	}

	@Override
	public void onFinish(JSONObject result) {
		synchronized(semaphore){
			results = result;
			semaphore.notifyAll();
		}
	}
	
}