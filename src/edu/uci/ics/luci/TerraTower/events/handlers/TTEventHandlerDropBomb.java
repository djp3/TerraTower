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
import edu.uci.ics.luci.TerraTower.GlobalsTerraTower;
import edu.uci.ics.luci.TerraTower.events.TTEvent;
import edu.uci.ics.luci.TerraTower.events.TTEventDropBomb;
import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.world.Territory;


public class TTEventHandlerDropBomb extends TTEventHandlerPlayer{
	
	private boolean parametersChecked = false;
	
	private long eventTime;
	private int xIndex ;
	private int yIndex ;
	private double alt;
	private Territory territory = null;
	

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
		
		TTEventDropBomb event = null;
		if(_event instanceof TTEventDropBomb){
			event = ((TTEventDropBomb) _event);
		}
		else{
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Internal error, event type mismatch\n"+this.getClass().getCanonicalName()+" was called with "+_event.getClass().getCanonicalName());
			ret.put("errors", errors);
			return ret;
		}
		
		//Check that territory exists 
		territory = wm.getTerritory();
		if(territory == null){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("No territory was initialized");
			ret.put("errors", errors);
			return ret;
		}
		

		//Has it been too soon since last bomb?
		long elapsed = eventTime - player.getLastBombPlacedTime();
		if (elapsed < player.getBombDelay()){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Last bomb placed "+elapsed+" ms ago.  Need to wait: "+player.getBombDelay());
			ret.put("errors", errors);
			return ret;
		}
		

		//Check bomb placement
		double lat = event.getLat();
		double lng = event.getLng();
		alt = event.getAlt();
		//Check coordinates to see if it is in bounds
		if(!territory.xInBounds(lng)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Bomb is out of bounds x="+lng+", "+territory.getLeft()+"< x < "+territory.getRight());
			ret.put("errors", errors);
			return ret;
		}
		if(!territory.yInBounds(lat)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Bomb is out of bounds y="+lat+", "+territory.getBottom()+"< y < "+territory.getTop());
			ret.put("errors", errors);
			return ret;
		}
		
		//Is there already a bomb there?
		//I think we don't care if there is a bomb there
		xIndex = territory.xIndex(lng);
		yIndex = territory.yIndex(lat);
		/*
		if(wm.numBombsPresent(xIndex,yIndex)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Bomb already exists close to that location");
			ret.put("errors", errors);
			return ret;
		}*/
		
		this.eventTime = eventTime;
		
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
		
		this.setParametersChecked(false);
		
		ret = new JSONObject();
			
		if(!wm.addBomb(new Bomb(player,xIndex,yIndex,player.getBombFuse(),GlobalsTerraTower.DEFAULT_TOWER_STRENGTH))){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Unable to add bomb due to an unknown reason");
			ret.put("errors", errors);
			return ret;
		}
		player.setLastBombPlacedTime(eventTime);
		
		if(this.alt > 0.0){
			territory.updateAltitude(xIndex,yIndex,alt);
		}
		
		ret.put("error","false");
		return ret;
	}
	
}
