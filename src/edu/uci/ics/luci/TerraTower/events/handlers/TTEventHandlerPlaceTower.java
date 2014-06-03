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
import edu.uci.ics.luci.TerraTower.events.TTEventPlaceTower;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.TerraTower.world.Territory;


public class TTEventHandlerPlaceTower extends TTEventHandlerPlayer{
	
	private int xIndex ;
	private int yIndex ;
	private double alt;
	private Territory territory = null;

	@Override
	public JSONObject checkParameters(long eventTime, TTEvent _event) {
		//Check parent 
		JSONObject ret = super.checkParameters(eventTime, _event);
		if(ret != null){
			return ret;
		}
		ret = new JSONObject();
		
		TTEventPlaceTower event = null;
		if(_event instanceof TTEventPlaceTower){
			event = ((TTEventPlaceTower) _event);
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
		

		//Has it been too soon since last tower?
		long elapsed = eventTime - player.getLastTowerPlacedTime();
		if (elapsed < player.getTowerDelay()){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Last tower placed "+elapsed+" ms ago.  Need to wait: "+player.getTowerDelay());
			ret.put("errors", errors);
			return ret;
		}
		

		//Check tower placement
		double lat = event.getLat();
		double lng = event.getLng();
		alt = event.getAlt();
		//Check coordinates to see if it is in bounds
		if(!territory.xInBounds(lng)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Tower is out of bounds x="+lng+", "+territory.getLeft()+"< x < "+territory.getRight());
			ret.put("errors", errors);
			return ret;
		}
		if(!territory.yInBounds(lat)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Tower is out of bounds y="+lat+", "+territory.getBottom()+"< y < "+territory.getTop());
			ret.put("errors", errors);
			return ret;
		}
		
		//Is there already a tower there?
		xIndex = territory.xIndex(lng);
		yIndex = territory.yIndex(lat);
		if(wm.towerPresent(xIndex,yIndex)){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Tower already exists close to that location");
			ret.put("errors", errors);
			return ret;
		}
		
		return null;
	}

	@Override
	public JSONObject onEvent() {   
		
		JSONObject ret = super.onEvent();
		if(ret.get("error").equals("true")){
			return ret;
		}
		ret = new JSONObject();
			
		if(!wm.addTower(new Tower(player,xIndex,yIndex))){
			ret.put("error","true");
			JSONArray errors = new JSONArray();
			errors.add("Unable to add tower due to an unknown reason");
			ret.put("errors", errors);
			return ret;
		}
		
		if(this.alt > 0.0){
			territory.updateAltitude(xIndex,yIndex,alt);
		}
		
		ret.put("error","false");
		return ret;
	}
	
}
