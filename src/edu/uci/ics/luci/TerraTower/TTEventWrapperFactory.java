package edu.uci.ics.luci.TerraTower;

import com.lmax.disruptor.EventFactory;

public class TTEventWrapperFactory implements EventFactory<TTEventWrapper>
{
    public TTEventWrapper newInstance()
    {
        return new TTEventWrapper(TTEventType.VOID,null,(TTEventHandlerResultListener)null);
    }
}
