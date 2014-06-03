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

import java.util.Arrays;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class Player {
	
	String playerName;
	byte[] hashedPassword;
	
	//How long this player must wait to place a new tower
	long towerDelay;
	//When the last tower was placed
	long lastTowerPlacedTime;
	
	//How long this player must wait to place a new Bomb
	long bombDelay;
	//When the last bomb placed
	long lastBombPlacedTime;
	
	
	public String getPlayerName() {
		return playerName;
	}


	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public byte[] getHashedPassword() {
		return(Arrays.copyOf(hashedPassword,hashedPassword.length));
	}


	private void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = Arrays.copyOf(hashedPassword,hashedPassword.length);
		
	}


	public long getBombDelay() {
		return bombDelay;
	}


	public void setBombDelay(long bombDelay) {
		this.bombDelay = bombDelay;
	}


	public long getLastBombPlacedTime() {
		return lastBombPlacedTime;
	}


	public void setLastBombPlacedTime(long lastBombPlacedTime) {
		this.lastBombPlacedTime = lastBombPlacedTime;
	}
	
	public long getTowerDelay() {
		return towerDelay;
	}


	public void setTowerDelay(long towerDelay) {
		this.towerDelay = towerDelay;
	}


	public long getLastTowerPlacedTime() {
		return lastTowerPlacedTime;
	}


	public void setLastTowerPlacedTime(long lastTowerPlacedTime) {
		this.lastTowerPlacedTime = lastTowerPlacedTime;
	}



	public Player(String playerName,byte[] hashedPassword){
		this.setPlayerName(playerName);
		this.setHashedPassword(hashedPassword);
		this.setTowerDelay(GlobalsTerraTower.DEFAULT_TOWER_DELAY);
		this.setLastTowerPlacedTime(0);
		this.setBombDelay(GlobalsTerraTower.DEFAULT_BOMB_DELAY);
		this.setLastBombPlacedTime(0);
	}





	
	public boolean passwordGood(String proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,hashedPassword));
	}
	

	public boolean passwordGood(byte[] proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,hashedPassword));
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bombDelay ^ (bombDelay >>> 32));
		result = prime * result + Arrays.hashCode(hashedPassword);
		result = prime * result
				+ (int) (lastBombPlacedTime ^ (lastBombPlacedTime >>> 32));
		result = prime * result
				+ (int) (lastTowerPlacedTime ^ (lastTowerPlacedTime >>> 32));
		result = prime * result
				+ ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + (int) (towerDelay ^ (towerDelay >>> 32));
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
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		if (bombDelay != other.bombDelay) {
			return false;
		}
		if (!Arrays.equals(hashedPassword, other.hashedPassword)) {
			return false;
		}
		if (lastBombPlacedTime != other.lastBombPlacedTime) {
			return false;
		}
		if (lastTowerPlacedTime != other.lastTowerPlacedTime) {
			return false;
		}
		if (playerName == null) {
			if (other.playerName != null) {
				return false;
			}
		} else if (!playerName.equals(other.playerName)) {
			return false;
		}
		if (towerDelay != other.towerDelay) {
			return false;
		}
		return true;
	}
	
	
	
	

}
