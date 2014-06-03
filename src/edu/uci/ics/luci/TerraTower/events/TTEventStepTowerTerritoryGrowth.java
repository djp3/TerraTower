package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventStepTowerTerritoryGrowth extends TTEvent{
	
	public TTEventStepTowerTerritoryGrowth(String worldName, String worldPassword){
		this(worldName,PasswordUtils.hashPassword(worldPassword));
	}
	
	public TTEventStepTowerTerritoryGrowth(String worldName, byte[] worldHashedPassword){
		super(worldName,worldHashedPassword);
	}

	@Override
	public JSONObject toJSON() {
		return (super.toJSON());
	}
	
	static public TTEventStepTowerTerritoryGrowth fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		return(new TTEventStepTowerTerritoryGrowth(worldName,worldHashedPassword));
	}

	
}
