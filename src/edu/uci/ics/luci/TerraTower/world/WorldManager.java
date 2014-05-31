package edu.uci.ics.luci.TerraTower.world;

import java.util.HashMap;

public class WorldManager {
	
	static HashMap<String, String> worlds = new HashMap<String,String>();

	public static boolean exists(String name) {
		return worlds.containsKey(name);
	}

	public static boolean create(String name, String password) {
		if(!exists(name)){
			worlds.put(name, password);
			return exists(name);
		}
		else{
			return false;
		}
	}
}
