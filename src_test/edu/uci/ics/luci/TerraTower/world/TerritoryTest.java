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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.PasswordUtils;
import edu.uci.ics.luci.TerraTower.gameElements.Bomb;
import edu.uci.ics.luci.TerraTower.gameElements.Player;
import edu.uci.ics.luci.TerraTower.gameElements.Tower;
import edu.uci.ics.luci.utility.datastructure.Pair;

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
	public void testIndex1() {
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
	
	
	@Test
	public void testBasic() {
		
		try{
			new Territory(1.0,-1.0,10,-1.0,1.0,10);
			fail("this should break");
		}
		catch(RuntimeException e){
			//ok
		}
		try{
			new Territory(-1.0,1.0,-10,-1.0,1.0,10);
			fail("this should break");
		}
		catch(RuntimeException e){
			//ok
		}
		try{
			new Territory(-1.0,1.0,10,1.0,-1.0,10);
			fail("this should break");
		}
		catch(RuntimeException e){
			//ok
		}
		try{
			new Territory(-1.0,1.0,10,-1.0,1.0,-10);
			fail("this should break");
		}
		catch(RuntimeException e){
			//ok
		}
	}
	
	@Test
	public void testBounds() {
		
		Territory t = new Territory(-1.0,1.0,10,-1.0,1.0,10);
		assertTrue(t.xInBounds(0.0));
		assertTrue(!t.xInBounds(-1.5));
		assertTrue(!t.xInBounds(1.5));
		assertTrue(t.yInBounds(0.0));
		assertTrue(!t.yInBounds(-1.5));
		assertTrue(!t.yInBounds(1.5));
	}
	
	@Test
	public void testIndex() {
		
		Territory t = new Territory(-1.0,1.0,2,-1.0,1.0,2);
		assertTrue(t.xIndex(-0.5)==0);
		assertTrue(t.xIndex(1.0)==1);
		
		assertTrue(t.yIndex(-0.5)==0);
		assertTrue(t.yIndex(1.0)==1);
	}
	
	@Test
	public void testTower() {
		
		Territory t = new Territory(-1.0,1.0,2,-1.0,1.0,2);
		assertTrue(!t.towerPresent(0, 0));
		assertTrue(t.addTower(new Tower(new Player("name",PasswordUtils.hashPassword("password")),0, 0)));
		assertTrue(t.towerPresent(0, 0));
	}
	
	
	@Test
	public void testBomb() {
		
		Territory t = new Territory(-1.0,1.0,2,-1.0,1.0,2);
		assertTrue(t.numBombsPresent(0, 0) == 0);
		assertTrue(t.addBomb(new Bomb(new Player("name",PasswordUtils.hashPassword("password")),0, 0,5,5)));
		assertTrue(t.numBombsPresent(0, 0) == 1 );
	}
	
	@Test
	public void testAlts() {
		Territory t = new Territory(-1.0,1.0,2,-1.0,1.0,2);
		t.updateAltitude(0, 0, 0.0);
		t.updateAltitude(0, 0, 10.0);
		t.updateAltitude(0, 0, 20.0);
		assertTrue(t.index(0,0).estimateAltitude().equals(10.0));
	}

	@Test
	public void test() {
		
		String name = "foo"+System.currentTimeMillis();
		String password = "foo"+System.currentTimeMillis();
		WorldManager wm = new WorldManager(password);
		
		assertTrue(wm.passwordGood(password));
		assertTrue(!wm.passwordGood(password+"x"));
		
		Territory territory1 = new Territory(-1.0,1.0,10,-1.0,1.0,15);
		Territory territory2 = new Territory(-1.0,1.0,10,-1.0,1.0,15);
		assertTrue(territory1.equals(territory2));
		
		
		/*Trying to figure out why hashCodes aren't equal*/
		/* First check the gridcells*/
		for(int xx = 0; xx < territory1.getNumXSplits(); xx++){
			for(int yy = 0; yy < territory1.getNumYSplits(); yy++){
				try{
					assertEquals(territory1.index(xx, yy).hashCode(),territory2.index(xx, yy).hashCode());
				}
				catch(AssertionError e){
					System.err.println("Problem with xx = "+xx+",yy = "+yy);
					throw e;
				}
			}
		}
		/*Then check the leaderboard */
		assertEquals(territory1.getLeaderBoard().hashCode(),territory2.getLeaderBoard().hashCode());
		assertEquals(territory1.hashCode(),territory2.hashCode());
		
		wm.setTerritory(territory1);
		assertEquals(territory2,wm.getTerritory());
		
		assertTrue(null != wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password)));
		assertTrue(wm.playerExists("player"+name));
		
		assertTrue(null == wm.createPlayer("player"+name, PasswordUtils.hashPassword("player"+password)));
	}
	
	
	@Test
	public void testEquals() {
		
		Territory territory1 = new Territory(-1.0,1.0,10,-1.0,1.0,10);
		Territory territory2 = new Territory(-1.0,1.0,10,-1.0,1.0,10);
		assertTrue(!territory1.equals(null));
		assertTrue(!territory1.equals("foo"));
		assertTrue(territory1.equals(territory1));
		assertEquals(territory1.hashCode(),territory2.hashCode());
		
		territory2.setBottom(-2.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setBottom(territory1.getBottom());
		
		territory2.setTop(2.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setTop(territory1.getTop());
		
		territory2.setLeft(-2.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setLeft(territory1.getLeft());
		
		territory2.setRight(2.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setRight(territory1.getRight());
		
		territory2.setStepX(20.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setStepX(territory1.getStepX());
		
		territory2.setStepY(20.0);
		assertTrue(!territory1.equals(territory2));
		territory2.setStepY(territory1.getStepY());
		
		territory2.setNumXSplits(20);
		assertTrue(!territory1.equals(territory2));
		territory2.setNumXSplits(territory1.getNumXSplits());
		
		territory2.setNumYSplits(20);
		assertTrue(!territory1.equals(territory2));
		territory2.setNumYSplits(territory1.getNumYSplits());
		
		territory2.index(0,1).setX(1);
		assertTrue(!territory1.equals(territory2));
		
	}
	

	@Test
	public void testStepTowerTerritoryGrowth() {
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		Player player = new Player("a",PasswordUtils.hashPassword("b"));
		Tower tower = new Tower(player,0,0);
		t.addTower(tower);
		
		t.stepTowerTerritoryGrowth(5, 2);
		Pair<Player, Integer> owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
		
		owner = t.index(-5.0,-4.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		owner = t.index(-5.0,-3.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-2.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-1.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,1.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-3.5,-3.5).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
	}
	
	@Test
	public void testStepTowerTerritoryGrowth2() {
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		Player player = new Player("a",PasswordUtils.hashPassword("b"));
		Tower tower = new Tower(player,5,5);
		t.addTower(tower);
		
		t.stepTowerTerritoryGrowth(5, 2);
		Pair<Player, Integer> owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(player,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
	}
	
	
	@Test
	public void testStepTowerTerritoryGrowth3() {
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		Player playera = new Player("a",PasswordUtils.hashPassword("b"));
		Player playerb = new Player("b",PasswordUtils.hashPassword("b"));
		Tower towera = new Tower(playera,0,5);
		Tower towerb = new Tower(playerb,5,4);
		t.addTower(towera);
		t.addTower(towerb);
		
		t.stepTowerTerritoryGrowth(5, 2);
		Pair<Player, Integer> owner = t.index(0.0,0.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertEquals(Integer.valueOf(0),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(playera,owner.getFirst());
		assertEquals(Integer.valueOf(2),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(playera,owner.getFirst());
		assertEquals(Integer.valueOf(3),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(playera,owner.getFirst());
		assertEquals(Integer.valueOf(4),owner.getSecond());
		
		
		t.stepTowerTerritoryGrowth(5, 2);
		owner = t.index(0.0,0.0).getOwner();
		assertEquals(playerb,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		owner = t.index(-5.0,-5.0).getOwner();
		assertEquals(playera,owner.getFirst());
		assertEquals(Integer.valueOf(5),owner.getSecond());
		
		
	}
	
	@Test
	public void testInRange() {
		
		assertTrue(Territory.inRange(0,0, 0, 0, 10));
		assertTrue(Territory.inRange(1,0, 0, 0, 10));
		assertTrue(Territory.inRange(1,1, 5, 5, 10));
		assertTrue(Territory.inRange(-1,-1, 5, 5, 10));
		assertTrue(Territory.inRange(9,9, 5, 5, 10));
		
		assertTrue(!Territory.inRange(1,1, 0, 0, 1));
		assertTrue(!Territory.inRange(-1,-1, 0, 0, 1));
		assertTrue(!Territory.inRange(1,-1, 0, 0, 1));
		assertTrue(!Territory.inRange(-1,1, 0, 0, 1));
		
	}
	

	@Test
	public void testBurnBombFuse() {
		/*Make a map */
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		
		/*Make two players*/
		Player playera = new Player("a",PasswordUtils.hashPassword("b"));
		Player playerb = new Player("b",PasswordUtils.hashPassword("b"));
		
		/*Give them each a tower*/
		Tower towera = new Tower(playera,0,5);
		Tower towerb = new Tower(playerb,5,4);
		
		/*Put the towers on the map */
		t.addTower(towera);
		t.addTower(towerb);
		
		/*Make sure the ownership is null */
		Pair<Player, Integer> owner = t.index(5.0,0.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertTrue(owner.getSecond() == 0);
		
		/* Run the towers for a bit */
		for(int i = 0 ; i < 20 ; i++){
			//t.printGrid();
			t.stepTowerTerritoryGrowth(5, 2);
		}
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst() == playera);
		assertTrue(owner.getSecond() == 5);
		
		/* Blow up non-existent bombs */
		t.burnBombFuse(0);
		assertTrue(!towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst() == playera);
		assertTrue(owner.getSecond() == 5);
		
		/* Blow up a bomb */
		Bomb bomb = new Bomb(playera, 0, 5, 0, 1);
		t.addBomb(bomb);
		t.burnBombFuse(0);
		assertTrue(bomb.isExploded());
		
		assertTrue(towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertTrue(owner.getSecond() == 0);
		//t.printGrid();
		
		bomb = new Bomb(playera, 0, 5, 2, 5);
		t.addBomb(bomb);
		t.burnBombFuse(0);
		assertTrue(towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		assertTrue(!bomb.isExploded());
		
		t.burnBombFuse(1);
		assertTrue(towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		assertTrue(!bomb.isExploded());
		
		t.burnBombFuse(2);
		assertTrue(towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		assertTrue(bomb.isExploded());
		//t.printGrid();
		
		bomb = new Bomb(playera, 0, 5, 4, 7);
		t.addBomb(bomb);
		t.burnBombFuse(3);
		assertTrue(towera.isDestroyed());
		assertTrue(!towerb.isDestroyed());
		assertTrue(!bomb.isExploded());
		
		t.burnBombFuse(4);
		assertTrue(towera.isDestroyed());
		assertTrue(towerb.isDestroyed());
		assertTrue(bomb.isExploded());
		//t.printGrid();

		
	}
	
	
	@Test
	public void testBlowUpBombsAndRegrow() {
		/*Make a map */
		Territory t = new Territory(-5.0,5.0,10,-5.0,5.0,10);
		
		/*Make two players*/
		Player playerx = new Player("x",PasswordUtils.hashPassword("b"));
		Player playero = new Player("o",PasswordUtils.hashPassword("b"));
		
		/*Give them each a tower*/
		Tower towera = new Tower(playerx,0,5);
		Tower towerb = new Tower(playero,5,4);
		
		/*Put the towers on the map */
		t.addTower(towera);
		t.addTower(towerb);
		
		/*Make sure the ownership is null */
		Pair<Player, Integer> owner = t.index(5.0,0.0).getOwner();
		assertTrue(Player.BARBARIAN.equals(owner.getFirst()));
		assertTrue(owner.getSecond() == 0);
		
		/* Make sure only barbarians have territory */
		List<Pair<Integer, Player>> l = t.getLeaderBoard();
		assertTrue(l.size() == 1);
		assertTrue(l.get(0).getSecond().equals(Player.BARBARIAN));
		assertTrue(l.get(0).getFirst().equals(100));
		
		/* Run the towers for a bit */
		for(int i = 0 ; i < 20 ; i++){
			//t.printGrid();
			t.stepTowerTerritoryGrowth(5, 2,true);
		}
		
		/* Make sure territory is owned correctly*/
		l = t.getLeaderBoard();
		assertTrue(l.size() == 2);
		assertTrue(l.get(0).getSecond().equals(playero));
		assertTrue(l.get(0).getFirst()> l.get(1).getFirst());
		assertTrue(l.get(1).getSecond().equals(playerx));
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst() == playerx);
		assertTrue(owner.getSecond() == 5);
		
		/* Blow up a bomb */
		//t.printGrid();
		Bomb bomb = new Bomb(playerx, 0, 5, 4, 7);
		t.addBomb(bomb);
		t.burnBombFuse(4);
		assertTrue(towera.isDestroyed());
		assertTrue(towerb.isDestroyed());
		assertTrue(bomb.isExploded());
		//t.printGrid();
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst().equals(Player.BARBARIAN));
		assertTrue(owner.getSecond() == 0);
		
		/* Check ownership */
		owner = t.index(5.0,5.0).getOwner();
		assertTrue(owner.getFirst() == playero);
		assertTrue(owner.getSecond() == 5);
		
		/* Run the towers for a bit */
		/* Places unsupported by a tower should dwindle to a 1 */
		for(int i = 0 ; i < 20 ; i++){
			//t.printGrid();
			t.stepTowerTerritoryGrowth(5, 2);
		}
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst().equals(Player.BARBARIAN));
		assertTrue(owner.getSecond() == 0);
		
		/* Check ownership */
		owner = t.index(5.0,5.0).getOwner();
		assertTrue(owner.getFirst() == playero);
		assertTrue(owner.getSecond() == 1);
		
		
		/*Add a tower */
		towera = new Tower(playerx,0,5);
		
		/*Put the towers on the map */
		t.addTower(towera);
		
		/* Run the towers for a bit */
		/* Watch a reclaim the whole board */
		for(int i = 0 ; i < 20 ; i++){
			//t.printGrid();
			t.stepTowerTerritoryGrowth(5, 2);
		}
		
		/* Check ownership */
		owner = t.index(-5.0,0.0).getOwner();
		assertTrue(owner.getFirst() == playerx);
		assertTrue(owner.getSecond() == 5);
		
		/* Check ownership */
		owner = t.index(5.0,5.0).getOwner();
		assertTrue(owner.getFirst() == playerx);
		assertTrue(owner.getSecond() == 5);
		
		//t.printGrid();
		/* Make sure territory is owned correctly*/
		l = t.getLeaderBoard();
		assertTrue(l.size() == 1);
		assertTrue(l.get(0).getSecond().equals(playerx));
		assertTrue(l.get(0).getFirst().equals(100));
	}
	
	

}
