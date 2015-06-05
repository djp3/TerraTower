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
package edu.uci.ics.luci.TerraTower.gameElements;


public class Tower {
	
	private Player owner;
	private int x;
	private int y;
	private boolean destroyed = false;
	
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

	public synchronized  boolean isDestroyed() {
		return destroyed;
	}

	public synchronized  void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public Tower(Player owner,int x, int y){
		this.setOwner(owner);
		this.setX(x);
		this.setY(y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (destroyed ? 1231 : 1237);
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + x;
		result = prime * result + y;
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
		if (!(obj instanceof Tower)) {
			return false;
		}
		Tower other = (Tower) obj;
		if (destroyed != other.destroyed) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	



}
