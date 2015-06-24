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

public class TTEventBuildTowerTest {
	static final String worldName = "name";
	static final String worldPassword = "password";
	static final String playerName = "name";
	static final String playerPassword = "password";

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
		
		TTEventBuildTower t1 = new TTEventBuildTower(worldName,worldPassword,playerName,playerPassword,0.0,0.0,0.0);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventBuildTower t2 = new TTEventBuildTower(worldName,worldPassword,playerName,playerPassword,5.0,5.0,5.0);
		TTEventBuildTower t3 = new TTEventBuildTower(worldName,worldPassword,playerName,playerPassword,5.0,5.0,5.0);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		
		t3.setAlt(null);
		assertTrue(!t2.equals(t3));
		assertTrue(!t3.equals(t2));
		assertTrue(t2.hashCode() != t3.hashCode());
		t3.setAlt(t2.getAlt());
		
		t3.setLat(null);
		assertTrue(!t2.equals(t3));
		assertTrue(!t3.equals(t2));
		assertTrue(t2.hashCode()!=t3.hashCode());
		t3.setLat(t2.getLat());
		
		t3.setLng(null);
		assertTrue(!t2.equals(t3));
		assertTrue(!t3.equals(t2));
		assertTrue(t2.hashCode()!=t3.hashCode());
		t3.setLng(t2.getLng());
		
		
	}

	@Test
	public void testBasic() {
		new TTEventBuildTower(worldName,worldPassword,playerName,playerPassword,0.0,0.0,0.0);
	}
	
	@Test
	public void testJSON() {
		TTEventBuildTower t1 = new TTEventBuildTower(worldName,worldPassword,playerName,playerPassword,0.0,0.0,0.0);
		
		assertEquals(t1,TTEventBuildTower.fromJSON(t1.toJSON()));
	}

}
