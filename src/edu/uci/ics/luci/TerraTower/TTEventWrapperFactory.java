package edu.uci.ics.luci.TerraTower;

import com.lmax.disruptor.EventFactory;

import edu.uci.ics.luci.TerraTower.events.TTEventType;

public class TTEventWrapperFactory implements EventFactory<TTEventWrapper>
{
    TTEventType defaultEventType = TTEventType.VOID;
	
	TTEventWrapperFactory(){
	}
	
	TTEventWrapperFactory(TTEventType d){
		this.defaultEventType = d;
	}
	
	
    public TTEventWrapper newInstance()
    {
        return new TTEventWrapper(defaultEventType,null,(TTEventHandlerResultListener)null);
    }
}
