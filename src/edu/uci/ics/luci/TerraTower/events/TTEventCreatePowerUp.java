/*
	Copyright 2014
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
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;

public class TTEventCreatePowerUp extends TTEvent{
	
	private PowerUp powerUp;
	
	public PowerUp getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(PowerUp pu) {
		this.powerUp = pu;
	}

	public TTEventCreatePowerUp(String worldName, String worldPassword, PowerUp pup){
		this(worldName,PasswordUtils.hashPassword(worldPassword), pup);
	}
	
	public TTEventCreatePowerUp(String worldName, byte[] worldHashedPassword,PowerUp pup){
		super(worldName,worldHashedPassword);
		this.setPowerUp(pup);
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("power_up", this.getPowerUp().toJSON());
		return ret;
	}
	
	static public TTEventCreatePowerUp fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		JSONObject _powerUp = (JSONObject) in.get("power_up");
		if(_powerUp == null){
			return(new TTEventCreatePowerUp(worldName,worldHashedPassword,null));
		}
		else{
			PowerUp pup = PowerUp.fromJSON(_powerUp);
			return(new TTEventCreatePowerUp(worldName,worldHashedPassword,pup));
		}
	}


}
