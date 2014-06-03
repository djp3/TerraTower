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
package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;


public class TTEventCreateWorld extends TTEvent {

	public TTEventCreateWorld(String worldName, String worldPassword) {
		super(worldName, worldPassword);
	}
	
	public TTEventCreateWorld(String worldName, byte[] worldHashedPassword) {
		super(worldName, worldHashedPassword);
	}
	
	static public TTEventCreateWorld fromJSON(JSONObject in) {
		TTEvent tt = TTEvent.fromJSON(in);
		return(new TTEventCreateWorld(tt.getWorldName(), tt.getWorldHashedPassword()));
	}

}
