package edu.uci.ics.luci.TerraTower;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class TTEventMain
{
    public static void main(String[] args) throws Exception
    {
        // Executor that will be used to construct new threads for consumers
        Executor executor = Executors.newCachedThreadPool();

        // The factory for the event
        TTEventWrapperFactory factory = new TTEventWrapperFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<TTEventWrapper> disruptor = new Disruptor<TTEventWrapper>(factory, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(new TTEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<TTEventWrapper> ringBuffer = disruptor.getRingBuffer();

        TTEventWrapperQueuer producer = new TTEventWrapperQueuer(ringBuffer);

        TTEventWrapper event = new TTEventWrapper(TTEventType.CREATE_MAP);
        for (int i = 0 ; i< 250000;i++)
        {
            producer.onData(event);
        }
    }
}