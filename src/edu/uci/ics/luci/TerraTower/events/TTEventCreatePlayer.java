package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventCreatePlayer extends TTEventPlayer{
	
	public TTEventCreatePlayer(String worldName, String worldPassword,String playerName, String playerPassword){
		this(worldName,PasswordUtils.hashPassword(worldPassword),playerName,PasswordUtils.hashPassword(playerPassword));
	}
	
	public TTEventCreatePlayer(String worldName, byte[] worldHashedPassword,String playerName, byte[] playerHashedPassword){
		super(worldName,worldHashedPassword,playerName,playerHashedPassword);
	}

	@Override
	public JSONObject toJSON() {
		return (super.toJSON());
	}
	
	static public TTEventCreatePlayer fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		String playerName = parent.getPlayerName();
		byte[] playerHashedPassword = parent.getPlayerHashedPassword();
		return(new TTEventCreatePlayer(worldName,worldHashedPassword,playerName,playerHashedPassword));
	}

	
}
