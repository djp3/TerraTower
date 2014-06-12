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
import java.util.Random;

import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class Player implements Comparable<Player>{
	
	private static Random r;
	public static final Player BARBARIAN;
	
	static{
		r = new Random();
		BARBARIAN = new Player("barbarian",PasswordUtils.hashPassword("barbarian password"+r.nextInt()));
	}
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
	
	//How long till a bomb blows?
	long bombFuse;
	
	
	public synchronized  String getPlayerName() {
		return playerName;
	}


	public synchronized  void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public synchronized  byte[] getHashedPassword() {
		return(Arrays.copyOf(hashedPassword,hashedPassword.length));
	}


	private synchronized void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = Arrays.copyOf(hashedPassword,hashedPassword.length);
	}


	public synchronized  long getBombDelay() {
		return bombDelay;
	}


	public synchronized  void setBombDelay(long bombDelay) {
		this.bombDelay = bombDelay;
	}


	public synchronized  long getLastBombPlacedTime() {
		return lastBombPlacedTime;
	}


	public synchronized  void setLastBombPlacedTime(long lastBombPlacedTime) {
		this.lastBombPlacedTime = lastBombPlacedTime;
	}
	
	public synchronized  long getTowerDelay() {
		return towerDelay;
	}


	public synchronized  void setTowerDelay(long towerDelay) {
		this.towerDelay = towerDelay;
	}


	public synchronized  long getLastTowerPlacedTime() {
		return lastTowerPlacedTime;
	}


	public synchronized  void setLastTowerPlacedTime(long lastTowerPlacedTime) {
		this.lastTowerPlacedTime = lastTowerPlacedTime;
	}
	

	public synchronized  long getBombFuse() {
		return bombFuse;
	}


	public synchronized  void setBombFuse(long bombFuse) {
		this.bombFuse = bombFuse;
	}



	public Player(String playerName,byte[] hashedPassword){
		this.setPlayerName(playerName);
		this.setHashedPassword(hashedPassword);
		this.setTowerDelay(GlobalsTerraTower.DEFAULT_TOWER_DELAY);
		this.setLastTowerPlacedTime(0);
		this.setBombDelay(GlobalsTerraTower.DEFAULT_BOMB_DELAY);
		this.setLastBombPlacedTime(0);
		this.setBombFuse(GlobalsTerraTower.DEFAULT_BOMB_FUSE);
	}





	
	public synchronized  boolean passwordGood(String proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,hashedPassword));
	}
	

	public synchronized  boolean passwordGood(byte[] proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,hashedPassword));
	}


	@Override
	public synchronized  int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bombDelay ^ (bombDelay >>> 32));
		result = prime * result + (int) (bombFuse ^ (bombFuse >>> 32));
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
	public synchronized  boolean equals(Object obj) {
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
		if (bombDelay != other.getBombDelay()) {
			return false;
		}
		if (bombFuse != other.getBombFuse()) {
			return false;
		}
		if (!Arrays.equals(hashedPassword, other.getHashedPassword())) {
			return false;
		}
		if (lastBombPlacedTime != other.getLastBombPlacedTime()) {
			return false;
		}
		if (lastTowerPlacedTime != other.getLastTowerPlacedTime()) {
			return false;
		}
		if (playerName == null) {
			if (other.getPlayerName() != null) {
				return false;
			}
		} else if (!playerName.equals(other.getPlayerName())) {
			return false;
		}
		if (towerDelay != other.getTowerDelay()) {
			return false;
		}
		return true;
	}


	@Override
	public synchronized  int compareTo(Player o) {
		int x;
		
		if(this.getPlayerName() == null){
			if( o.getPlayerName() == null){
				x = 0;
			}
			else{
				x = 1;
			}
		}
		else{
			if(o.getPlayerName() == null){
				x = -1;
			}
			else{
				x = this.getPlayerName().compareTo(o.getPlayerName());
				if(x == 0){
					long a = this.getBombDelay()+this.getTowerDelay();
					long b = o.getBombDelay()+o.getTowerDelay();
					return (int) (a - b);
				}
			}
		}
		return x;
	}

}
