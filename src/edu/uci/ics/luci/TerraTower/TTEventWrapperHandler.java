package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

import com.lmax.disruptor.EventHandler;

public class TTEventWrapperHandler implements EventHandler<TTEventWrapper> {
	public void onEvent(TTEventWrapper eventWrapper, long sequence, boolean endOfBatch) {
		
		TTEventHandler handler = eventWrapper.getHandler();
		TTEvent event = eventWrapper.getEvent();
		JSONObject result = null;
		if(handler != null){
			result = handler.onEvent(event);
		}
		
		for(TTEventHandlerResultListener rl:eventWrapper.getResultListeners()){
			rl.onFinish(result);
		}
	}
}
