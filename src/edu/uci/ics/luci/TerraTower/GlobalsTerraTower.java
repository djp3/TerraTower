/*
	Copyright 2007-2014
		University of California, Irvine (c/o Donald J. Patterson)
*/
/*
	This file is part of the Laboratory for Ubiquitous Computing java Utility package, i.e. "Utilities"

    Utilities is free software: you can redistribute it and/or modify
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


import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.CalendarCache;
import edu.uci.ics.luci.utility.Globals;

public class GlobalsTerraTower extends Globals {
	
	static{
        /* Test that we are using GMT as the default */
        if(!TimeZone.getDefault().equals(CalendarCache.TZ_GMT)){
                throw new RuntimeException("We are in the wrong timezone:\n"+TimeZone.getDefault()+"\n We want to be in:\n "+CalendarCache.TZ_GMT);
        }

        /* Test that we are using UTF-8 as default */
        String c = java.nio.charset.Charset.defaultCharset().name();
        if(!c.equals("UTF-8")){
                throw new IllegalArgumentException("The character set is not UTF-8:"+c);
        }

	}
	
	private static final String LOG4J_CONFIG_FILE_DEFAULT = "TerraTower.log4j.xml";
	

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(GlobalsTerraTower.class);
		}
		return log;
	}
	String version = null;
	
	WorldManager wm = null;

	@Override
	public String getSystemVersion() {
		return version;
	}
	
	public GlobalsTerraTower(String version){
		this(version,true);
	}
	
	static GlobalsTerraTower getGlobalsTerraTower(){
		return (GlobalsTerraTower) Globals.getGlobals();
	}
	
	protected GlobalsTerraTower(String version, boolean testing){
		super();
		this.version = version;
		setTesting(true);
		setLog4JPropertyFileName(LOG4J_CONFIG_FILE_DEFAULT);
	}
	
	public void setWorldManager(WorldManager wm){
		this.wm = wm;
	}
	
	public WorldManager getWorldManager(){
		return(this.wm);
	}
	
}
