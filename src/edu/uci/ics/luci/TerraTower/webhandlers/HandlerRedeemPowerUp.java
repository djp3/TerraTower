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
package edu.uci.ics.luci.TerraTower.webhandlers;


import java.net.InetAddress;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;
import edu.uci.ics.luci.TerraTower.events.TTEventRedeemPowerUp;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.utility.datastructure.Pair;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher.HTTPRequest;

public class HandlerRedeemPowerUp extends HandlerAbstractPlayer {
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(HandlerRedeemPowerUp.class);
		}
		return log;
	}

	
	public HandlerRedeemPowerUp(TTEventWrapperQueuer eventPublisher) {
		super(eventPublisher);
	}

	@Override
	public HandlerAbstract copy() {
		return new HandlerRedeemPowerUp(this.getEventPublisher());
	}
	
	/**
	 * @param parameters a map of key and value that was passed through the REST request
	 * @return a pair where the first element is the content type and the bytes are the output bytes to send back
	 */
	@Override
	public Pair<byte[], byte[]> handle(InetAddress ip, HTTPRequest httpRequestType, Map<String, String> headers, String restFunction, Map<String, String> parameters) {
		Pair<byte[], byte[]> pair = null;
		

		JSONArray errors = new JSONArray();
		
		errors.addAll(getWorldParameters(restFunction,parameters));
		errors.addAll(getPlayerParameters(restFunction,parameters));
		
		String code = parameters.get("code");
		if(code == null){
			errors.add("Problem handling "+restFunction+": code was null");
		}
		
		JSONObject ret = new JSONObject();
		if(errors.size() != 0){
			ret.put("error", "true");
			ret.put("errors", errors);
		}
		else{
			TTEventRedeemPowerUp tt = new TTEventRedeemPowerUp(getWorldName(),getWorldPassword(),getPlayerName(),getPlayerPassword(),code);
			ret = this.queueEvent(TTEventType.REDEEM_POWER_UP, tt);
		}
		
		pair = new Pair<byte[],byte[]>(HandlerAbstract.getContentTypeHeader_JSON(),wrapCallback(parameters,ret.toString()).getBytes());
		return pair;
	}
}


