package edu.uci.ics.luci.TerraTower.events;

public enum TTEventType{
	VOID,
	CREATE_WORLD,
	CREATE_TERRITORY,
	CREATE_PLAYER,
	CREATE_POWER_UP,
	BUILD_TOWER,
	STEP_TOWER_TERRITORY_GROWTH,
	DROP_BOMB,
	BURN_BOMB_FUSE,
	REDEEM_POWER_UP;
	
	public static String toString(TTEventType x){
		switch (x){
			case BUILD_TOWER: return "BUILD_TOWER";
			case BURN_BOMB_FUSE: return "BURN_BOMB_FUSE";
			case CREATE_PLAYER: return "CREATE_PLAYER";
			case CREATE_POWER_UP: return "CREATE_POWER_UP";
			case CREATE_TERRITORY: return "CREATE_TERRITORY";
			case CREATE_WORLD: return "CREATE_WORLD";
			case DROP_BOMB: return "DROP_BOMB";
			case STEP_TOWER_TERRITORY_GROWTH: return "STEP_TOWER_TERRITORY_GROWTH";
			case REDEEM_POWER_UP: return "REDEEM_POWER_UP";
			default: return "VOID";
		}
	}
	
	static public TTEventType fromString(String x){
		switch (x){
			case "BUILD_TOWER": return BUILD_TOWER;
			case "BURN_BOMB_FUSE": return BURN_BOMB_FUSE;
			case "CREATE_PLAYER": return CREATE_PLAYER;
			case "CREATE_POWER_UP": return CREATE_POWER_UP;
			case "CREATE_TERRITORY": return CREATE_TERRITORY;
			case "CREATE_WORLD": return CREATE_WORLD;
			case "DROP_BOMB": return DROP_BOMB;
			case "REDEEM_POWER_UP": return REDEEM_POWER_UP;
			case "STEP_TOWER_TERRITORY_GROWTH": return STEP_TOWER_TERRITORY_GROWTH;
			default: return VOID;
		}
	}
}