package edu.uci.ics.luci.TerraTower.events;

public enum TTEventType{
	VOID,
	CREATE_WORLD,
	CREATE_TERRITORY,
	CREATE_PLAYER,
	BUILD_TOWER,
	STEP_TOWER_TERRITORY_GROWTH,
	DROP_BOMB,
	BURN_FUSE;
	
	public static String toString(TTEventType x){
		switch (x){
			case CREATE_WORLD: return "CREATE_WORLD";
			case CREATE_TERRITORY: return "CREATE_TERRITORY";
			case CREATE_PLAYER: return "CREATE_PLAYER";
			case BUILD_TOWER: return "BUILD_TOWER";
			case STEP_TOWER_TERRITORY_GROWTH: return "STEP_TOWER_TERRITORY_GROWTH";
			case BURN_FUSE: return "BURN_FUSE";
			case DROP_BOMB: return "DROP_BOMB";
			default: return "VOID";
		}
	}
	
	static public TTEventType fromString(String x){
		switch (x){
			case "CREATE_WORLD": return CREATE_WORLD;
			case "CREATE_TERRITORY": return CREATE_TERRITORY;
			case "CREATE_PLAYER": return CREATE_PLAYER;
			case "BUILD_TOWER": return BUILD_TOWER;
			case "STEP_TOWER_TERRITORY_GROWTH": return STEP_TOWER_TERRITORY_GROWTH;
			case "BURN_FUSE": return BURN_FUSE;
			case "DROP_BOMB": return DROP_BOMB;
			default: return VOID;
		}
	}
}