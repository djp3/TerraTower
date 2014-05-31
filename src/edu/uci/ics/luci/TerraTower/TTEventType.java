package edu.uci.ics.luci.TerraTower;

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