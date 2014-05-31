package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MapTest {

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
			new Map(90.0,-90.0,100,0,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//x same 
		try{
			new Map(90.0,90.0,100,0,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//y out of order 
		try{
			new Map(-90.0,90.0,100,10,-10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//y same
		try{
			new Map(-90.0,90.0,100,10,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//bad xsplits 
		try{
			new Map(-90.0,90.0,-100,-10,10,100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//bad ysplits 
		try{
			new Map(-90.0,90.0,100,-10,10,-100);
			fail("Should throw exception");
		}
		catch(IllegalArgumentException e){
			//ok
		}
		
		//good
		try{
			new Map(-90.0,90.0,100,-180.0,180.0,100);
		}
		catch(IllegalArgumentException e){
			fail("Shouldn't throw exception");
		}
	}
	
	@Test
	public void testXIndex() {
		Map map = new Map(-90.0,90.0,2,-180.0,180.0,2);
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
		Map map = new Map(-90.0,90.0,2,-180.0,180.0,2);
		
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
		Map map = new Map(-90.0,90.0,2,-180.0,180.0,2);
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
