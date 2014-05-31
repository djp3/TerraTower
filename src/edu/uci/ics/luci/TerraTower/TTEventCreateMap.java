package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class TTEventCreateMap implements TTEvent{

	private double left;
	private double right;
	private int numXSplits;
	private double bottom;
	private double top;
	private int numYSplits;

	/**
	 * @return the left
	 */
	public double getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(double left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public double getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(double right) {
		this.right = right;
	}

	/**
	 * @return the numXSplits
	 */
	public int getNumXSplits() {
		return numXSplits;
	}

	/**
	 * @param numXSplits the numXSplits to set
	 */
	public void setNumXSplits(int numXSplits) {
		this.numXSplits = numXSplits;
	}

	/**
	 * @return the bottom
	 */
	public double getBottom() {
		return bottom;
	}

	/**
	 * @param bottom the bottom to set
	 */
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	/**
	 * @return the top
	 */
	public double getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(double top) {
		this.top = top;
	}

	/**
	 * @return the numYSplits
	 */
	public int getNumYSplits() {
		return numYSplits;
	}

	/**
	 * @param numYSplits the numYSplits to set
	 */
	public void setNumYSplits(int numYSplits) {
		this.numYSplits = numYSplits;
	}

	public TTEventCreateMap(double left, double right, int numXSplits, double bottom, double top, int numYSplits) {
		this.setLeft(left);
		this.setRight(right);
		this.setNumXSplits(numXSplits);
		this.setBottom(bottom);
		this.setTop(top);
		this.setNumYSplits(numYSplits);
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = new JSONObject();
		ret.put("left", this.getLeft());
		ret.put("right", this.getRight());
		ret.put("numXSplits", this.getNumXSplits());
		ret.put("top", this.getTop());
		ret.put("bottom", this.getBottom());
		ret.put("numYSplits", this.getNumYSplits());
		
		return ret;
	}
	
	static public TTEventCreateMap fromJSON(JSONObject in) {
		double left = (double) in.get("left");
		double right = (double) in.get("right");
		int numXSplits = (int) in.get("numXSplits");
		double top = (double) in.get("top");
		double bottom = (double) in.get("bottom");
		int numYSplits = (int) in.get("numYSplits");
		
		return(new TTEventCreateMap(left,right,numXSplits,bottom,top,numYSplits));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bottom);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(left);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + numXSplits;
		result = prime * result + numYSplits;
		temp = Double.doubleToLongBits(right);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(top);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TTEventCreateMap)) {
			return false;
		}
		TTEventCreateMap other = (TTEventCreateMap) obj;
		if (Double.doubleToLongBits(bottom) != Double
				.doubleToLongBits(other.bottom)) {
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
		if (Double.doubleToLongBits(top) != Double.doubleToLongBits(other.top)) {
			return false;
		}
		return true;
	}
}
