package edu.uci.ics.luci.TerraTower.gameEvents;

import java.util.Arrays;

import edu.uci.ics.luci.TerraTower.PasswordUtils;
import edu.uci.ics.luci.TerraTower.TTEvent;
import net.minidev.json.JSONObject;

public class TTEventCreatePlayer implements TTEvent{

	private String playerName;
	private byte[] hashedPassword;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public byte[] getHashedPassword() {
		return hashedPassword;
	}
	
	public void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword; 
	}

	public void setPassword(String password) {
		if(password == null){
			this.hashedPassword = null;
		}
		else{
			this.hashedPassword = PasswordUtils.hashPassword(password);
		}
	}

	public TTEventCreatePlayer(String playerName, String password){
		this.setPlayerName(playerName);
		this.setPassword(password);
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("player_name", this.getPlayerName());
		ret.put("hashed_password", this.getHashedPassword());
		return ret;
	}
	
	static public TTEventCreatePlayer fromJSON(JSONObject in) {
		String playerName = (String) in.get("player_name");
		String hashedPassword = (String) in.get("hashed_password");
		return(new TTEventCreatePlayer(playerName,hashedPassword));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(hashedPassword);
		result = prime * result
				+ ((playerName == null) ? 0 : playerName.hashCode());
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
		if (!(obj instanceof TTEventCreatePlayer)) {
			return false;
		}
		TTEventCreatePlayer other = (TTEventCreatePlayer) obj;
		if (!Arrays.equals(hashedPassword, other.hashedPassword)) {
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
