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

package edu.uci.ics.luci.TerraTower;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordUtils {
	

	private static transient volatile Logger log = null;
	public static Logger getLog(){
		if(log == null){
			log = LogManager.getLogger(PasswordUtils.class);
		}
		return log;
	}

	private static MessageDigest digest = null;
	
	public static byte[] hashPassword(String password) {
		byte[] hash = null;
		try {
			if(password != null){
				hash = digest.digest(password.getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			getLog().fatal("Can't support UTF-8\n"+e);
		}
		return hash;
	}
	
	static{
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			getLog().fatal("Can't find SHA-256 algorithm:\n"+e);
		}
	}
	
	final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHexString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public static boolean checkPassword(String password, byte[] p) {
		if((password == null) && (p==null)){
			return true;
		}
		return (Arrays.equals(p,PasswordUtils.hashPassword(password)));
	}
	

	public static boolean checkPassword(byte[] a, byte[] b) {
		if((a == null) && (b==null)){
			return true;
		}
		return (Arrays.equals(a,b));
	}


}
