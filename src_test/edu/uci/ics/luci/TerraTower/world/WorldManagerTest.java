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
package edu.uci.ics.luci.TerraTower.world;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;
import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.PowerUp;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;

public class WorldManagerTest {

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
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager(password);
		
		assertTrue(wm.passwordGood(password));
		assertTrue(wm.passwordGood(PasswordUtils.hashPassword(password)));
		
		Player player = wm.getPlayer("player"+name,password);
		assertTrue(player == null);
		
		player = wm.getPlayer("player"+name,PasswordUtils.hashPassword(password));
		assertTrue(player == null);
		
		player = wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password));
		assertTrue(player != null);
		
		assertTrue(wm.playerExists("player"+name));
		
		assertTrue(null==wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password)));
		
		player = wm.getPlayer("player"+name,password+"x");
		assertTrue(player == null);
		player = wm.getPlayer("player"+name,"player"+password);
		assertTrue(player != null);
		player = wm.getPlayer("player"+name,PasswordUtils.hashPassword("playerx"+password));
		assertTrue(player == null);
		player = wm.getPlayer("player"+name,PasswordUtils.hashPassword("player"+password));
		assertTrue(player != null);
	}
	
	@Test
	public void testTower() {
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager(password);
		
		Territory territory = new Territory(-1.0, 1.0, 2, -1.0, 1.0, 2);
		wm.setTerritory(territory);
		assertEquals(territory,wm.getTerritory());
		assertTrue(!wm.towerPresent(0, 0));
		Player player = wm.createPlayer("player"+name, PasswordUtils.hashPassword(password));
		Tower t = new Tower(player,0,0);
		assertTrue(wm.addTower(t));
		assertTrue(!wm.addTower(t));
		
		assertTrue(wm.towerPresent(0, 0));
	}
	
	@Test
	public void testBomb() {
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager(password);
		
		Territory territory = new Territory(-1.0, 1.0, 2, -1.0, 1.0, 2);
		wm.setTerritory(territory);
		assertTrue(wm.numBombsPresent(0, 0) == 0);
		Player player = wm.createPlayer("player"+name, PasswordUtils.hashPassword(password));
		Bomb b = new Bomb(player,0,0,5,5);
		assertTrue(wm.addBomb(b));
		assertTrue(wm.addBomb(b));
		
		assertTrue(wm.numBombsPresent(0, 0) == 2);
	}
	
	
	@Test
	public void testPowerUp() {
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager(password);
		
		assertTrue(!wm.powerUpExists("code"));
		PowerUp pup = new PowerUp("code",0L,0L,0L,false);
		assertTrue(wm.createPowerUp(pup)!= null);
		assertTrue(wm.powerUpExists("code"));
		assertTrue(wm.getPowerUp("code").getBombDelayDelta() == 0L);
		assertTrue(wm.createPowerUp(pup)== null);
	}

}
