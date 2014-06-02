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
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventPlayerTest {
	
	static final String worldName ="earth"; 
	static final String worldPassword ="earthpassword"; 
	static final String playerName ="player"; 
	static final String playerPassword ="playerpassword"; 

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
		
		TTEventPlayer p = new TTEventPlayer(worldName, worldPassword, playerName, playerPassword);
		TTEventPlayer q = new TTEventPlayer(worldName, worldPassword, playerName, playerPassword);
		try{
			p= new TTEventPlayer(worldName, worldPassword, playerName, playerPassword);
		}
		catch(RuntimeException e){
			fail("This should't fail");
		}
		
		assertEquals(p,p);
		assertEquals(p.hashCode(),p.hashCode());
		assertEquals(p,q);
		assertEquals(p.hashCode(),p.hashCode());
		assertEquals(p.getPlayerName(),playerName);
		assertTrue(Arrays.equals(p.getPlayerHashedPassword(),PasswordUtils.hashPassword(playerPassword)));
		
		p.setPlayerPassword(null);
		assertTrue(Arrays.equals(p.getPlayerHashedPassword(),new byte[0]));
		
		p.setPlayerPassword(playerPassword);
		assertTrue(Arrays.equals(p.getPlayerHashedPassword(),PasswordUtils.hashPassword(playerPassword)));
		
		assertEquals(p,TTEventPlayer.fromJSON(p.toJSON()));
		
		p = new TTEventPlayer(worldName, worldPassword, playerName, playerPassword);
		q = new TTEventPlayer(worldName, worldPassword, playerName, playerPassword);
		assertEquals(p,q);
		q.setPlayerName(playerName+"x");
		assertTrue(!p.equals(q));
		assertTrue(!q.equals(p));
		q.setPlayerName(null);
		assertTrue(!p.equals(q));
		assertTrue(!q.equals(p));
		q.setPlayerName(p.getPlayerName());
		q.setPlayerPassword(playerPassword+"x");
		assertTrue(!p.equals(q));
		assertTrue(!q.equals(p));
		
	}

}
