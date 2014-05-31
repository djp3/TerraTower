package edu.uci.ics.luci.TerraTower;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

public class TTEventProducerWithTranslator{ 
    private final RingBuffer<TTEvent> ringBuffer;

    public TTEventProducerWithTranslator(RingBuffer<TTEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<TTEvent, TTEvent> TRANSLATOR =
        new EventTranslatorOneArg<TTEvent,TTEvent>()
        {
            public void translateTo(TTEvent event, long sequence, TTEvent incoming)
            {
                event.set(incoming);
            }
        };

    public void onData(TTEvent incoming)
    {
        ringBuffer.publishEvent(TRANSLATOR, incoming);
    }
}
