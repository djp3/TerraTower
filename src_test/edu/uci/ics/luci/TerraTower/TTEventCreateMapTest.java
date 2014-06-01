package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TTEventCreateMapTest {

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
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		TTEventCreateMap t1 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		assertEquals(t1,t1);
		assertTrue(!t1.equals(null));
		assertTrue(!t1.equals("string"));
		
		TTEventCreateMap t2 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		TTEventCreateMap t3 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t1.equals(t2));
		assertTrue(t1.hashCode()==t2.hashCode());
		
		t2 = new TTEventCreateMap(worldName+"x",password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		
		t2 = new TTEventCreateMap(null,password,left,right,numXSplits,bottom,top,numYSplits);
		t3 = new TTEventCreateMap(null,password,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateMap(worldName,password+"x",left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,null,left,right,numXSplits,bottom,top,numYSplits);
		t3 = new TTEventCreateMap(worldName,null,left,right,numXSplits,bottom,top,numYSplits);
		assertTrue(t2.equals(t3));
		assertTrue(t2.hashCode()==t3.hashCode());
		assertTrue(!t1.equals(t2));
		assertTrue(t1.hashCode()!=t2.hashCode());
		assertTrue(!t2.equals(t1));
		assertTrue(t2.hashCode()!=t1.hashCode());
		
		t2 = new TTEventCreateMap(worldName,password,(left+right)/2.0,right,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,password,left,(left+right)/2.0,numXSplits,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,password,left,right,numXSplits+1,bottom,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,password,left,right,numXSplits,(top+bottom)/2,top,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,(top+bottom)/2,numYSplits);
		assertTrue(!t1.equals(t2));
		
		t2 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits+1);
		assertTrue(!t1.equals(t2));

	}

	@Test
	public void testBasic() {
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
	}
	
	@Test
	public void testJSON() {
		String worldName = "name";
		String password = "password";
		double left = -90.0;
		double right = 90.0;
		int numXSplits = 10;
		double bottom = -180.0;
		double top = 180.0;
		int numYSplits = 10;
		TTEventCreateMap t1 = new TTEventCreateMap(worldName,password,left,right,numXSplits,bottom,top,numYSplits);
		
		assertEquals(t1,TTEventCreateMap.fromJSON(t1.toJSON()));
	}

}
