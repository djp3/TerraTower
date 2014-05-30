package edu.uci.ics.luci.TerraTower;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;

public class TerraTower {
	
	
	
	private static int port = 9020;
	

	final static String VERSION="0.1";
	
	public static void main(String[] args) throws ConfigurationException{
		
		Configuration config = new PropertiesConfiguration("TerraTower.properties");
		
		Globals.setGlobals(new GlobalsTerraTower(VERSION));
		
		WebServer ws = null;
		HashMap<String,HandlerAbstract> requestHandlerRegistry;
		
		try {
			requestHandlerRegistry = new HashMap<String, HandlerAbstract>();
			requestHandlerRegistry.put("",new HandlerVersion(VERSION));
			requestHandlerRegistry.put("version",new HandlerVersion(VERSION));
			requestHandlerRegistry.put("shutdown",new HandlerShutdown(Globals.getGlobals()));
	
			RequestDispatcher dispatcher = new RequestDispatcher(requestHandlerRegistry);
			ws = new WebServer(dispatcher, port, false, new AccessControl());
			ws.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Globals.getGlobals().addQuittables(ws);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			Globals.getGlobals().setQuitting(true);
		}
		
		
	}
		
}

