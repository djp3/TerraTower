/*
	Copyright 2014-2015
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minidev.json.JSONObject;

import com.lmax.disruptor.EventHandler;

import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.handlers.TTEventHandler;

public class TTEventWrapperHandler implements EventHandler<TTEventWrapper> {

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(TTEventWrapperHandler.class);
		}
		return log;
	}
	
	public void onEvent(TTEventWrapper eventWrapper, long sequence, boolean endOfBatch) {
		
		TTEventHandler handler = eventWrapper.getHandler();
		TTEvent event = eventWrapper.getEvent();
		long timestamp = eventWrapper.getTimestamp();
		getLog().debug("Handling a "+eventWrapper.getEventType().toString());
		JSONObject result = null;
		if(handler != null){
			result = handler.checkParameters(timestamp,event);
			if(result == null){
				JSONObject result2 = handler.onEvent();
				result = result2;
			}
		}
		
		for(TTEventHandlerResultListener rl:eventWrapper.getResultListeners()){
			rl.onFinish(result);
		}
		getLog().debug("Done Handling a "+eventWrapper.getEventType().toString());
	}
}
