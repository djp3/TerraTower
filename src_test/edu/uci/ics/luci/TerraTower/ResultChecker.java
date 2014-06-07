package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class ResultChecker implements TTEventHandlerResultListener{
	
	private Object semaphore = new Object();
	private Boolean resultOK = null;
	private String expect;
	private JSONObject results;
	
	public Object getSemaphore() {
		return semaphore;
	}
	
	public Boolean getResultOK() {
		return resultOK;
	}

	public JSONObject getResults() {
		return results;
	}

	public ResultChecker(boolean expectError){
		if(expectError){
			this.expect = "true";
		}
		else{
			this.expect = "false";
		}
	}

	@Override
	public void onFinish(JSONObject result) {
		synchronized(semaphore){
			results = result;
			if(result == null){
				resultOK = false;
			}
			else{
				if(result.get("error") == null){
					resultOK = false;
				}
				else{
					if(result.get("error").equals(this.expect)){
						resultOK = true;
					}
					else{
						resultOK = false;
					}
				}
			}
			semaphore.notifyAll();
		}
	}
	
}