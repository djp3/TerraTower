package edu.uci.ics.luci.TerraTower.webhandlers;

import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;
import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;

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
	
	protected JSONArray getPlayerParameters(String restFunction,Map<String,Set<String>> parameters){
		JSONArray errors = new JSONArray();
		
		Set<String> _playerName = parameters.get("player_name");
		if((_playerName == null) || ((playerName = (_playerName.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+": player_name was null");
		}
		Set<String> _playerPassword = parameters.get("player_password");
		if((_playerPassword == null) || ((playerPassword = (_playerPassword.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+": player_password was null");
		}
		return errors;
	}

}
