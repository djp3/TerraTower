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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class BombTest {
	
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
		Bomb bomb1;
		Bomb bomb2;
		Player player;
		try{
			player = new Player(playerName, PasswordUtils.hashPassword(playerPassword));
			bomb1 = new Bomb(player,0,0,0,0);
			bomb2 = new Bomb(player,0,0,0,0);
			assertTrue(!bomb1.equals(null));
			assertTrue(!bomb1.equals("foo"));
			assertTrue(bomb1.equals(bomb1));
			assertEquals(bomb1,bomb2);
			assertEquals(bomb2,bomb1);
			assertEquals(bomb1.hashCode(),bomb2.hashCode());
			
			bomb2.setExplosionTime(1);
			assertTrue(!bomb1.equals(bomb2));
			bomb2.setExplosionTime(bomb1.getExplosionTime());
			
			bomb2.setStrength(1);
			assertTrue(!bomb1.equals(bomb2));
			assertTrue(!bomb2.equals(bomb1));
			bomb2.setStrength(bomb1.getStrength());
			
			bomb2.setOwner(null);
			assertTrue(!bomb1.equals(bomb2));
			assertTrue(!bomb2.equals(bomb1));
			bomb2.setOwner(bomb1.getOwner());
			
			Player owner = bomb2.getOwner();
			bomb2.setOwner(null);
			bomb1.setOwner(null);
			assertTrue(bomb1.equals(bomb2));
			assertTrue(bomb2.equals(bomb1));
			assertEquals(bomb1.hashCode(),bomb2.hashCode());
			bomb2.setOwner(owner);
			bomb1.setOwner(owner);
			
			bomb2.setX(1);
			assertTrue(!bomb1.equals(bomb2));
			assertTrue(!bomb2.equals(bomb1));
			bomb2.setX(bomb1.getX());
			
			bomb2.setY(1);
			assertTrue(!bomb1.equals(bomb2));
			assertTrue(!bomb2.equals(bomb1));
			bomb2.setY(bomb1.getY());
			
			bomb2.setExploded(true);
			assertTrue(!bomb1.equals(bomb2));
			assertTrue(!bomb2.equals(bomb1));
			bomb2.setExploded(bomb1.isExploded());
		}
		catch(RuntimeException e){
			fail("Shouldn't throw an exception");
		}
	}

}
