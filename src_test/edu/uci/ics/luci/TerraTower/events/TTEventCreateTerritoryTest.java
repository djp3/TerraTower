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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TTEventCreateTerritoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testEquals() {
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		TTEventCreateTerritory t1 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreateTerritory t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		TTEventCreateTerritory t3 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName+"x",password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(null,password,left,right,numXSplits,bottom,top,numYSplits);
		t3 = new TTEventCreateTerritory(null,password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password+"x",left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateTerritory(worldName,(String)null,left,right,numXSplits,bottom,top,numYSplits);
		t3 = new TTEventCreateTerritory(worldName,(String)null,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,(left+right)/2.0,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,null,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,(left+right)/2.0,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,null,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits+1,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,null,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,(top+bottom)/2,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,null,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,(top+bottom)/2,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,null,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits+1);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,null);
		assertTrue(!t1.equals(t2));
		assertTrue(!t2.equals(t1));
		assertTrue(t1.hashCode()!=t2.hashCode());
	}

	@Test
	public void testBasic() {
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
	}
	
	@Test
	public void testJSON() {
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		TTEventCreateTerritory t1 = new TTEventCreateTerritory(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		
		assertEquals(t1,TTEventCreateTerritory.fromJSON(t1.toJSON()));
	}

}
