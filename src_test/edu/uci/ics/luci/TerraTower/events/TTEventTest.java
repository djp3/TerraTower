package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventTest {

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
		String worldName = "earth";
		String worldPassword = "password";
		
		TTEvent x = new TTEvent((String)null,(String)null);
		TTEvent y = new TTEvent((String)null,(String)null);
		
		assertEquals(x.getWorldName(),null);
		assertTrue(Arrays.equals(x.getWorldHashedPassword(),new byte[0]));
		assertEquals(x,TTEvent.fromJSON(x.toJSON()));
		
		assertEquals(x,y);
		assertEquals(x.hashCode(),y.hashCode());
		
		x = new TTEvent((String)null,(byte[])null);
		y = new TTEvent((String)null,(byte[])null);
		
		assertEquals(x.getWorldName(),null);
		assertTrue(Arrays.equals(x.getWorldHashedPassword(),new byte[0]));
		assertEquals(x,TTEvent.fromJSON(x.toJSON()));
		
		assertEquals(x,y);
		assertEquals(x.hashCode(),y.hashCode());
		
		
		x = new TTEvent(worldName,worldPassword);
		y = new TTEvent(worldName,worldPassword);
		
		assertEquals(x.getWorldName(),worldName);
		assertTrue(Arrays.equals(x.getWorldHashedPassword(),PasswordUtils.hashPassword(worldPassword)));
		assertEquals(x,TTEvent.fromJSON(x.toJSON()));
		
		assertEquals(x,y);
		assertEquals(x.hashCode(),y.hashCode());
		
		assertTrue(!x.equals(null));
		assertTrue(!x.equals("foo"));
		assertTrue(x.equals(x));
		
		x = new TTEvent(worldName,worldPassword);
		y = new TTEvent(worldName,worldPassword+"x");
		assertTrue(!x.equals(y));
		assertTrue(!y.equals(x));
		assertTrue(x.hashCode() != y.hashCode());
		
		x = new TTEvent(null,worldPassword);
		y = new TTEvent(worldName,worldPassword);
		assertTrue(!x.equals(y));
		assertTrue(!y.equals(x));
		assertTrue(x.hashCode() != y.hashCode());
	}

}
