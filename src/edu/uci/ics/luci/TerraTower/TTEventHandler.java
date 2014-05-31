package edu.uci.ics.luci.TerraTower;

import com.lmax.disruptor.EventHandler;

public class TTEventHandler implements EventHandler<TTEvent> {
	public void onEvent(TTEvent event, long sequence, boolean endOfBatch) {
		switch (event.getEvent()) {
		case CREATE_WORLD:{
			TTEventHandlerCreateWorld.onEvent((TTEventCreateWorld) event);
			break;
		}
		case CREATE_MAP:{ 
			TTEventHandlerCreateMap.onEvent((TTEventCreateMap) event);
			break;
		}
		case CREATE_PLAYER:
		case VOID:
		default:
			break;
		}
	}
}
