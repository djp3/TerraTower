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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.utility.datastructure.Pair;

public class Territory {

	private GridCell[][] grid;
	double left;
	double right;
	int numXSplits;
	double stepX;
	double bottom;
	double top;
	int numYSplits;
	double stepY;
	private boolean leaderBoardOutdated = true;
	private transient Map<Player,Integer> leaderBoard;

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
		
		leaderBoard = new HashMap<Player,Integer>();

	}

	
	public boolean xInBounds(double x) {
		if ((x < this.getLeft()) || (x > this.getRight())) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean yInBounds(double y) {
		if ((y < this.getBottom()) || (y > this.getTop())) {
			return false;
		} else {
			return true;
		}
	}

	public int xIndex(double x) {
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

	public int yIndex(double y) {
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

	public GridCell index(double x, double y) {
		return grid[xIndex(x)][yIndex(y)];
	}


	public double getLeft() {
		return left;
	}


	public void setLeft(double left) {
		this.left = left;
	}


	public double getRight() {
		return right;
	}


	public void setRight(double right) {
		this.right = right;
	}


	public int getNumXSplits() {
		return numXSplits;
	}


	public void setNumXSplits(int numXSplits) {
		this.numXSplits = numXSplits;
	}


	public double getStepX() {
		return stepX;
	}


	public void setStepX(double stepX) {
		this.stepX = stepX;
	}


	public double getBottom() {
		return bottom;
	}


	public void setBottom(double bottom) {
		this.bottom = bottom;
	}


	public double getTop() {
		return top;
	}


	public void setTop(double top) {
		this.top = top;
	}


	public int getNumYSplits() {
		return numYSplits;
	}


	public void setNumYSplits(int numYSplits) {
		this.numYSplits = numYSplits;
	}


	public double getStepY() {
		return stepY;
	}


	public void setStepY(double stepY) {
		this.stepY = stepY;
	}


	public boolean towerPresent(int x, int y) {
		return(grid[x][y].towerPresent());
	}


	public boolean addTower(Tower tower) {
		return(grid[tower.getX()][tower.getY()].addTower(tower));
	}


	public void updateAltitude(int xIndex, int yIndex, double alt) {
		index(xIndex,yIndex).updateAltitude(alt);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bottom);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.deepHashCode(grid);			//Special!!
		temp = Double.doubleToLongBits(left);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numXSplits;
		result = prime * result + numYSplits;
		temp = Double.doubleToLongBits(right);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(stepY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(top);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof Territory)) {
			return false;
		}
		Territory other = (Territory) obj;
		if (Double.doubleToLongBits(bottom) != Double
				.doubleToLongBits(other.bottom)) {
			return false;
		}
		if (!Arrays.deepEquals(grid, other.grid)) {
			return false;
		}
		if (Double.doubleToLongBits(left) != Double
				.doubleToLongBits(other.left)) {
			return false;
		}
		if (numXSplits != other.numXSplits) {
			return false;
		}
		if (numYSplits != other.numYSplits) {
			return false;
		}
		if (Double.doubleToLongBits(right) != Double
				.doubleToLongBits(other.right)) {
			return false;
		}
		if (Double.doubleToLongBits(stepX) != Double
				.doubleToLongBits(other.stepX)) {
			return false;
		}
		if (Double.doubleToLongBits(stepY) != Double
				.doubleToLongBits(other.stepY)) {
			return false;
		}
		if (Double.doubleToLongBits(top) != Double.doubleToLongBits(other.top)) {
			return false;
		}
		return true;
	}


	public void stepTowerTerritoryGrowth(int towerMax,int towerStart) {
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
				stepTowerTerritoryRecurse(tower, towerStart,tower.getX(),tower.getY(),1);
			}
			/* If the owner of the tower does own the land, then the tower attempts to start claiming
			 * land at one level higher (which is two because of the decay above) 
			 */
			else{
				Integer level = o.getSecond();
				level+=2;
				stepTowerTerritoryRecurse(tower, level,tower.getX(),tower.getY(),1);
			}
		}
		
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				//Clear proposed owners
				grid[x][y].resolveOwner();
				grid[x][y].raiseTowerTerritoryLevel(0,towerMax);
			}
		}
		
		leaderBoardOutdated = true;
		//printGrid();
	}

	private void printGrid() {
		System.out.println();
		for (int y = 0; y < numYSplits; y++) {
			for (int x = 0; x < numXSplits; x++) {
				Player player = grid[x][y].getOwner().getFirst();
				if(player == null){
					System.out.print("\tn");
				}
				else{
					System.out.print("\t"+player.getPlayerName());
				}
				System.out.print(":"+grid[x][y].getOwner().getSecond());
			}
			System.out.println();
		}
	}


	private void stepTowerTerritoryRecurse(Tower tower,int level, int x, int y,int stepsTaken) {
		if(level == 0){
			//return;
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
		Collections.shuffle(steps);
		for(Pair<Integer, Integer> p:steps){
			stepTowerTerritoryRecurse(tower,level-1, p.getFirst(),p.getSecond(),stepsTaken+1);
		}
	}
	
	private void calculateLeaderBoard(){
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
		leaderBoardOutdated = false;
	}


	public int numBombsPresent(int x, int y) {
		return(grid[x][y].numBombsPresent());
	}


	public boolean addBomb(Bomb bomb) {
		return(grid[bomb.getX()][bomb.getY()].addBomb(bomb));
	}
	

}
