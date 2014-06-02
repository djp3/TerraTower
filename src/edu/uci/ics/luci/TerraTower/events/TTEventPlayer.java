package edu.uci.ics.luci.TerraTower.events;

import java.util.Arrays;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

import net.minidev.json.JSONObject;

public class TTEventPlayer extends TTEvent {
	
	private String playerName;
	private byte[] playerHashedPassword;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public byte[] getPlayerHashedPassword() {
		return Arrays.copyOf(playerHashedPassword,playerHashedPassword.length);
	}
	
	private void clearPlayerHashedPassword(){
		this.playerHashedPassword = null;
	}
	
	private void setPlayerHashedPassword(byte[] playerHashedPassword) {
		if(playerHashedPassword == null){
			this.playerHashedPassword = new byte[0];
		}
		else{
			this.playerHashedPassword = Arrays.copyOf(playerHashedPassword,playerHashedPassword.length);
		}
	}
	

	public void setPlayerPassword(String playerPassword) {
		if(playerPassword == null){
			this.clearPlayerHashedPassword();
		}
		else{
			this.setPlayerHashedPassword(PasswordUtils.hashPassword(playerPassword));
		}
	}
	
	
	public TTEventPlayer(String worldName, String worldPassword,String playerName, String playerPassword){
		this(worldName,PasswordUtils.hashPassword(worldPassword),playerName,PasswordUtils.hashPassword(playerPassword));
	}
	
	public TTEventPlayer(String worldName, byte[] worldHashedPassword,String playerName, byte[] playerHashedPassword){
		super(worldName,worldHashedPassword);
		this.setPlayerName(playerName);
		this.setPlayerHashedPassword(playerHashedPassword);
	}

	
	public JSONObject toJSON(){
		JSONObject ret = super.toJSON();
		ret.put("player_name", this.getPlayerName());
		ret.put("player_hashed_password", PasswordUtils.bytesToHexString(this.getPlayerHashedPassword()));
		return ret;
	}
	
	static public TTEventPlayer fromJSON(JSONObject in) {
		TTEvent parent = TTEvent.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		String playerName = (String) in.get("player_name");
		
		byte[] playerHashedPassword = new byte[0];
		String _playerHashedPassword = (String) in.get("player_hashed_password");
		if(_playerHashedPassword != null){
			playerHashedPassword = PasswordUtils.hexStringToByteArray((String) in.get("player_hashed_password"));
		}
		
		return(new TTEventPlayer(worldName,worldHashedPassword,playerName,playerHashedPassword));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(playerHashedPassword);
		result = prime * result
				+ ((playerName == null) ? 0 : playerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TTEventPlayer)) {
			return false;
		}
		TTEventPlayer other = (TTEventPlayer) obj;
		if (!Arrays.equals(playerHashedPassword, other.playerHashedPassword)) {
			return false;
		}
		if (playerName == null) {
			if (other.playerName != null) {
				return false;
			}
		} else if (!playerName.equals(other.playerName)) {
			return false;
		}
		return true;
	}
	
}
