package edu.uci.ics.luci.TerraTower.webhandlers;

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.EventHandlerResultChecker;
import edu.uci.ics.luci.TerraTower.TTEventWrapper;
import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventType;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;

public abstract class HandlerAbstractWorld extends HandlerAbstract {
	

	private String worldName;
	private String worldPassword;
	private TTEventWrapperQueuer eventPublisher;

	protected String getWorldName() {
		return worldName;
	}

	protected String getWorldPassword() {
		return worldPassword;
	}
	
	protected TTEventWrapperQueuer getEventPublisher() {
		return eventPublisher;
	}

	HandlerAbstractWorld(TTEventWrapperQueuer eventPublisher){
		super();
		this.eventPublisher = eventPublisher;
	}

	protected JSONArray getWorldParameters(String restFunction,Map<String,String> parameters){
		JSONArray errors = new JSONArray();
		
		worldName = parameters.get("world_name");
		if(worldName == null){
			errors.add("Problem handling "+restFunction+": world_name was null");
		}
		worldPassword = parameters.get("world_password");
		if(worldPassword == null){
			errors.add("Problem handling "+restFunction+": world_password was null");
		}
		return errors;
	}
	
	protected JSONObject queueEvent(TTEventType type, TTEvent tt){
	
		EventHandlerResultChecker resultChecker = new EventHandlerResultChecker();
		TTEventWrapper event = new TTEventWrapper(type,tt,resultChecker);
		getEventPublisher().onData(event);
		synchronized(resultChecker.getSemaphore()){
			while(resultChecker.getResults() == null){
				try {
					resultChecker.getSemaphore().wait();
				} catch (InterruptedException e) {
				}
			}
		}
		return(resultChecker.getResults());
	}
}
