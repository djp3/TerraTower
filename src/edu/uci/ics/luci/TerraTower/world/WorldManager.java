package edu.uci.ics.luci.TerraTower.world;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class WorldManager {
	
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(WorldManager.class);
		}
		return log;
	}
	
	
	HashMap<String, byte[]> worlds = new HashMap<String,byte[]>();
	HashMap<String,Map> maps = new HashMap<String,Map>();
	
	
	public boolean worldExists(String worldName) {
		return worlds.containsKey(worldName);
	}
	
	public boolean passwordGood(String worldName,String password) {
		if(!worldExists(worldName)){
			return false;
		}
		byte[] p = worlds.get(worldName);
		return (Arrays.equals(p,PasswordUtils.hashPassword(password)));
	}

	public boolean create(String name, String password) {
		if(!worldExists(name)){
			worlds.put(name, PasswordUtils.hashPassword(password));
			return worldExists(name);
		}
		else{
			return false;
		}
	}
	
	public boolean mapExists(String worldName){
		return(maps.containsKey(worldName));
	}

	public boolean addMap(String worldName, String password, Map map) {
		if(!worldExists(worldName)){
			return false;
		}
		if(!passwordGood(worldName,password)){
			return false;
		}
		if(mapExists(worldName)){
			return false;
		}
		maps.put(worldName, map);
		return true;
	}
}
