package edu.uci.ics.luci.TerraTower.webhandlers;

import java.util.Map;

import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;

import net.minidev.json.JSONArray;

public abstract class HandlerAbstractPlayer extends HandlerAbstractWorld{
	

	private String playerPassword;
	private String playerName;

	public HandlerAbstractPlayer(TTEventWrapperQueuer eventPublisher) {
		super(eventPublisher);
	}

	protected String getPlayerPassword() {
		return playerPassword;
	}

	protected String getPlayerName() {
		return playerName;
	}

	protected JSONArray getPlayerParameters(String restFunction,Map<String,String> parameters){
		JSONArray errors = new JSONArray();
		
		playerName = parameters.get("player_name");
		if(playerName == null){
			errors.add("Problem handling "+restFunction+": player_name was null");
		}
		playerPassword = parameters.get("player_password");
		if(playerPassword == null){
			errors.add("Problem handling "+restFunction+": player_password was null");
		}
		return errors;
	}

}
