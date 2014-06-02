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

	public double estimateAltitude() {
		double x = 0;
		for(Double a :alts.keySet()){
			x+=a;
		}
		return x/alts.size();
	}

}
