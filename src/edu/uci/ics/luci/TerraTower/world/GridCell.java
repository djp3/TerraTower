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
package edu.uci.ics.luci.TerraTower.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.utility.datastructure.Pair;

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
	
	private Tower t;
	private SortedMap<Long,List<Bomb>> bombs = null;
	private SortedMap<Double,Double> alts;
	
	private Pair<Player,Integer> owner;
	
	//Used during the territory expansion as temp variables
	private Map<Player,Integer> proposedOwner;
	private int stepsTaken;
	
	public synchronized int getX() {
		return x;
	}

	public synchronized void setX(int x) {
		this.x = x;
	}

	public synchronized  int getY() {
		return y;
	}

	public synchronized void setY(int y) {
		this.y = y;
	}

	public synchronized  Tower getTower() {
		return t;
	}

	public synchronized  void setTower(Tower t) {
		this.t = t;
	}

	public synchronized  SortedMap<Long, List<Bomb>> getBombs() {
		return bombs;
	}

	public synchronized  void setBombs(SortedMap<Long, List<Bomb>> bombs) {
		this.bombs = bombs;
	}

	public synchronized  SortedMap<Double, Double> getAlts() {
		return alts;
	}

	public synchronized  void setAlts(SortedMap<Double, Double> alts) {
		this.alts = alts;
	}

	public synchronized  Pair<Player, Integer> getOwner() {
		return owner;
	}

	public synchronized  void setOwner(Pair<Player, Integer> owner) {
		this.owner = owner;
	}

	public synchronized  Map<Player, Integer> getProposedOwner() {
		return proposedOwner;
	}

	public synchronized  void setProposedOwner(Map<Player, Integer> proposedOwner) {
		this.proposedOwner = proposedOwner;
	}
	
	public synchronized  int getStepsTaken() {
		return stepsTaken;
	}

	public synchronized  void setStepsTaken(int stepsTaken) {
		this.stepsTaken = stepsTaken;
	}

	public GridCell(int x,int y){
		setX(x);
		setY(y);
		setBombs(Collections.synchronizedSortedMap(new TreeMap<Long,List<Bomb>>()));
		setAlts(Collections.synchronizedSortedMap(new TreeMap<Double,Double>()));
		setTower(null);
		resetOwner();
	}

	public synchronized  void resetOwner() {
		setOwner(new Pair<Player,Integer>(Player.BARBARIAN,0));
		setProposedOwner(Collections.synchronizedMap(new HashMap<Player,Integer>()));
	}

	public synchronized  boolean towerPresent() {
		return (t != null);
	}

	public synchronized  boolean addTower(Tower tower) {
		setTower(tower);
		return towerPresent();
	}
	

	public synchronized  int numBombsPresent() {
		int count = 0;
		for(List<Bomb> e:bombs.values()){
			count += e.size();
		}
		return(count);
	}
	
	public synchronized  boolean addBomb(Bomb bomb) {
		int size = numBombsPresent();
		List<Bomb> list = getBombs().get(bomb.getExplosionTime());
		if(list == null){
			list = new ArrayList<Bomb>();
		}
		list.add(bomb);
		bombs.put(bomb.getExplosionTime(),list);
		int size2 = numBombsPresent();
		return (size2 == (size+1));
	}

	public synchronized  void updateAltitude(double alt) {
		alts.put(alt,alt);
	}

	public synchronized  Double estimateAltitude() {
		Double x = 0.0d;
		for(Double a :alts.keySet()){
			x+=a;
		}
		return x/alts.size();
	}


	// for any levels above the floor lower it no lower than the floor
	// if it starts below the floor, then leave it there.
	public synchronized  void lowerTowerTerritoryLevel(int delta,int floor) {
		Pair<Player, Integer> o = getOwner();
		if(o != null){
			Integer level = o.getSecond();
			if(level != null){
				if(level > floor){
					level -= delta;
					if(level < floor){
						level = floor;
					}
				}
			}
			else{
				level = floor;
			}
			setOwner(new Pair<Player,Integer>(o.getFirst(),level));
		}
	}
	
	public synchronized  void raiseTowerTerritoryLevel(int delta,int ceiling) {
		Pair<Player, Integer> o = getOwner();
		if(o != null){
			Integer level = o.getSecond();
			if(level != null){
				level += delta;
				if(level > ceiling ){
					level = ceiling;
				}
			}
			else{
				level = ceiling;
			}
			setOwner(new Pair<Player,Integer>(o.getFirst(),level));
		}
	}

	public synchronized  void resolveOwner() {
		Player maxPlayer = null;
		int max = -1;
		for( Entry<Player, Integer> po : getProposedOwner().entrySet()){
			if(po.getValue() > max){
				maxPlayer = po.getKey();
				max = po.getValue();
			}
		}
		if(maxPlayer != null){
			setOwner(new Pair<Player,Integer>(maxPlayer,max));
		}
		else{
			setOwner(new Pair<Player,Integer>(null,0));
		}
		getProposedOwner().clear();
		setStepsTaken(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alts == null) ? 0 : alts.hashCode());
		result = prime * result + ((bombs == null) ? 0 : bombs.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((proposedOwner == null) ? 0 : proposedOwner.hashCode());
		result = prime * result + stepsTaken;
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
		if (bombs == null) {
			if (other.bombs != null) {
				return false;
			}
		} else if (!bombs.equals(other.bombs)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		if (proposedOwner == null) {
			if (other.proposedOwner != null) {
				return false;
			}
		} else if (!proposedOwner.equals(other.proposedOwner)) {
			return false;
		}
		if (stepsTaken != other.stepsTaken) {
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
