package edu.uci.ics.luci.TerraTower;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.luci.TerraTower.gameEvents.TTEventCreateMap;
import edu.uci.ics.luci.TerraTower.gameEvents.TTEventCreateWorld;

import net.minidev.json.JSONObject;

/**
 * Setting the eventType and the event is required
 * @author djp3
 *
 */
public class TTEventWrapper {
	
	/* The basic data encapsulated by the wrapper */
	private TTEventType eventType;
	private TTEvent event;
	private TTEventHandler handler;
	private List<TTEventHandlerResultListener> resultListeners;
	
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
		resetEventHandler();
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
		case CREATE_PLAYER: this.setHandler(new TTEventHandlerCreatePlayer());
			break;
		case VOID: this.setHandler(null);
			break;
		default:
			break;
		
		}
	}
	
	public void checkConsistency(){
		boolean problem = false;
		switch(this.getEventType()){
		case CREATE_MAP: problem = (!(this.getEvent() instanceof TTEventCreateMap));
			break;
		case CREATE_PLAYER: problem = (!(this.getEvent() instanceof TTEventCreatePlayer));
			break;
		case CREATE_WORLD: problem = (!(this.getEvent() instanceof TTEventCreateWorld));
			break;
		case VOID: problem = (!(this.getEvent() instanceof TTEventVoid));
			break;
		default:
			break;problem = true;}
		if(problem){
			throw new IllegalArgumentException("EventType:"+this.getEventType()+" is inconsistent with Event:"+this.getEvent().getClass().getCanonicalName());
		}
	}
	
	public List<TTEventHandlerResultListener>getResultListeners(){
		return this.resultListeners;
	}
	
	public void addResultListener(TTEventHandlerResultListener rl){
		if(rl != null){
			resultListeners.add(rl);
		}
	}
	
	public void setResultListeners(List<TTEventHandlerResultListener> rl){
		this.resultListeners = rl;
	}
	
	
	
	/**
	 *	Constructor 
	 */
	TTEventWrapper(TTEventType eventType,TTEvent event,TTEventHandlerResultListener resultListener){
		if(eventType == null){
			throw new IllegalArgumentException("eventType can't be null");
		}
		this.setEventType(eventType);
		
		//Event can be null because it has to be initialized for disruptor
		//before an event exists
		this.setEvent(event);
		
		this.setResultListeners(new ArrayList<TTEventHandlerResultListener>());
		this.addResultListener(resultListener);
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
		this.setEventType(eventType);
		
		//Event can be null because it has to be initialized for disruptor
		//before an event exists
		this.setEvent(event);
		
		if(resultListeners == null){
			this.setResultListeners(new ArrayList<TTEventHandlerResultListener>());
		}
		else{
			this.setResultListeners(resultListeners);
		}
	}
	
	void set(TTEventWrapper ttEventWrapper){
		this.setEventType(ttEventWrapper.getEventType());
		TTEvent event = ttEventWrapper.getEvent();
		this.setEvent(event);
		this.setResultListeners(ttEventWrapper.getResultListeners());
		checkConsistency();
	}
	
	@Override
	public String toString(){
		String localEventType = "";
		String localEvent = "";
		if(this.getEventType() == null){
			localEventType = "null";
		}
		if(this.getEvent() == null){
			localEvent = "null";
		}
		return(localEventType+":"+localEvent);
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
			case VOID: event = TTEventVoid.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_WORLD: event = TTEventCreateWorld.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_MAP: event = TTEventCreateMap.fromJSON((JSONObject)in.get("event"));
				break;
			case CREATE_PLAYER: event = TTEventCreatePlayer.fromJSON((JSONObject)in.get("event"));
				break;
			default:event = null;
				break;
		}
		TTEventWrapper ret = new TTEventWrapper(eventType,event,(TTEventHandlerResultListener)null);
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
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result
				+ ((resultListeners == null) ? 0 : resultListeners.hashCode());
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
		if (handler == null) {
			if (other.handler != null) {
				return false;
			}
		} else if (!handler.equals(other.handler)) {
			return false;
		}
		if (resultListeners == null) {
			if (other.resultListeners != null) {
				return false;
			}
		} else if (!resultListeners.equals(other.resultListeners)) {
			return false;
		}
		return true;
	}


}
