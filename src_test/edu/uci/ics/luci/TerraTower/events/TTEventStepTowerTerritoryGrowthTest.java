package edu.uci.ics.luci.TerraTower.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;

public class TTEventStepTowerTerritoryGrowthTest {

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
			TTEventStepTowerTerritoryGrowth tt = new TTEventStepTowerTerritoryGrowth(worldName,worldPassword);
			assertEquals(tt,TTEventStepTowerTerritoryGrowth.fromJSON(tt.toJSON()));
			
			tt = new TTEventStepTowerTerritoryGrowth(worldName,PasswordUtils.hashPassword(worldPassword));
			assertEquals(tt,TTEventStepTowerTerritoryGrowth.fromJSON(tt.toJSON()));
		}
		catch(RuntimeException e){
			fail("This shouldn't fail\n"+e);
		}
	}

}
