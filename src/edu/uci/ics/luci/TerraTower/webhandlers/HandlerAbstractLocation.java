package edu.uci.ics.luci.TerraTower.webhandlers;

import java.util.Map;
import java.util.Set;

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
	
	protected JSONArray getLocationParameters(String restFunction,Map<String,Set<String>> parameters){
		JSONArray errors = new JSONArray();
		
		Set<String> _latS = parameters.get("lat");
		String latS = null;
		if((_latS == null) || ((latS = (_latS.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+": lat was null");
		}
		
		lat = null;
		if(latS == null){
			errors.add("Problem handling "+restFunction+": lat was null");

		}
		else{
			try{
				lat = Double.parseDouble(latS);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": lat was not a double");
			}
		}
		
		Set<String> _lngS = parameters.get("lng");
		String lngS = null;
		if((_lngS == null) || ((lngS = (_lngS.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+": lng was null");
		}
		
		lng = null;
		if(lngS == null){
			errors.add("Problem handling "+restFunction+": lng was null");

		}
		else{
			try{
				lng = Double.parseDouble(lngS);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": lng was not a double");
			}
		}
		
		
		
		Set<String> _altS = parameters.get("alt");
		String altS = null;
		if((_altS == null) || ((altS = (_altS.iterator().next()))==null)){
			errors.add("Problem handling "+restFunction+": alt was null");
		}
		
		alt = null;
		if(altS == null){
			errors.add("Problem handling "+restFunction+": alt was null");

		}
		else{
			try{
				alt = Double.parseDouble(altS);
			}
			catch(NumberFormatException e){
				errors.add("Problem handling "+restFunction+": alt was not a double");
			}
		}
		
		
		return errors;
	}

}
