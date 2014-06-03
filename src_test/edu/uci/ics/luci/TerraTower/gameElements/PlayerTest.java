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

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class PlayerTest {
	
	static final String playerName = "name"+System.currentTimeMillis();
	static final String playerPassword = "password"+System.currentTimeMillis();

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
		Player player= null;
		try{
			player = new Player(playerName,PasswordUtils.hashPassword(playerPassword));
		}
		catch(RuntimeException e){
			fail("This shouldn't fail");
		}
		Player player2 = new Player(playerName,PasswordUtils.hashPassword(playerPassword));
		assertEquals(player, player2);
		assertEquals(player2, player);
		assertTrue(player.hashCode() == player2.hashCode());
		assertEquals(player.getPlayerName(),player2.getPlayerName());
		assertEquals(player.getTowerDelay(),player2.getTowerDelay());
		assertEquals(player.getLastTowerPlacedTime(),player2.getLastTowerPlacedTime());
		assertTrue(Arrays.equals(player.getHashedPassword(),player2.getHashedPassword()));
	}

}
