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
package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;

public class TTEventVoid extends TTEvent{
	
	public TTEventVoid(){
		super((String)null,(String)null);
	}
	
	public TTEventVoid(String worldName,String worldPassword){
		super(worldName,worldPassword);
	}
	
	public TTEventVoid(String worldName,byte[] worldPassword){
		super(worldName,worldPassword);
	}
	
	@Override
	public JSONObject toJSON() {
		return(super.toJSON());
	}
	
	static public TTEventVoid fromJSON(JSONObject in) {
		TTEvent parent = TTEvent.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		return(new TTEventVoid(worldName,worldHashedPassword));
	}
	
}
