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

public class Bomb {
	
	Player owner;
	int x;
	int y;
	long explosionTime;
	int strength;
	boolean exploded;
	
	public synchronized  Player getOwner() {
		return owner;
	}

	public synchronized  void setOwner(Player owner) {
		this.owner = owner;
	}

	public synchronized  int getX() {
		return x;
	}

	public synchronized  void setX(int x) {
		this.x = x;
	}

	public synchronized  int getY() {
		return y;
	}

	public synchronized  void setY(int y) {
		this.y = y;
	}

	public synchronized  long getExplosionTime() {
		return explosionTime;
	}

	public synchronized void setExplosionTime(long explosionTime) {
		this.explosionTime = explosionTime;
	}

	public synchronized  int getStrength() {
		return strength;
	}

	public synchronized void setStrength(int strength) {
		this.strength = strength;
	}

	public synchronized  boolean isExploded() {
		return exploded;
	}

	public synchronized  void setExploded(boolean exploded) {
		this.exploded = exploded;
	}

	public Bomb(Player owner,int x, int y,long explosionTime,int strength){
		this.setOwner(owner);
		this.setX(x);
		this.setY(y);
		this.setExplosionTime(explosionTime);
		this.setStrength(strength);
		this.setExploded(false);
	}

	@Override
	public synchronized  int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exploded ? 1231 : 1237);
		result = prime * result
				+ (int) (explosionTime ^ (explosionTime >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + strength;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Bomb)) {
			return false;
		}
		Bomb other = (Bomb) obj;
		if (exploded != other.isExploded()) {
			return false;
		}
		if (explosionTime != other.getExplosionTime()) {
			return false;
		}
		if (owner == null) {
			if (other.getOwner() != null) {
				return false;
			}
		} else if (!owner.equals(other.getOwner())) {
			return false;
		}
		if (strength != other.getStrength()) {
			return false;
		}
		if (x != other.getX()) {
			return false;
		}
		if (y != other.getY()) {
			return false;
		}
		return true;
	}



}
