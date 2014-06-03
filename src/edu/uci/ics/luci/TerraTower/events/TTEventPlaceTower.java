package edu.uci.ics.luci.TerraTower.events;

import net.minidev.json.JSONObject;
import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventPlaceTower extends TTEventPlayer{
	
	private Double lat;
	private Double lng;
	private Double alt;
	
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getAlt() {
		return alt;
	}

	public void setAlt(Double alt) {
		this.alt = alt;
	}
	
	public TTEventPlaceTower(String worldName, String worldPassword, String playerName, String playerPassword,Double lat,Double lng,Double alt){
		this(worldName,PasswordUtils.hashPassword(worldPassword), playerName,PasswordUtils.hashPassword(playerPassword),lat,lng,alt);
	}
	
	public TTEventPlaceTower(String worldName, byte[] worldHashedPassword,String playerName, byte[] playerHashedPassword,Double lat,Double lng,Double alt){
		super(worldName,worldHashedPassword,playerName,playerHashedPassword);
		this.setLat(lat);
		this.setLng(lng);
		this.setAlt(alt);
	}

	@Override
	public JSONObject toJSON() {
		JSONObject ret = super.toJSON();
		ret.put("lat", ""+this.getLat());
		ret.put("lng", ""+this.getLng());
		ret.put("alt", ""+this.getAlt());
		return ret;
	}
	
	static public TTEventPlaceTower fromJSON(JSONObject in) {
		TTEventPlayer parent = TTEventPlayer.fromJSON(in);
		String worldName = parent.getWorldName();
		byte[] worldHashedPassword = parent.getWorldHashedPassword();
		String playerName = parent.getPlayerName();
		byte[] playerHashedPassword = parent.getPlayerHashedPassword();
		Double lat = null;
		String _lat =  (String) in.get("lat");
		if(_lat != null){
			try{
				lat = Double.parseDouble(_lat);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Double lng = null;
		String _lng =  (String) in.get("lng");
		if(_lng != null){
			try{
				lng = Double.parseDouble(_lng);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
		Double alt = null;
		String _alt =  (String) in.get("alt");
		if(_alt != null){
			try{
				alt = Double.parseDouble(_alt);
			}
			catch(NumberFormatException e){
				//null it is then.
			}
		}
		
			
		return(new TTEventPlaceTower(worldName,worldHashedPassword,playerName,playerHashedPassword,lat,lng,alt));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((alt == null) ? 0 : alt.hashCode());
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
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
		if (!(obj instanceof TTEventPlaceTower)) {
			return false;
		}
		TTEventPlaceTower other = (TTEventPlaceTower) obj;
		if (alt == null) {
			if (other.alt != null) {
				return false;
			}
		} else if (!alt.equals(other.alt)) {
			return false;
		}
		if (lat == null) {
			if (other.lat != null) {
				return false;
			}
		} else if (!lat.equals(other.lat)) {
			return false;
		}
		if (lng == null) {
			if (other.lng != null) {
				return false;
			}
		} else if (!lng.equals(other.lng)) {
			return false;
		}
		return true;
	}

	
}
