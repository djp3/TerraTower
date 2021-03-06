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
package edu.uci.ics.luci.TerraTower;


import java.util.HashMap;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.CalendarCache;
import edu.uci.ics.luci.utility.Globals;

public class GlobalsTerraTower extends Globals {
	
	static{
        /* Test that we are using GMT as the default */
        if(!TimeZone.getDefault().equals(CalendarCache.TZ_GMT)){
                throw new RuntimeException("We are in the wrong timezone:\n"+TimeZone.getDefault()+"\n We want to be in:\n "+CalendarCache.TZ_GMT);
        }

        /* Test that we are using UTF-8 as default */
        String c = java.nio.charset.Charset.defaultCharset().name();
        if(!c.equals("UTF-8")){
                throw new IllegalArgumentException("The character set is not UTF-8:"+c);
        }

	}
	
	private static final String LOG4J_CONFIG_FILE_DEFAULT = "TerraTower.log4j.xml";
	
	static final long ONE_SECOND = 1000;
	static final long ONE_MINUTE = 60 * ONE_SECOND;
	
	private long _DEFAULT_TOWER_DELAY = 5 * ONE_MINUTE;
	private long towerDelay = _DEFAULT_TOWER_DELAY;
	private long _DEFAULT_BOMB_DELAY = 5 * ONE_MINUTE;
	private long bombDelay = _DEFAULT_BOMB_DELAY;
	private long _DEFAULT_BOMB_FUSE = 5 * ONE_MINUTE;
	private long bombFuse = _DEFAULT_BOMB_FUSE;
	private int _DEFAULT_TOWER_STRENGTH = 10;
	private int towerStrength = _DEFAULT_TOWER_STRENGTH;

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(GlobalsTerraTower.class);
		}
		return log;
	}
	String version = null;
	
	HashMap<String,WorldManager> worlds = new HashMap<String,WorldManager>();

	@Override
	public String getSystemVersion() {
		return version;
	}
	
	public GlobalsTerraTower(String version){
		this(version,false);
	}
	
	public GlobalsTerraTower(String version, boolean testing){
		super();
		this.version = version;
		setTesting(testing);
		setLog4JPropertyFileName(LOG4J_CONFIG_FILE_DEFAULT);
	}
	
	public static GlobalsTerraTower getGlobalsTerraTower(){
		return (GlobalsTerraTower) Globals.getGlobals();
	}
	
	
	/* Tower Delay is the amount of time between successive placement of towers */
	public void setTowerDelay(long towerDelay){
		this.towerDelay = towerDelay;
	}
	
	public long getTowerDelay(){
		return towerDelay;
	}
	
	/* Tower Strength is the max land claim they can make */
	public void setTowerStrength(int towerStrength){
		this.towerStrength = towerStrength;
	}
	
	public int getTowerStrength(){
		return towerStrength;
	}
	
	/* Bomb Delay is the amount of time between successive placement of bombs */
	public void setBombDelay(long bombDelay){
		this.bombDelay = bombDelay;
	}
	
	public long getBombDelay(){
		return bombDelay;
	}
	
	/* Bomb Fuse is the amount of time before a bomb blows up */
	public void setBombFuse(long bombFuse){
		this.bombFuse = bombFuse;
	}
	
	public long getBombFuse(){
		return bombFuse;
	}
	
	
	
	
	

	public boolean createWorld(String worldName, String password) {
		if(!worldExists(worldName)){
			WorldManager wm = new WorldManager(password);
			worlds.put(worldName, wm);
			return worldExists(worldName);
		}
		else{
			return false;
		}
	}
	
	public boolean createWorld(String worldName, byte[] hashedPassword) {
		if(!worldExists(worldName)){
			WorldManager wm = new WorldManager(hashedPassword);
			worlds.put(worldName, wm);
			return worldExists(worldName);
		}
		else{
			return false;
		}
	}

	public boolean worldExists(String worldName) {
		return worlds.containsKey(worldName);
	}
	
	public void setWorld(String worldName,WorldManager wm){
		if(worldExists(worldName)){
			throw new IllegalArgumentException("World exists, not replacing");
		}
		else{
			this.worlds.put(worldName, wm);
		}
	}
	
	public WorldManager getWorld(String worldName,String password){
		WorldManager wm = this.worlds.get(worldName);
		if(wm == null){
			return null;
		}
		if(!wm.passwordGood(password)){
			return null;
		}
		return(wm);
	}
	
	public WorldManager getWorld(String worldName,byte[] password){
		WorldManager wm = this.worlds.get(worldName);
		if(wm == null){
			return null;
		}
		if(!wm.passwordGood(password)){
			return null;
		}
		return(wm);
	}
	
	public boolean clearWorld(String worldName,String password){
		WorldManager wm = this.worlds.get(worldName);
		if(wm == null){
			return true;
		}
		if(!wm.passwordGood(password)){
			return false;
		}
		this.worlds.remove(worldName);
		return true;
	}
	
	public boolean clearWorld(String worldName,byte[] password){
		WorldManager wm = this.worlds.get(worldName);
		if(wm == null){
			return true;
		}
		if(!wm.passwordGood(password)){
			return false;
		}
		this.worlds.remove(worldName);
		return true;
	}
	
	
}
