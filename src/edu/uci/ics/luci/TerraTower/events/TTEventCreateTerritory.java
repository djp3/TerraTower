/*
	Copyright 2014-2015
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
package edu.uci.ics.luci.TerraTower.events;

import edu.uci.ics.luci.TerraTower.PasswordUtils;
import net.minidev.json.JSONObject;

public class TTEventCreateTerritory extends TTEvent{

	private Double left;
	private Double right;
	private Integer numXSplits;
	private Double bottom;
	private Double top;
	private Integer numYSplits;

	public Double getLeft() {
		return left;
	}

	public void setLeft(Double left) {
		this.left = left;
	}

	public Double getRight() {
		return right;
	}

	public void setRight(Double right) {
		this.right = right;
	}

	public Integer getNumXSplits() {
		return numXSplits;
	}

	public void setNumXSplits(Integer numXSplits) {
		this.numXSplits = numXSplits;
	}

	public Double getBottom() {
		return bottom;
	}

	public void setBottom(Double bottom) {
		this.bottom = bottom;
	}

	public Double getTop() {
		return top;
	}

	public void setTop(Double top) {
		this.top = top;
	}

	public Integer getNumYSplits() {
		return numYSplits;
	}

	public void setNumYSplits(Integer numYSplits) {
		this.numYSplits = numYSplits;
	}

	public TTEventCreateTerritory(String worldName, String worldPassword, Double left, Double right, Integer numXSplits, Double bottom, Double top, Integer numYSplits) {
		this(worldName,PasswordUtils.hashPassword(worldPassword),left,right,numXSplits,bottom,top,numYSplits);
	}
	
	public TTEventCreateTerritory(String worldName, byte[] worldHashedPassword, Double left, Double right, Integer numXSplits, Double bottom, Double top, Integer numYSplits) {
		super(worldName,worldHashedPassword);
		this.setLeft(left);
		this.setRight(right);
		this.setNumXSplits(numXSplits);
		this.setBottom(bottom);
		this.setTop(top);
		this.setNumYSplits(numYSplits);
	}
	

	@Override
	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("left", ""+this.getLeft());
		ret.put("right", ""+this.getRight());
		ret.put("numXSplits", ""+this.getNumXSplits());
		ret.put("top", ""+this.getTop());
		ret.put("bottom", ""+this.getBottom());
		ret.put("numYSplits", ""+this.getNumYSplits());
		
		return ret;
	}
	
	static public TTEventCreateTerritory fromJSON(JSONObject in) {
		TTEvent parent = TTEvent.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		
		Double left = null;
		String _left =  (String) in.get("left");
		if(_left != null){
			try{
				left = Double.parseDouble(_left);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Double right = null;
		String _right = (String) in.get("right");
		if(_right !=null){
			try{
				right = Double.parseDouble(_right);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Integer numXSplits = null;
		String _numXSplits = (String) in.get("numXSplits");
		if(_numXSplits != null){
			try{
				numXSplits = Integer.parseInt(_numXSplits);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Double top = null;
		String _top =  (String) in.get("top");
		if(_top != null){
			try{
				top = Double.parseDouble(_top);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Double bottom = null;
		String _bottom = (String) in.get("bottom");
		if(_bottom !=null){
			try{
				bottom = Double.parseDouble(_bottom);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Integer numYSplits = null;
		String _numYSplits = (String) in.get("numYSplits");
		if(_numYSplits != null){
			try{
				numYSplits = Integer.parseInt(_numYSplits);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		return(new TTEventCreateTerritory(worldName,worldHashedPassword,left,right,numXSplits,bottom,top,numYSplits));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bottom == null) ? 0 : bottom.hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result
				+ ((numXSplits == null) ? 0 : numXSplits.hashCode());
		result = prime * result
				+ ((numYSplits == null) ? 0 : numYSplits.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((top == null) ? 0 : top.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TTEventCreateTerritory)) {
			return false;
		}
		TTEventCreateTerritory other = (TTEventCreateTerritory) obj;
		if (bottom == null) {
			if (other.bottom != null) {
				return false;
			}
		} else if (!bottom.equals(other.bottom)) {
			return false;
		}
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (numXSplits == null) {
			if (other.numXSplits != null) {
				return false;
			}
		} else if (!numXSplits.equals(other.numXSplits)) {
			return false;
		}
		if (numYSplits == null) {
			if (other.numYSplits != null) {
				return false;
			}
		} else if (!numYSplits.equals(other.numYSplits)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		if (top == null) {
			if (other.top != null) {
				return false;
			}
		} else if (!top.equals(other.top)) {
			return false;
		}
		return true;
	}


	
}
