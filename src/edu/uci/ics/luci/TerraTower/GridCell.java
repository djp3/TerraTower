package edu.uci.ics.luci.TerraTower;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	public GridCell(int x,int y){
		setX(x);
		setY(y);
	}

}
