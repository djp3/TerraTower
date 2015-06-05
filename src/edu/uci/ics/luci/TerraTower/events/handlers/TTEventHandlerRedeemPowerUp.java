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
package edu.uci.ics.luci.TerraTower.events.handlers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventRedeemPowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;


public class TTEventHandlerRedeemPowerUp extends TTEventHandlerPlayer{
	
	private boolean parametersChecked = false;
	
	private PowerUp pup = null;
	

	private boolean getParametersChecked() {
		return parametersChecked;
	}

	private void setParametersChecked(boolean parametersChecked) {
		this.parametersChecked = parametersChecked;
	}

	@Override
	public JSONObject checkParameters(long eventTime, TTEvent _event) {
		//Check parent 
		JSONObject ret = super.checkParameters(eventTime, _event);
		if(ret != null){
			return ret;
		}
		ret = new JSONObject();
		
		TTEventRedeemPowerUp event = null;
		if(_event instanceof TTEventRedeemPowerUp){
			event = ((TTEventRedeemPowerUp) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}
		
		//Check to see if the code is null
		if(event.getCode() == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Power Up does not exist with that code:"+event.getCode());
			ret.put("errors", errors);
			return ret;
		}
		
		//Check to see if the code exists
		if(!wm.powerUpExists(event.getCode())){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Power Up does not exist with that code:"+event.getCode());
			ret.put("errors", errors);
			return ret;
		}
		
		//Check to see if PowerUp is null 
		pup = wm.getPowerUp(event.getCode());
		if(pup == null){
			ret = new JSONObject();
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Powerup is null");
			ret.put("errors", errors);
			return ret;
		}
		
		//Check to see if the power up is already used
		if(pup.getRedeemed()){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("PowerUp has already been used");
			ret.put("errors", errors);
			return ret;
		}
		
		this.setParametersChecked(true);
		
		return null;
	}

	@Override
	public JSONObject onEvent() {   
		
		JSONObject ret = super.onEvent();
		if(ret.get("error").equals("true")){
			return ret;
		}
		
		if(!this.getParametersChecked()){
			ret = new JSONObject();
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Parameters were not checked before calling onEvent");
			ret.put("errors", errors);
			return ret;
		}
		
		/* This shouldn't be possible, but the code checking doesn't realize it */
		if(pup == null){
			ret = new JSONObject();
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error: Powerup is null");
			ret.put("errors", errors);
			return ret;
		}
		
		this.setParametersChecked(false);
		
		ret = new JSONObject();
			
		long bombDelay = player.getBombDelay();
		bombDelay += pup.getBombDelayDelta();
		if(bombDelay < 0){
			bombDelay = 0;
		}
		player.setBombDelay(bombDelay);
		
		long bombFuse = player.getBombFuse();
		bombFuse += pup.getBombFuseDelta();
		if(bombFuse < 0){
			bombFuse = 0;
		}
		player.setBombFuse(bombFuse);
		
		long towerDelay = player.getTowerDelay();
		towerDelay += pup.getTowerDelayDelta();
		if(towerDelay < 0){
			towerDelay = 0;
		}
		player.setTowerDelay(towerDelay);
		
		if(!pup.getCode().equals("test")){
			pup.setRedeemed(true);
		}
		
		JSONObject results = new JSONObject();
		results.put("bomb_delay", bombDelay+"");
		results.put("tower_delay", towerDelay+"");
		results.put("bomb_fuse", bombFuse+"");
		ret.put("results",results);
		ret.put("error","false");
		return ret;
	}
	
}
