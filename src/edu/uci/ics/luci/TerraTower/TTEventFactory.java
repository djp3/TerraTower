package edu.uci.ics.luci.TerraTower;

import com.lmax.disruptor.EventFactory;

public class TTEventFactory implements EventFactory<TTEvent>
{
    public TTEvent newInstance()
    {
        return new TTEvent(TTEvent.TTEventType.VOID);
    }
}
