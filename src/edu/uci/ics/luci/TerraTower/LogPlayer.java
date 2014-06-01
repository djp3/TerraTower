package edu.uci.ics.luci.TerraTower;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogPlayer implements Runnable{
	

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(LogPlayer.class);
		}
		return log;
	}
	
	private TTEventWrapperQueuer q;
	private TreeMap<Long, TTEventWrapper> events;
	private boolean realTime;
	private long numberEventsFired;
	private ArrayList<MyResultListener> results;

	public long getNumberEventsFired() {
		return numberEventsFired;
	}

	public void setNumberEventsFired(long numberEventsFired) {
		this.numberEventsFired = numberEventsFired;
	}
	
	public long getNumberEventsRemaining() {
		return events.size();
	}

	public ArrayList<MyResultListener> getResults() {
		return results;
	}

	LogPlayer(String logFileName, TTEventWrapperQueuer q,boolean realTime) throws IOException {
		this.q = q;
		this.realTime = realTime;
		
		results = new ArrayList<MyResultListener>();
		
		setNumberEventsFired(0);

		BufferedReader reader = null;
		try {
			Path newFile = Paths.get(logFileName);
			reader = Files.newBufferedReader(newFile, Charset.defaultCharset());

			events = new TreeMap<Long, TTEventWrapper>();

			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				JSONObject logEntry = (JSONObject) JSONValue .parse(lineFromFile);
				long timestamp = Long.parseLong((String) logEntry.get("timestamp"));
				TTEventWrapper ew = TTEventWrapper.fromJSON((JSONObject) logEntry.get("event_wrapper"));
				events.put(timestamp,ew);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static class MyResultListener implements TTEventHandlerResultListener{
		
		private Boolean resultOK = null;
		private JSONArray errors = null;

		public Boolean getResultOK() {
			return resultOK;
		}

		private void setResultOK(Boolean resultOK) {
			this.resultOK = resultOK;
		}
		
		public JSONArray getErrors() {
			return errors;
		}

		public void setErrors(JSONArray errors) {
			this.errors = errors;
		}
		

		@Override
		public void onFinish(JSONObject result) {
			setResultOK(false);
			
			if(result.get("error").equals("true")){
				setResultOK(false);
				setErrors((JSONArray) result.get("errors"));
			}
			if(result.get("error").equals("false")){
				setResultOK(true);
			}
			
		}

	}

	@Override
	public void run() {
		long replayStart = System.currentTimeMillis();
		
		if(events.isEmpty()){
			getLog().info("No entries in log to play back");
			return;
		}
		
		Entry<Long, TTEventWrapper> first  = events.pollFirstEntry();
		long logStart = first.getKey();
		numberEventsFired++;
		q.onData(first.getValue());
		getLog().info("Starting log play back");
		
		while(!events.isEmpty()){
			Entry<Long, TTEventWrapper> current = events.pollFirstEntry();
			
			if(realTime){
				long realElapsed = System.currentTimeMillis() - replayStart;
				long simElapsed = current.getKey() - logStart;
				while(realElapsed < simElapsed){
					try {
						Thread.sleep(simElapsed - realElapsed);
					} catch (InterruptedException e) {
					}
					realElapsed = System.currentTimeMillis() - replayStart;
					simElapsed = current.getKey() - logStart;
				}
			}
			
			MyResultListener result = new MyResultListener();
			TTEventWrapper eventWrapper = current.getValue();
			eventWrapper.addResultListener(result);
			results.add(result);
			
			numberEventsFired++;
			q.onData(current.getValue());
		}
		
	}

}
