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
package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TTEventCreateWorldTest {

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
		TTEventCreateWorld t1 = new TTEventCreateWorld(worldName,password);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreateWorld t2 = new TTEventCreateWorld(worldName,password);
		TTEventCreateWorld t3 = new TTEventCreateWorld(worldName,password);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreateWorld(worldName+"x",password);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateWorld(null,password);
		t3 = new TTEventCreateWorld(null,password);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateWorld(worldName,password+"x");
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateWorld(worldName,(String)null);
		t3 = new TTEventCreateWorld(worldName,(String)null);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateWorld(worldName,(byte[])null);
		t3 = new TTEventCreateWorld(worldName,(byte[])null);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());

	}

	@Test
	public void testBasic() {
		String worldName = "name";
		String password = "password";
		new TTEventCreateWorld(worldName,password);
	}
	
	@Test
	public void testJSON() {
		String worldName = "name";
		String password = "password";
		TTEventCreateWorld t1 = new TTEventCreateWorld(worldName,password);
		
		assertEquals(t1,TTEventCreateWorld.fromJSON(t1.toJSON()));
	}

}
