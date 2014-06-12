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
package edu.uci.ics.luci.TerraTower.events.handlers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventCreatePowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;


public class TTEventHandlerCreatePowerUp extends TTEventHandler{
	
	private boolean parametersChecked = false;
	private String code;
	private Long towerDelayDelta;
	private Long bombDelayDelta;
	private Long bombFuseDelta;
	
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
				
		TTEventCreatePowerUp event = null;
		if(_event instanceof TTEventCreatePowerUp){
			event = ((TTEventCreatePowerUp) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}
		
		code = event.getCode();
		if(code == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("PowerUp code can't be null");
			ret.put("errors", errors);
			return ret;
		}
		towerDelayDelta = event.getTowerDelayDelta();
		bombDelayDelta = event.getBombDelayDelta();
		bombFuseDelta = event.getBombFuseDelta();
		
		if(wm.powerUpExists(code)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("PowerUp with that code already exists");
			ret.put("errors", errors);
			return ret;
		}
		
		this.setParametersChecked(true);

		return null;
	}

	@Override
	public JSONObject onEvent()  {   
		
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
		
		this.setParametersChecked(false);
		
		ret = new JSONObject();
		
		PowerUp pup = new PowerUp(code, towerDelayDelta,bombDelayDelta,bombFuseDelta,false);
		if(wm.createPowerUp(pup) == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("That PowerUp already exists (by name)");
			ret.put("errors", errors);
			return ret;
		}
		
		ret.put("error","false");
		return ret;
	}

}
