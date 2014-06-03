package edu.uci.ics.luci.TerraTower.events;

public enum TTEventType{
	VOID,
	CREATE_WORLD,
	CREATE_TERRITORY,
	CREATE_PLAYER,
	PLACE_TOWER,
	STEP_TOWER_TERRITORY_GROWTH;
	
	public static String toString(TTEventType x){
		switch (x){
			case CREATE_WORLD: return "CREATE_WORLD";
			case CREATE_TERRITORY: return "CREATE_TERRITORY";
			case CREATE_PLAYER: return "CREATE_PLAYER";
			case PLACE_TOWER: return "PLACE_TOWER";
			case STEP_TOWER_TERRITORY_GROWTH: return "STEP_TOWER_TERRITORY_GROWTH";
			default: return "VOID";
		}
	}
	
	static public TTEventType fromString(String x){
		switch (x){
			case "CREATE_WORLD": return CREATE_WORLD;
			case "CREATE_TERRITORY": return CREATE_TERRITORY;
			case "CREATE_PLAYER": return CREATE_PLAYER;
			case "PLACE_TOWER": return PLACE_TOWER;
			case "STEP_TOWER_TERRITORY_GROWTH": return STEP_TOWER_TERRITORY_GROWTH;
			default: return VOID;
		}
	}
}