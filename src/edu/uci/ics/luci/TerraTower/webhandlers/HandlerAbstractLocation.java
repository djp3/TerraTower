package edu.uci.ics.luci.TerraTower.webhandlers;

import java.util.Map;

import net.minidev.json.JSONArray;
import edu.uci.ics.luci.TerraTower.TTEventWrapperQueuer;

public abstract class HandlerAbstractLocation extends HandlerAbstractPlayer{
	
	private Double lat;
	private Double lng;
	private Double alt;

	public HandlerAbstractLocation(TTEventWrapperQueuer eventPublisher) {
		super(eventPublisher);
	}

	protected Double getLat() {
		return lat;
	}

	protected Double getLng() {
		return lng;
	}

	protected Double getAlt() {
		return alt;
	}

	protected JSONArray getLocationParameters(String restFunction, Map<String,String> parameters){
		JSONArray errors = new JSONArray();
		
		String _lat = parameters.get("lat");
		lat = null;
		if(_lat == null){
			errors.add("Problem handling "+restFunction+": lat was null");

		}
		else{
			try{
				lat = Double.parseDouble(_lat);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": lat was not a double");
			}
		}
		
		
		String _lng = parameters.get("lng");
		lng = null;
		if(_lng == null){
			errors.add("Problem handling "+restFunction+": lng was null");
		}
		else{
			try{
				lng = Double.parseDouble(_lng);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": lng was not a double");
			}
		}
		
		
		String _alt = parameters.get("alt");
		alt = null;
		if(_alt == null){
			errors.add("Problem handling "+restFunction+": alt was null");
		}
		else{
			try{
				alt = Double.parseDouble(_lat);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": alt was not a double");
			}
		}
		
		return errors;
	}

}
