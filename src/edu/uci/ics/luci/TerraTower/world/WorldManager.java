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
package edu.uci.ics.luci.TerraTower.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.PasswordUtils;
import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;

public class WorldManager {
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(WorldManager.class);
		}
		return log;
	}
	
	
	//World password 
	byte[] worldHashedPassword;
	Territory territory = null;
	
	//Map from playerName to player
	Map<String,Player> players;
	
	List<Tower> towers;
	List<Bomb> bombs;
	Map<String, PowerUp> powerUps;

	
	public WorldManager(String password){
		this(PasswordUtils.hashPassword(password));
	}
	
	public WorldManager(byte[] hashedPassword){
		this.worldHashedPassword=Arrays.copyOf(hashedPassword,hashedPassword.length);
		
		players = Collections.synchronizedMap(new HashMap<String,Player>());
	
		towers = Collections.synchronizedList(new ArrayList<Tower>());
		bombs = Collections.synchronizedList(new ArrayList<Bomb>());
		
		powerUps = Collections.synchronizedMap(new HashMap<String,PowerUp>());
	}
	
	
	public synchronized  boolean passwordGood(String proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,worldHashedPassword));
	}

	public synchronized  boolean passwordGood(byte[] proposedPassword) {
		return(PasswordUtils.checkPassword(proposedPassword,worldHashedPassword));
	}

	public synchronized  void setTerritory(Territory t) {
		territory=t;
	}
	
	public synchronized  Territory getTerritory(){
		return territory;
	}

	public synchronized  boolean playerExists(String playerName) {
		Player player = players.get(playerName);
		return(player!=null);
	}

	public synchronized  Player createPlayer(String playerName, byte[] hashedPassword) {
		if(playerExists(playerName)){
			return null;
		}
		Player player = new Player(playerName,hashedPassword);
		players.put(playerName, player);
		return player;
	}
	
	public synchronized  Player getPlayer(String playerName,String password){
		Player player= players.get(playerName);
		if(player == null){
			return null;
		}
		if(!player.passwordGood(password)){
			return null;
		}
		return(player);
	}
	
	public synchronized  Player getPlayer(String playerName,byte[] hashedPassword){
		Player player= players.get(playerName);
		if(player == null){
			return null;
		}
		if(!player.passwordGood(hashedPassword)){
			return null;
		}
		return(player);
	}
	
	public synchronized  boolean towerPresent(int x,int y){
		return(territory.towerPresent(x,y));
	}
	
	public synchronized  boolean addTower(Tower tower){
		if(towerPresent(tower.getX(),tower.getY())){
			return false;
		}
		if(territory.addTower(tower)){
			towers.add(tower);
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public synchronized  boolean powerUpExists(String code) {
		return(powerUps.containsKey(code));
	}

	public synchronized  PowerUp createPowerUp(PowerUp pup){
		if(powerUpExists(pup.getCode())){
			return null;
		}
		powerUps.put(pup.getCode(), pup);
		return pup;
	}
	
	public synchronized  PowerUp getPowerUp(String code){
		PowerUp pup= powerUps.get(code);
		return(pup);
	}
	

	public synchronized  void stepTowerTerritoryGrowth(boolean withRandom) {
		getTerritory().stepTowerTerritoryGrowth(10,2,withRandom);
	}
	
	

	public synchronized  int numBombsPresent(int x,int y){
		return(territory.numBombsPresent(x,y));
	}
	
	public synchronized  boolean addBomb(Bomb bomb){
		return(territory.addBomb(bomb));
	}
	

	public synchronized  void burnBombFuse(long eventTime) {
		getTerritory().burnBombFuse(eventTime);
	}
	
}
