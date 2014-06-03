/*
	Copyright 2014
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java TerraTower game, i.e. "TerraTower"

    TerraTower is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.luci.TerraTower;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.TerraTower.events.TTEventVoid;

public class DisruptorTest
{
	@Test
    public void testDisruptor() 
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
        disruptor.handleEventsWith(new TTEventWrapperHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<TTEventWrapper> ringBuffer = disruptor.getRingBuffer();

        TTEventWrapperQueuer producer = new TTEventWrapperQueuer(ringBuffer);

        TTEventWrapper event = new TTEventWrapper(TTEventType.VOID,new TTEventVoid(),(TTEventHandlerResultListener)null);
        for (int i = 0 ; i< 25000;i++)
        {
            producer.onData(event);
        }
    }
}