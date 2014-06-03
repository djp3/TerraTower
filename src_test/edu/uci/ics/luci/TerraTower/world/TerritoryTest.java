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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TerritoryTest {

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
	public void testConstructor() {
		
		//x out of order
		try{
			new Territory(90.0,-90.0,100,0,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//x same 
		try{
			new Territory(90.0,90.0,100,0,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//y out of order 
		try{
			new Territory(-90.0,90.0,100,10,-10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//y same
		try{
			new Territory(-90.0,90.0,100,10,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//bad xsplits 
		try{
			new Territory(-90.0,90.0,-100,-10,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//bad ysplits 
		try{
			new Territory(-90.0,90.0,100,-10,10,-100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//good
		try{
			new Territory(-90.0,90.0,100,-180.0,180.0,100);
		}
		catch(IllegalArgumentException e){
			fail("Shouldn't throw exception");
		}
	}
	
	@Test
	public void testXIndex() {
		Territory map = new Territory(-90.0,90.0,2,-180.0,180.0,2);
		try{
			map.xIndex(-90.0001);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		assertEquals(0,map.xIndex(-90.0));
		assertEquals(1,map.xIndex(0.0));
		assertEquals(1,map.xIndex(90.0));
		try{
			map.xIndex(90.000001);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
	}
	
	@Test
	public void testYIndex() {
		Territory map = new Territory(-90.0,90.0,2,-180.0,180.0,2);
		
		try{
			map.yIndex(-180.00001);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		assertEquals(0,map.yIndex(-180.0));
		assertEquals(1,map.yIndex(0.0));
		assertEquals(1,map.yIndex(180.0));
		try{
			map.yIndex(180.00001);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
	}
	
	
	
	@Test
	public void testIndex() {
		Territory map = new Territory(-90.0,90.0,2,-180.0,180.0,2);
		assertEquals(0,map.index(-90, -180.0).getX());
		assertEquals(1,map.index(90.0, -180.0).getX());
		assertEquals(0,map.index(-90.0, 180.0).getX());
		assertEquals(1,map.index(90.0, 180.0).getX());
		
		assertEquals(0,map.index(-90.0, -180.0).getY());
		assertEquals(1,map.index(-90.0, 180.0).getY());
		assertEquals(0,map.index(90.0, -180.0).getY());
		assertEquals(1,map.index(90.0, 180.0).getY());
	}

}
