package edu.uci.ics.luci.TerraTower;

public class TTEventCreateMap extends TTEvent {

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
		super(TTEvent.TTEventType.CREATE_MAP);
		
		this.setLeft(left);
		this.setRight(right);
		this.setNumXSplits(numXSplits);
		this.setBottom(bottom);
		this.setTop(top);
		this.setNumYSplits(numYSplits);
	}
	
	void set(TTEventCreateMap ttEvent){
		super.set(ttEvent);
		
		this.setLeft(ttEvent.getLeft());
		this.setRight(ttEvent.getRight());
		this.setNumXSplits(ttEvent.getNumXSplits());
		this.setBottom(ttEvent.getBottom());
		this.setTop(ttEvent.getTop());
		this.setNumYSplits(ttEvent.getNumYSplits());
	}
}
