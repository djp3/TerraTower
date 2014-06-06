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

public class PowerUp {
	
	String code;
	long towerDelayDelta; // negative is faster regeneration for towers
	long bombDelayDelta; // negative is faster regeneration for bom
	long bombFuseDelta; // negative is faster fuse for bombs
	
	
	public PowerUp(String code, long towerDelayDelta, long bombDelayDelta, long bombFuseDelta) {
		this.setCode(code);
		this.setTowerDelayDelta(towerDelayDelta);
		this.setBombDelayDelta(bombDelayDelta);
		this.setBombFuseDelta(bombFuseDelta);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public long getTowerDelayDelta() {
		return towerDelayDelta;
	}
	public void setTowerDelayDelta(long towerDelayDelta) {
		this.towerDelayDelta = towerDelayDelta;
	}
	public long getBombDelayDelta() {
		return bombDelayDelta;
	}
	public void setBombDelayDelta(long bombDelayDelta) {
		this.bombDelayDelta = bombDelayDelta;
	}
	public long getBombFuseDelta() {
		return bombFuseDelta;
	}
	public void setBombFuseDelta(long bombFuseDelta) {
		this.bombFuseDelta = bombFuseDelta;
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
		if (towerDelayDelta != other.towerDelayDelta) {
			return false;
		}
		return true;
	}
	
	
	

}
