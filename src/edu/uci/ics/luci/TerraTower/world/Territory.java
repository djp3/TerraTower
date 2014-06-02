package edu.uci.ics.luci.TerraTower.world;

import edu.uci.ics.luci.TerraTower.GridCell;

public class Map {

	GridCell[][] grid;
	double left;
	double right;
	int numXSplits;
	double stepX;
	double bottom;
	double top;
	int numYSplits;
	double stepY;

	public Map(double left, double right, int numXSplits, double bottom,
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
		this.left = left;
		this.right = right;
		this.numXSplits = numXSplits;

		this.stepX = (right - left) / numXSplits;

		this.bottom = bottom;
		this.top = top;
		this.numYSplits = numYSplits;

		this.stepY = (top - bottom) / numYSplits;

		grid = new GridCell[numXSplits][numYSplits];
		for (int x = 0; x < numXSplits; x++) {
			for (int y = 0; y < numYSplits; y++) {
				grid[x][y] = new GridCell(x, y);
			}
		}

	}

	
	boolean xInBounds(double x) {
		if ((x < left) || (x > right)) {
			return false;
		} else {
			return true;
		}
	}
	
	boolean yInBounds(double y) {
		if ((y < bottom) || (y > top)) {
			return false;
		} else {
			return true;
		}
	}

	int xIndex(double x) {
		if(!xInBounds(x)){
			throw new IllegalArgumentException("x is out of bounds");
		}

		int xIndex;
		// Special case
		if (x == right) {
			xIndex = numXSplits - 1;
		} else {
			xIndex = (int) Math.floor((x - left) / stepX);
		}

		return (xIndex);
	}

	int yIndex(double y) {
		if(!yInBounds(y)){
			throw new IllegalArgumentException("y is out of bounds");
		}

		int yIndex;
		// Special case
		if (y == top) {
			yIndex = numYSplits - 1;
		} else {
			yIndex = (int) Math.floor((y - bottom) / stepY);
		}

		return (yIndex);
	}

	public GridCell index(double x, double y) {
		return grid[xIndex(x)][yIndex(y)];
	}

}
