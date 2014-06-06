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
package edu.uci.ics.luci.TerraTower.gameElements;

import net.minidev.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PowerUp {
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(PowerUp.class);
		}
		return log;
	}
	
	String code;
	long towerDelayDelta; // negative is faster regeneration for towers
	long bombDelayDelta; // negative is faster regeneration for bom
	long bombFuseDelta; // negative is faster fuse for bombs
	boolean redeemed;  //false until used
	
	
	public PowerUp(String code, Long towerDelayDelta, Long bombDelayDelta, Long bombFuseDelta,boolean redeemed) {
		this.setCode(code);
		this.setTowerDelayDelta(towerDelayDelta);
		this.setBombDelayDelta(bombDelayDelta);
		this.setBombFuseDelta(bombFuseDelta);
		this.setRedeemed(redeemed);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		if(code == null){
			this.code = "";
			getLog().warn("Setting code to \"\" instead of null");
		}
		else{
			this.code = code;
		}
		
	}
	public long getTowerDelayDelta() {
		return towerDelayDelta;
	}
	public void setTowerDelayDelta(Long towerDelayDelta) {
		if(towerDelayDelta == null){
			this.towerDelayDelta = 0;
			getLog().warn("Setting tower delay delta to 0 instead of null");
		}
		else{
			this.towerDelayDelta = towerDelayDelta;
		}
	}
	public long getBombDelayDelta() {
		return bombDelayDelta;
	}
	public void setBombDelayDelta(Long bombDelayDelta) {
		if(bombDelayDelta == null){
			this.bombDelayDelta = 0;
			getLog().warn("Setting bomb delay delta to 0 instead of null");
		}
		else{
			this.bombDelayDelta = bombDelayDelta;
		}
	}
	public long getBombFuseDelta() {
		return bombFuseDelta;
	}
	public void setBombFuseDelta(Long bombFuseDelta) {
		if(bombFuseDelta == null){
			this.bombFuseDelta = 0;
			getLog().warn("Setting bomb fuse delta to 0 instead of null");
		}
		else{
			this.bombFuseDelta = bombFuseDelta;
		}
	}
	public boolean getRedeemed() {
		return redeemed;
	}
	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}
	
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("code", this.getCode());
		ret.put("tower_delay_delta", this.getTowerDelayDelta()+"");
		ret.put("bomb_delay_delta", this.getBombDelayDelta()+"");
		ret.put("bomb_fuse_delta", this.getBombFuseDelta()+"");
		ret.put("redeemed", this.getRedeemed()+"");
		return ret;
	}
	
	static public PowerUp fromJSON(JSONObject in) {
		String code =  (String) in.get("code");
		Long towerDelayDelta = null;
		String _towerDelayDelta =  (String) in.get("tower_delay_delta");
		if(_towerDelayDelta != null){
			try{
				towerDelayDelta = Long.parseLong(_towerDelayDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Long bombDelayDelta = null;
		String _bombDelayDelta =  (String) in.get("bomb_delay_delta");
		if(_bombDelayDelta != null){
			try{
				bombDelayDelta = Long.parseLong(_bombDelayDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Long bombFuseDelta = null;
		String _bombFuseDelta =  (String) in.get("bomb_fuse_delta");
		if(_bombFuseDelta != null){
			try{
				bombFuseDelta = Long.parseLong(_bombFuseDelta);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		boolean redeemed = false;
		String _redeemed =  (String) in.get("redeemed");
		if(_redeemed != null){
			if(_redeemed.toLowerCase().trim().equals("true")){
				redeemed = true;
			}
		}
		
		PowerUp pup = new PowerUp(code,towerDelayDelta,bombDelayDelta,bombFuseDelta,redeemed);
		return(pup);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (bombDelayDelta ^ (bombDelayDelta >>> 32));
		result = prime * result
				+ (int) (bombFuseDelta ^ (bombFuseDelta >>> 32));
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (redeemed ? 1231 : 1237);
		result = prime * result
				+ (int) (towerDelayDelta ^ (towerDelayDelta >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PowerUp)) {
			return false;
		}
		PowerUp other = (PowerUp) obj;
		if (bombDelayDelta != other.bombDelayDelta) {
			return false;
		}
		if (bombFuseDelta != other.bombFuseDelta) {
			return false;
		}
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		if (redeemed != other.redeemed) {
			return false;
		}
		if (towerDelayDelta != other.towerDelayDelta) {
			return false;
		}
		return true;
	}

	

}
