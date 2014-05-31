package edu.uci.ics.luci.TerraTower;

public class TTEvent {
	public enum TTEventType{
		VOID,
		CREATE_WORLD,
		CREATE_MAP,
		CREATE_PLAYER
	}
	
	private TTEventType event;
	
	/**
	 * @return the event
	 */
	public TTEventType getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(TTEventType event) {
		this.event = event;
	}

	TTEvent(TTEventType type){
		this.event = type;
	}
	
	/**
	 * This needs to be overridden
	 * @param ttEvent
	 */
	void set(TTEvent ttEvent){
		this.event = ttEvent.getEvent();
	}
	
	@Override
	public String toString(){
		return(TTEvent.class.getCanonicalName()+":"+this.event.toString());
	}
}
