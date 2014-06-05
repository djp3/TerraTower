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
package edu.uci.ics.luci.TerraTower.gameElements;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TowerTest {
	
	final static String playerName = "name";
	final static String playerPassword = "password";

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
	public void test() {
		Tower tower1;
		Tower tower2;
		Player player;
		try{
			player = new Player(playerName, PasswordUtils.hashPassword(playerPassword));
			tower1 = new Tower(player,0,0);
			tower2 = new Tower(player,0,0);
			assertEquals(tower1,tower1);
			assertEquals(tower1,tower2);
			assertEquals(tower2,tower1);
			assertEquals(tower1.hashCode(),tower2.hashCode());
			
			assertTrue(!tower1.equals(null));
			assertTrue(!tower1.equals("string"));
			
			tower2.setDestroyed(true);
			assertTrue(!tower1.equals(tower2));
			assertTrue(!tower2.equals(tower1));
			assertTrue(tower1.hashCode() != tower2.hashCode());
			tower2.setDestroyed(tower1.isDestroyed());
			
			tower2.setOwner(null);
			assertTrue(!tower1.equals(tower2));
			assertTrue(!tower2.equals(tower1));
			assertTrue(tower1.hashCode() != tower2.hashCode());
			tower2.setOwner(tower1.getOwner());
			
			tower1.setOwner(null);
			tower2.setOwner(null);
			assertTrue(tower1.equals(tower2));
			assertTrue(tower2.equals(tower1));
			assertTrue(tower1.hashCode() == tower2.hashCode());
			tower1.setOwner(player);
			tower2.setOwner(player);
			
			tower2.setX(19);
			assertTrue(!tower1.equals(tower2));
			assertTrue(!tower2.equals(tower1));
			assertTrue(tower1.hashCode() != tower2.hashCode());
			tower2.setX(tower1.getX());
			
			tower2.setY(19);
			assertTrue(!tower1.equals(tower2));
			assertTrue(!tower2.equals(tower1));
			assertTrue(tower1.hashCode() != tower2.hashCode());
			tower2.setY(tower1.getY());
			
			
		}
		catch(RuntimeException e){
			fail("Shouldn't throw an exception");
		}
	}

}
