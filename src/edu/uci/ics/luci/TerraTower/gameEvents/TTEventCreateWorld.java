package edu.uci.ics.luci.TerraTower.gameEvents;

import edu.uci.ics.luci.TerraTower.TTEvent;
import net.minidev.json.JSONObject;

public class TTEventCreateWorld implements TTEvent {

	private String name;
	private String password;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public TTEventCreateWorld(String worldName, String password){
		this.setName(worldName);
		this.setPassword(password);
	}
	

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("name",getName());
		ret.put("password", getPassword());
		return ret;
	}
	
	static TTEventCreateWorld fromJSON(JSONObject in){
		String localName = (String) in.get("name");
		String localPassword = (String) in.get("password");
		TTEventCreateWorld ret = new TTEventCreateWorld(localName,localPassword);
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
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
		if (!(obj instanceof TTEventCreateWorld)) {
			return false;
		}
		TTEventCreateWorld other = (TTEventCreateWorld) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		return true;
	}
}
