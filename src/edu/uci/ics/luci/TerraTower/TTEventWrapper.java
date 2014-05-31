package edu.uci.ics.luci.TerraTower;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

public class TTEventWrapper {
	
	/* The basic data encapsulated by the wrapper */
	private TTEventType eventType;
	private TTEvent event;
	private TTEventHandler handler;
	private transient List<TTEventHandlerResultListener> resultListeners;
	
	/* Getters and Setters */
	public TTEventType getEventType() {
		return eventType;
	}

	public void setEventType(TTEventType eventType) {
		this.eventType = eventType;
	}
	
	public TTEvent getEvent(){
		return event;
	}
	
	public void setEvent(TTEvent event){
		this.event = event;
	}
	
	public TTEventHandler getHandler(){
		return handler;
	}
	
	private void setHandler(TTEventHandler handler){
		this.handler = handler;
	}
	
	public void resetEventHandler(){
		switch(this.getEventType()){
		case CREATE_WORLD: this.setHandler(new TTEventHandlerCreateWorld());
			break;
		case CREATE_MAP: this.setHandler(new TTEventHandlerCreateMap());
			break;
			/*
		case CREATE_PLAYER: this.setHandler(new TTEventHandlerCreatePlayer());
			break;
			*/
		case VOID: this.setHandler(null);
			break;
		default:
			break;
		
		}
	}
	
	public List<TTEventHandlerResultListener>getResultListeners(){
		return this.resultListeners;
	}
	
	public void addResultListener(TTEventHandlerResultListener rl){
		resultListeners.add(rl);
	}
	
	public void setResultListeners(List<TTEventHandlerResultListener> rl){
		this.resultListeners = rl;
	}

	
	/**
	 *	Constructor 
	 * @param eventType
	 * @param event
	 */
	TTEventWrapper(TTEventType eventType,TTEvent event,List<TTEventHandlerResultListener> resultListeners){
		if(eventType == null){
			throw new IllegalArgumentException("eventType can't be null");
		}
		this.eventType = eventType;
		
		//Event can be null because it has to be initialized for disruptor
		//before an event exists
		this.event = event;
		resetEventHandler();
		
		if(resultListeners == null){
			this.resultListeners = new ArrayList<TTEventHandlerResultListener>();
		}
		else{
			this.resultListeners = resultListeners;
		}
	}
	
	void set(TTEventWrapper ttEventWrapper){
		this.setEventType(ttEventWrapper.getEventType());
		TTEvent event = ttEventWrapper.getEvent();
		if(event == null){
			throw new IllegalArgumentException("event can't be null");
		}
		this.setEvent(event);
		this.setResultListeners(ttEventWrapper.getResultListeners());
	}
	
	@Override
	public String toString(){
		return(this.getEventType().toString()+":"+this.getEvent().toString());
	}
	
	public JSONObject toJSON(){
		JSONObject ret = new JSONObject();
		ret.put("eventType",getEventType().toString());
		ret.put("event", getEvent().toJSON());
		return ret;
	}
	
	static public TTEventWrapper fromJSON(JSONObject in){
		TTEventType eventType = TTEventType.fromString((String) in.get("eventType"));
		TTEvent event;
		switch(eventType){
			case CREATE_WORLD: event = TTEventCreateWorld.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_MAP: event = TTEventCreateMap.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_PLAYER: event = TTEventCreatePlayer.fromJSON((JSONObject)in.get("event"));
				break;
			default:event = null;
				break;
		}
		TTEventWrapper ret = new TTEventWrapper(eventType,event,null);
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TTEventWrapper)) {
			return false;
		}
		TTEventWrapper other = (TTEventWrapper) obj;
		if (event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!event.equals(other.event)) {
			return false;
		}
		if (eventType != other.eventType) {
			return false;
		}
		return true;
	}

}
