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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventBurnBombFuseTest {

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
	public void test() {
		try{
			TTEventBurnBombFuse tt = new TTEventBurnBombFuse(worldName,worldPassword);
			assertEquals(tt,TTEventBurnBombFuse.fromJSON(tt.toJSON()));
			
			tt = new TTEventBurnBombFuse(worldName,PasswordUtils.hashPassword(worldPassword));
			assertEquals(tt,TTEventBurnBombFuse.fromJSON(tt.toJSON()));
		}
		catch(RuntimeException e){
			fail("This shouldn't fail\n"+e);
		}
	}

}
