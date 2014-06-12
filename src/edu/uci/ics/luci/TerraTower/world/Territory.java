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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.utility.datastructure.Pair;

public class Territory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3206636784420193073L;
	
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(
		    value="SE_TRANSIENT_FIELD_NOT_RESTORED", 
		    justification="I know what I'm doing")
	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(Territory.class);
		}
		return log;
	}
	
	static Random r;
	static{
		r = new Random();
	}

	private GridCell[][] grid;
	double left;
	double right;
	int numXSplits;
	double stepX;
	double stepXMeters;
	double bottom;
	double top;
	int numYSplits;
	double stepY;
	double stepYMeters;
	private boolean leaderBoardOutdated = true;
	private Map<Player,Integer> leaderBoard;

	public Territory(double left, double right, int numXSplits, double bottom,
			double top, int numYSplits) {
		
		if (left >= right) {
			throw new IllegalArgumentException("left must be less than right");
		}
		if (numXSplits <= 0) {
			throw new IllegalArgumentException(
					"number of X splits must be greater than 0");
		}
		if (bottom >= top) {
			throw new IllegalArgumentException("bottom must be less than top");
		}
		if (numYSplits <= 0) {
			throw new IllegalArgumentException(
					"number of Y splits must be greater than 0");
		}
		this.setLeft(left);
		this.setRight(right);
		this.setNumXSplits(numXSplits);

		this.setStepX((right - left) / numXSplits);
		
		double xdistance = Haversine.haversine(bottom, left, bottom, right);
		this.setStepXMeters(xdistance/numXSplits);
		double ydistance = Haversine.haversine(bottom, left, top, left);
		this.setStepYMeters(ydistance/numYSplits);

		this.setBottom(bottom);
		this.setTop(top);
		this.setNumYSplits(numYSplits);

		this.setStepY((top - bottom) / numYSplits);

		grid = new GridCell[numXSplits][numYSplits];
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				grid[x][y] = new GridCell(x, y);
			}
		}
		
		leaderBoard = Collections.synchronizedMap(new HashMap<Player,Integer>());

	}

	
	public synchronized  boolean xInBounds(double x) {
		if ((x < this.getLeft()) || (x > this.getRight())) {
			return false;
		} else {
			return true;
		}
	}
	
	public synchronized  boolean yInBounds(double y) {
		if ((y < this.getBottom()) || (y > this.getTop())) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized  int xIndex(double x) {
		if(!xInBounds(x)){
			throw new IllegalArgumentException("x is out of bounds");
		}

		int xIndex;
		// Special case
		if (Double.valueOf(x).equals(this.getRight())) {
			xIndex = this.getNumXSplits() - 1;
		} else {
			xIndex = (int) Math.floor((x - this.getLeft()) / this.getStepX());
		}

		return (xIndex);
	}

	public synchronized  int yIndex(double y) {
		if(!yInBounds(y)){
			throw new IllegalArgumentException("y is out of bounds");
		}

		int yIndex;
		// Special case
		if (Double.valueOf(y).equals(this.getTop())) {
			yIndex = this.getNumYSplits() - 1;
		} else {
			yIndex = (int) Math.floor((y - this.getBottom()) / this.getStepY());
		}

		return (yIndex);
	}

	public synchronized  GridCell index(double x, double y) {
		return grid[xIndex(x)][yIndex(y)];
	}
	
	public synchronized  GridCell index(int x, int y) {
		return grid[x][y];
	}


	public synchronized  double getLeft() {
		return left;
	}


	public synchronized  void setLeft(double left) {
		this.left = left;
	}


	public synchronized  double getRight() {
		return right;
	}


	public synchronized  void setRight(double right) {
		this.right = right;
	}


	public synchronized  int getNumXSplits() {
		return numXSplits;
	}


	public synchronized  void setNumXSplits(int numXSplits) {
		this.numXSplits = numXSplits;
	}


	public synchronized  double getStepX() {
		return stepX;
	}


	public synchronized  void setStepX(double stepX) {
		this.stepX = stepX;
	}

	
	public synchronized  double getStepXMeters() {
		return stepXMeters;
	}


	public synchronized  void setStepXMeters(double stepXMeters) {
		this.stepXMeters = stepXMeters;
	}
	
	public synchronized  double getBottom() {
		return bottom;
	}


	public synchronized  void setBottom(double bottom) {
		this.bottom = bottom;
	}


	public synchronized  double getTop() {
		return top;
	}


	public synchronized  void setTop(double top) {
		this.top = top;
	}


	public synchronized  int getNumYSplits() {
		return numYSplits;
	}


	public synchronized  void setNumYSplits(int numYSplits) {
		this.numYSplits = numYSplits;
	}


	public synchronized  double getStepY() {
		return stepY;
	}


	public synchronized  void setStepY(double stepY) {
		this.stepY = stepY;
	}
	
	
	public synchronized  double getStepYMeters() {
		return stepYMeters;
	}


	public synchronized  void setStepYMeters(double stepYMeters) {
		this.stepYMeters = stepYMeters;
	}


	public synchronized boolean isLeaderBoardOutdated() {
		return leaderBoardOutdated;
	}


	public synchronized void setLeaderBoardOutdated(boolean leaderBoardOutdated) {
		this.leaderBoardOutdated = leaderBoardOutdated;
	}


	public synchronized  boolean towerPresent(int x, int y) {
		return(grid[x][y].towerPresent());
	}


	public synchronized  boolean addTower(Tower tower) {
		return(grid[tower.getX()][tower.getY()].addTower(tower));
	}


	public synchronized  void updateAltitude(int xIndex, int yIndex, double alt) {
		index(xIndex,yIndex).updateAltitude(alt);
	}


	


	public synchronized  void stepTowerTerritoryGrowth(int towerMax,int towerStart) {
		stepTowerTerritoryGrowth(towerMax,towerStart,false);
	}
	
	
	
	public synchronized  void stepTowerTerritoryGrowth(int towerMax,int towerStart,boolean withRandom) {
		List<Tower> towerList = new ArrayList<Tower>();
		
		//printGrid();
		
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				//Clear proposed owners
				grid[x][y].getProposedOwner().clear();
				
				
				//Collect a list of towers
				if(grid[x][y].towerPresent()){
					towerList.add(grid[x][y].getTower());
				}
				//Lower existing ownership levels by 1 with a floor of 1
				grid[x][y].lowerTowerTerritoryLevel(1,1);
				
				//Add the current owner as a proposed owner in case it's no longer supported by a tower
				Pair<Player, Integer> owner = grid[x][y].getOwner();
				Map<Player, Integer> p = new HashMap<Player,Integer>();
				p.put(owner.getFirst(),owner.getSecond());
				grid[x][y].setProposedOwner(p);
			}
		}
		//printGrid();
		
		//Shuffle towers so that ties are randomly resolved
		Collections.shuffle(towerList);
		
		for(Tower tower: towerList){
			
			//Clear the steps marker for this tower
			for (int x = 0; x < numXSplits; x++) {
				for (int y = 0; y < numYSplits; y++) {
					grid[x][y].setStepsTaken(Integer.MAX_VALUE);
				}
			}
			
			int x = tower.getX();
			int y = tower.getY();
			Pair<Player, Integer> o = grid[x][y].getOwner();
			
			/*If the owner of the tower doesn't own the land it sits on then the tower attempts
			 * to start claiming land at power level "towerstart"
			 */
			if(o.getFirst() != tower.getOwner()){
				stepTowerTerritoryRecurse(tower, towerStart,tower.getX(),tower.getY(),1,withRandom);
			}
			/* If the owner of the tower does own the land, then the tower attempts to start claiming
			 * land at one level higher (which is two because of the decay above) 
			 */
			else{
				Integer level = o.getSecond();
				level+=2;
				stepTowerTerritoryRecurse(tower, level,tower.getX(),tower.getY(),1,withRandom);
			}
		}
		
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				//Clear proposed owners
				grid[x][y].resolveOwner();
				grid[x][y].raiseTowerTerritoryLevel(0,towerMax);
			}
		}
		
		setLeaderBoardOutdated(true);
		//printGrid();
	}

	public synchronized void printGrid() {
		System.out.println();
		for (int y = 0; y < numYSplits; y++) {
			for (int x = 0; x < numXSplits; x++) {
				Player player = grid[x][y].getOwner().getFirst();
				if(player == null){
					System.out.print("\t ");
				}
				else{
					System.out.print("\t"+player.getPlayerName().substring(0, 1));
				}
				if(grid[x][y].towerPresent()){
					System.out.print("<t>");
				}
				else{
					System.out.print("...");
				}
				if(grid[x][y].numBombsPresent() != 0 ){
					System.out.print("<"+grid[x][y].numBombsPresent()+">");
				}
				else{
					System.out.print("...");
				}
				
				
				if(grid[x][y].getOwner().getSecond() != 0 ){
					System.out.print(" "+grid[x][y].getOwner().getSecond());
				}
				else{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		List<Pair<Integer, Player>> l = this.getLeaderBoard();
		for(Pair<Integer, Player> p:l){
			System.out.println(p.getSecond().getPlayerName()+":"+p.getFirst());
		}
	}


	private void stepTowerTerritoryRecurse(Tower tower,int level, int x, int y,int stepsTaken,boolean withRandom) {
		if(level == 0){
			//return;
		}
		//panic
		if(stepsTaken > 300){
			return;
		}
		if(x < 0 ){
			return;
		}
		if(x >= numXSplits){
			return;
		}
		if(y < 0 ){
			return;
		}
		if(y >= numYSplits){
			return;
		}
		if(grid[x][y].getStepsTaken()<=stepsTaken){
			return;
		}
		else{
			grid[x][y].setStepsTaken(stepsTaken);
		}
		
		int proposedlevel = level;
		Pair<Player, Integer> o = grid[x][y].getOwner();
		if((o.getFirst() != null) &&(o.getFirst().equals(tower.getOwner()))){
			proposedlevel = o.getSecond()+2; // Increase it to one more than decay
			level++; //don't decrease the counter in the next step
		}
		else{
			if(level > 0){
				proposedlevel = 2; //Increase it to one more than decay
				level = 0; //Stop allowing expansion
			}
			else{
				proposedlevel = 0;
			}
		}
		
		// Check to see if the cell has a proposal for this player 
		Map<Player, Integer> po = grid[x][y].getProposedOwner();
		Integer pl = po.get(tower.getOwner());
		//If no proposal make one
		if(pl == null){
			if(proposedlevel > 0){
				po.put(tower.getOwner(), proposedlevel);
			}
		}
		//If the proposal is too low, increase it
		else if(pl < proposedlevel){
			po.put(tower.getOwner(), proposedlevel);
		}
		else{
			//the existing proposal is fine.
		}
		
		//Recurse around
		ArrayList<Pair<Integer, Integer>> steps = new ArrayList<Pair<Integer,Integer>>();
		steps.add(new Pair<Integer,Integer>(x,y-1));
		steps.add(new Pair<Integer,Integer>(x,y+1));
		steps.add(new Pair<Integer,Integer>(x-1,y));
		steps.add(new Pair<Integer,Integer>(x+1,y));
		if(withRandom){
			if(r.nextDouble()<0.707){
				steps.add(new Pair<Integer,Integer>(x+1,y+1));
			}
			if(r.nextDouble()<0.707){
				steps.add(new Pair<Integer,Integer>(x-1,y+1));
			}
			if(r.nextDouble()<0.707){
				steps.add(new Pair<Integer,Integer>(x-1,y-1));
			}
			if(r.nextDouble()<0.707){
				steps.add(new Pair<Integer,Integer>(x+1,y-1));
			}
		}
		Collections.shuffle(steps);
		for(Pair<Integer, Integer> p:steps){
			stepTowerTerritoryRecurse(tower,level-1, p.getFirst(),p.getSecond(),stepsTaken+1, withRandom);
		}
	}
	
	public synchronized  List<Pair<Integer, Player>> getLeaderBoard(){
		if(isLeaderBoardOutdated()){
			leaderBoard.clear();
			for (int x = 0; x < numXSplits; x++) {
				for (int y = 0; y < numYSplits; y++) {
					Pair<Player, Integer> o = grid[x][y].getOwner();
					if(leaderBoard.containsKey(o.getFirst())){
						Integer e = leaderBoard.get(o.getFirst());
						leaderBoard.put(o.getFirst(), e+1);
					}
					else{
						leaderBoard.put(o.getFirst(), 1);
					}
				}
			}
			setLeaderBoardOutdated(false);
		}
		List<Pair<Integer,Player>> ret = new ArrayList<Pair<Integer,Player>>();
		for(Entry<Player, Integer> e :leaderBoard.entrySet()){
			ret.add(new Pair<Integer,Player>(e.getValue(),e.getKey()));
		}
		Collections.sort(ret,Collections.reverseOrder());
		return(ret);
	}


	public synchronized  int numBombsPresent(int x, int y) {
		return(grid[x][y].numBombsPresent());
	}


	public synchronized  boolean addBomb(Bomb bomb) {
		return(grid[bomb.getX()][bomb.getY()].addBomb(bomb));
	}


	public synchronized  void burnBombFuse(long eventTime) {
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				if(grid[x][y].numBombsPresent() > 0){
					SortedMap<Long, List<Bomb>> bombs = grid[x][y].getBombs();
					//Go through all bombs that should be exploded
					for(List<Bomb> e :bombs.headMap(eventTime+1).values()){
						for(Bomb b: e){
							if(!b.isExploded()){
								for(int bx = (b.getX() - b.getStrength()); bx<= (b.getX() + b.getStrength()); bx++ ){
									if((bx >= 0) && (bx < this.getNumXSplits())){
										for(int by = (b.getY() - b.getStrength()); by<= (b.getY() + b.getStrength()); by++ ){
											if((by >= 0) && (by < this.getNumYSplits())){
												if(inRange(bx,by,b.getX(),b.getY(),b.getStrength())){
													b.setExploded(true);
													effectBombDamage(bx,by);
												}
											}
										}
									}
								}
							}
						}
					}
					//Remove bombs that have expired
					grid[x][y].setBombs(bombs.tailMap(eventTime+1));
				}
				//Remove towers that have been destroyed
				if(grid[x][y].towerPresent()){
					Tower tower = grid[x][y].getTower();
					if(tower.isDestroyed()){
						grid[x][y].setTower(null);
					}
				}
			}
		}
		
	}


	private void effectBombDamage(int x, int y) {
		setLeaderBoardOutdated(true);
		grid[x][y].resetOwner();
		if(grid[x][y].towerPresent()){
			Tower tower = grid[x][y].getTower();
			tower.setDestroyed(true);
		}
	}


	public synchronized  static boolean inRange(int bx, int by, int x, int y, int strength) {
		int dx = (bx - x);
		int dy = (by - y);
		int d = strength;
		if((dx*dx+dy*dy)< (d*d)){
			return true;
		}
		else{
			return false;
		}
	}


	@Override
	public synchronized  int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bottom);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.deepHashCode(grid);			//Special!!
		result = prime * result + (isLeaderBoardOutdated() ? 1231 : 1237);
		temp = Double.doubleToLongBits(left);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numXSplits;
		result = prime * result + numYSplits;
		temp = Double.doubleToLongBits(right);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepXMeters);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepYMeters);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(top);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public synchronized  boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Territory)) {
			return false;
		}
		Territory other = (Territory) obj;
		if (Double.doubleToLongBits(bottom) != Double
				.doubleToLongBits(other.getBottom())) {
			return false;
		}
		if (!Arrays.deepEquals(grid, other.grid)) {
			return false;
		}
		if (isLeaderBoardOutdated() != other.isLeaderBoardOutdated()) {
			return false;
		}
		if (Double.doubleToLongBits(left) != Double
				.doubleToLongBits(other.getLeft())) {
			return false;
		}
		if (numXSplits != other.getNumXSplits()) {
			return false;
		}
		if (numYSplits != other.getNumYSplits()) {
			return false;
		}
		if (Double.doubleToLongBits(right) != Double
				.doubleToLongBits(other.getRight())) {
			return false;
		}
		if (Double.doubleToLongBits(stepX) != Double
				.doubleToLongBits(other.getStepX())) {
			return false;
		}
		if (Double.doubleToLongBits(stepXMeters) != Double
				.doubleToLongBits(other.getStepXMeters())) {
			return false;
		}
		if (Double.doubleToLongBits(stepY) != Double
				.doubleToLongBits(other.getStepY())) {
			return false;
		}
		if (Double.doubleToLongBits(stepYMeters) != Double
				.doubleToLongBits(other.getStepYMeters())) {
			return false;
		}
		if (Double.doubleToLongBits(top) != Double.doubleToLongBits(other.getTop())) {
			return false;
		}
		return true;
	}
	
	//@SuppressWarnings("unchecked")
	public Territory deepCopy() {
		Territory cloned = (Territory) org.apache.commons.lang.SerializationUtils.clone(this);
		return(cloned);
	}
	

}
