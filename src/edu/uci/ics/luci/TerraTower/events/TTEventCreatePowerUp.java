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

public class TTEventCreatePowerUp extends TTEvent{
	
	private String code;
	private long towerDelayDelta;
	private long bombDelayDelta;
	private long bombFuseDelta;
	

	public synchronized String getCode() {
		return code;
	}

	public synchronized void setCode(String code) {
		this.code = code;
	}

	public synchronized long getTowerDelayDelta() {
		return towerDelayDelta;
	}

	public synchronized void setTowerDelayDelta(long towerDelayDelta) {
		this.towerDelayDelta = towerDelayDelta;
	}

	public synchronized long getBombDelayDelta() {
		return bombDelayDelta;
	}

	public synchronized void setBombDelayDelta(long bombDelayDelta) {
		this.bombDelayDelta = bombDelayDelta;
	}

	public synchronized long getBombFuseDelta() {
		return bombFuseDelta;
	}

	public synchronized void setBombFuseDelta(long bombFuseDelta) {
		this.bombFuseDelta = bombFuseDelta;
	}

	public TTEventCreatePowerUp(String worldName, String worldPassword, String code, long towerDelayDelta, long bombDelayDelta, long bombFuseDelta){
		this(worldName,PasswordUtils.hashPassword(worldPassword), code,towerDelayDelta,bombDelayDelta,bombFuseDelta);
	}
	
	public TTEventCreatePowerUp(String worldName, byte[] worldHashedPassword, String code, long towerDelayDelta, long bombDelayDelta, long bombFuseDelta){
		super(worldName,worldHashedPassword);
		this.setCode(code);
		this.setTowerDelayDelta(towerDelayDelta);
		this.setBombDelayDelta(bombDelayDelta);
		this.setBombFuseDelta(bombFuseDelta);
	}

	@Override
	public synchronized JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("code", this.getCode());
		ret.put("tower_delay_delta", this.getTowerDelayDelta()+"");
		ret.put("bomb_delay_delta", this.getBombDelayDelta()+"");
		ret.put("bomb_fuse_delta", this.getBombFuseDelta()+"");
		return ret;
	}
	
	static public synchronized TTEventCreatePowerUp fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		
		String code =  (String) in.get("code");
		long towerDelayDelta = 0L;
		String _towerDelayDelta =  (String) in.get("tower_delay_delta");
		if(_towerDelayDelta != null){
			try{
				towerDelayDelta = Long.parseLong(_towerDelayDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		long bombDelayDelta = 0L;
		String _bombDelayDelta =  (String) in.get("bomb_delay_delta");
		if(_bombDelayDelta != null){
			try{
				bombDelayDelta = Long.parseLong(_bombDelayDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		long bombFuseDelta = 0L;
		String _bombFuseDelta =  (String) in.get("bomb_fuse_delta");
		if(_bombFuseDelta != null){
			try{
				bombFuseDelta = Long.parseLong(_bombFuseDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		return(new TTEventCreatePowerUp(worldName,worldHashedPassword,code,towerDelayDelta,bombDelayDelta,bombFuseDelta));
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (int) (bombDelayDelta ^ (bombDelayDelta >>> 32));
		result = prime * result
				+ (int) (bombFuseDelta ^ (bombFuseDelta >>> 32));
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result
				+ (int) (towerDelayDelta ^ (towerDelayDelta >>> 32));
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TTEventCreatePowerUp)) {
			return false;
		}
		TTEventCreatePowerUp other = (TTEventCreatePowerUp) obj;
		if (bombDelayDelta != other.getBombDelayDelta()) {
			return false;
		}
		if (bombFuseDelta != other.getBombFuseDelta()) {
			return false;
		}
		if (code == null) {
			if (other.getCode()!= null) {
				return false;
			}
		} else if (!code.equals(other.getCode())) {
			return false;
		}
		if (towerDelayDelta != other.getTowerDelayDelta()) {
			return false;
		}
		return true;
	}



}
