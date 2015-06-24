/*
	Copyright 2014-2015
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java TerraTower game, i.e. "TerraTower"

    TerraTower is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Utilities is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Utilities.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventRedeemPowerUp extends TTEventPlayer{
	
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TTEventRedeemPowerUp(String worldName, String worldPassword, String playerName, String playerPassword,String code){
		this(worldName,PasswordUtils.hashPassword(worldPassword), playerName,PasswordUtils.hashPassword(playerPassword),code);
	}
	
	public TTEventRedeemPowerUp(String worldName, byte[] worldHashedPassword,String playerName, byte[] playerHashedPassword,String code){
		super(worldName,worldHashedPassword,playerName,playerHashedPassword);
		this.setCode(code);
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("code", ""+this.getCode());
		return ret;
	}
	
	static public TTEventRedeemPowerUp fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		String playerName = parent.getPlayerName();
		byte[] playerHashedPassword = parent.getPlayerHashedPassword();
		String code =  (String) in.get("code");
		return(new TTEventRedeemPowerUp(worldName,worldHashedPassword,playerName,playerHashedPassword,code));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		if (!(obj instanceof TTEventRedeemPowerUp)) {
			return false;
		}
		TTEventRedeemPowerUp other = (TTEventRedeemPowerUp) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

}
