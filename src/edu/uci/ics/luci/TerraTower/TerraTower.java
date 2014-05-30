/*
	Copyright 2014
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

package edu.uci.ics.luci.TerraTower;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.utility.Globals;
import edu.uci.ics.luci.utility.webserver.AccessControl;
import edu.uci.ics.luci.utility.webserver.HandlerAbstract;
import edu.uci.ics.luci.utility.webserver.RequestDispatcher;
import edu.uci.ics.luci.utility.webserver.WebServer;
import edu.uci.ics.luci.utility.webserver.WebUtil;
import edu.uci.ics.luci.utility.webserver.handlers.HandlerVersion;

public class TerraTower {
	private static int port = 9020;
	
	final static String VERSION="0.1";

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(TerraTower.class);
		}
		return log;
	}
	
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
			
			Globals.getGlobals().addQuittable(ws);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			Globals.getGlobals().setQuitting(true);
		}
		
		
		getLog().info("Waiting 10 seconds");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			HashMap<String, String> params = new HashMap<String, String>();

			WebUtil.fetchWebPage("http://localhost:" + ws.getPort() + "/shutdown", false, params, 30 * 1000);
		} catch (MalformedURLException e) {
			getLog().error(e.toString());
		} catch (IOException e) {
			getLog().error(e.toString());
		}
		
	}
		
}

