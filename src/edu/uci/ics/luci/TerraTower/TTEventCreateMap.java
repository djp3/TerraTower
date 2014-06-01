package edu.uci.ics.luci.TerraTower;

import net.minidev.json.JSONObject;

public class TTEventCreateMap implements TTEvent{

	private String worldName;
	private String password;
	private double left;
	private double right;
	private int numXSplits;
	private double bottom;
	private double top;
	private int numYSplits;

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public TTEventCreateMap(String worldName, String password, double left, double right, int numXSplits, double bottom, double top, int numYSplits) {
		this.setWorldName(worldName);
		this.setPassword(password);
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
		ret.put("world_name", this.getWorldName());
		ret.put("password", this.getPassword());
		ret.put("left", this.getLeft());
		ret.put("right", this.getRight());
		ret.put("numXSplits", this.getNumXSplits());
		ret.put("top", this.getTop());
		ret.put("bottom", this.getBottom());
		ret.put("numYSplits", this.getNumYSplits());
		
		return ret;
	}
	
	static public TTEventCreateMap fromJSON(JSONObject in) {
		String name = (String) in.get("world_name");
		String password = (String) in.get("password");
		double left = (double) in.get("left");
		double right = (double) in.get("right");
		int numXSplits = (int) in.get("numXSplits");
		double top = (double) in.get("top");
		double bottom = (double) in.get("bottom");
		int numYSplits = (int) in.get("numYSplits");
		
		return(new TTEventCreateMap(name,password,left,right,numXSplits,bottom,top,numYSplits));
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
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		temp = Double.doubleToLongBits(right);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(top);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((worldName == null) ? 0 : worldName.hashCode());
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
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (Double.doubleToLongBits(right) != Double
				.doubleToLongBits(other.right)) {
			return false;
		}
		if (Double.doubleToLongBits(top) != Double.doubleToLongBits(other.top)) {
			return false;
		}
		if (worldName == null) {
			if (other.worldName != null) {
				return false;
			}
		} else if (!worldName.equals(other.worldName)) {
			return false;
		}
		return true;
	}

	
}
