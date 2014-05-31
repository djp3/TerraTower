package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class TTEvent {
	public enum TTEventType{
		VOID,
		CREATE_WORLD,
		CREATE_MAP,
		CREATE_PLAYER;
		
		public static String toString(TTEventType x){
			switch (x){
				case CREATE_WORLD: return "CREATE_WORLD";
				case CREATE_MAP: return "CREATE_MAP";
				case CREATE_PLAYER: return "CREATE_PLAYER";
				default :return "VOID";
			}
		}
		
		static public TTEventType fromString(String x){
			switch (x){
				case "CREATE_WORLD": return CREATE_WORLD;
				case "CREATE_MAP": return CREATE_MAP;
				case "CREATE_PLAYER": return CREATE_PLAYER;
				default: return VOID;
			}
		}
	}
	
	private TTEventType eventType;
	
	public TTEventType getEventType() {
		return eventType;
	}

	public void setEventType(TTEventType eventType) {
		this.eventType = eventType;
	}

	
	TTEvent(TTEventType eventType){
		this.eventType = eventType;
	}
	
	/**
	 * This needs to be overridden by child classes
	 * @param ttEvent
	 */
	void set(TTEvent ttEvent){
		this.setEventType(ttEvent.getEventType());
	}
	
	@Override
	public String toString(){
		return(TTEvent.class.getCanonicalName()+":"+this.eventType.toString());
	}
	
	/**
	 * This needs to be overridden by child classes
	 * @param ttEvent
	 */
	public JSONObject toJSON(){
		JSONObject ret = new JSONObject();
		ret.put("eventType",getEventType().toString());
		return ret;
	}
	
	
	/**
	 * This needs to be overridden by child classes
	 * @param ttEvent
	 */
	static public TTEvent fromJSON(JSONObject in){
		TTEventType eventType = TTEventType.fromString((String) in.get("eventType"));
		TTEvent ret = new TTEvent(eventType);
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof TTEvent)) {
			return false;
		}
		TTEvent other = (TTEvent) obj;
		if (eventType != other.eventType) {
			return false;
		}
		return true;
	}
}
