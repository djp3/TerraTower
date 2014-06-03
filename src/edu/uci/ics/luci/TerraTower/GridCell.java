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
package edu.uci.ics.luci.TerraTower;

import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.gameElements.Tower;

public class GridCell {
	

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(GridCell.class);
		}
		return log;
	}
	
	private int x;
	private int y;
	
	private Tower t = null;
	private TreeMap<Double,Double> alts;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Tower getTower() {
		return t;
	}

	public void setTower(Tower t) {
		this.t = t;
	}

	public TreeMap<Double, Double> getAlts() {
		return alts;
	}

	public void setAlts(TreeMap<Double, Double> alts) {
		this.alts = alts;
	}

	public GridCell(int x,int y){
		setX(x);
		setY(y);
		setAlts(new TreeMap<Double,Double>());
	}

	public boolean towerPresent() {
		return (t != null);
	}

	public boolean addTower(Tower tower) {
		setTower(tower);
		return towerPresent();
	}

	public void updateAltitude(double alt) {
		alts.put(alt,alt);
	}

	public Double estimateAltitude() {
		Double x = 0.0d;
		for(Double a :alts.keySet()){
			x+=a;
		}
		return x/alts.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alts == null) ? 0 : alts.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GridCell)) {
			return false;
		}
		GridCell other = (GridCell) obj;
		if (alts == null) {
			if (other.alts != null) {
				return false;
			}
		} else if (!alts.equals(other.alts)) {
			return false;
		}
		if (t == null) {
			if (other.t != null) {
				return false;
			}
		} else if (!t.equals(other.t)) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}
