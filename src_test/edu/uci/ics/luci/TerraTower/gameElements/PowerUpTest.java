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

public class PowerUpTest {
	
	final static String code = "alkdsfj";

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
	public void testNulls() {
		PowerUp powerUpA;
		PowerUp powerUpB;
		try{
			powerUpA = new PowerUp(null,null,null,null,false);
			powerUpB = new PowerUp(null,null,null,null,false);
			assertTrue(powerUpA.equals(powerUpB));
			assertTrue(powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() == powerUpB.hashCode());
		}
		catch(RuntimeException e){
			fail("Shouldn't throw an exception");
		}
	}


	@Test
	public void test() {
		PowerUp powerUpA;
		PowerUp powerUpB;
		try{
			powerUpA = new PowerUp(code, -1000L,-1000L,-1000L,false);
			powerUpB = new PowerUp(code, -1000L,-1000L,-1000L,false);
			
			assertTrue(!powerUpA.equals(null));
			assertTrue(!powerUpA.equals("foo"));
			assertTrue(powerUpA.equals(powerUpA));
			assertTrue(powerUpA.equals(powerUpB));
			assertTrue(powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() == powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			
			powerUpB.setBombDelayDelta(0L);
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			powerUpB.setBombDelayDelta(powerUpA.getBombDelayDelta());
			
			powerUpB.setTowerDelayDelta(0L);
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			powerUpB.setTowerDelayDelta(powerUpA.getTowerDelayDelta());
			
			powerUpB.setBombFuseDelta(0L);
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			powerUpB.setBombFuseDelta(powerUpA.getBombFuseDelta());
			
			powerUpB.setCode("hello");
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			powerUpB.setCode(powerUpA.getCode());
			
			powerUpB.setCode(null); //Null is captured and turned to ""
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertTrue(powerUpB.equals(PowerUp.fromJSON(powerUpB.toJSON())));
			powerUpB.setCode(powerUpA.getCode());
			
			powerUpA.setRedeemed(false);
			powerUpB.setRedeemed(true);
			assertTrue(!powerUpA.equals(powerUpB));
			assertTrue(!powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() != powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			powerUpB.setRedeemed(powerUpA.getRedeemed());
			
			powerUpA.setCode(null);
			powerUpB.setCode(null);//Nulls get translated to ""
			assertTrue(powerUpA.equals(powerUpB));
			assertTrue(powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() == powerUpB.hashCode());
			assertTrue(powerUpB.equals(PowerUp.fromJSON(powerUpB.toJSON())));
			powerUpA.setCode("code");
			powerUpB.setCode("code");
			
			assertTrue(powerUpA.equals(powerUpB));
			assertTrue(powerUpB.equals(powerUpA));
			assertTrue(powerUpA.hashCode() == powerUpB.hashCode());
			assertEquals(powerUpB,PowerUp.fromJSON(powerUpB.toJSON()));
			
		}
		catch(RuntimeException e){
			fail("Shouldn't throw an exception");
		}
	}

}
