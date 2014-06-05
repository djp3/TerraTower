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

import java.util.TreeMap;

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

public class GridCellTest {

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
		int x = 10;
		int y = 10;
		GridCell gc = new GridCell(x,y);
		assertEquals(x,gc.getX());
		assertEquals(y,gc.getY());
		
		assertTrue(!gc.towerPresent());
		gc.addTower(new Tower(null, x, y));
		assertTrue(gc.towerPresent());
		
		assertTrue(gc.numBombsPresent() == 0);
		gc.addBomb(new Bomb(null, x, y,0,0));
		assertTrue(gc.numBombsPresent() == 1);
		
		assertTrue(gc.getTower().getOwner()==null);
		assertTrue(gc.getTower().getX()==x);
		assertTrue(gc.getTower().getY()==y);
	}
	
	
	@Test
	public void testAlts() {
		int x = 10;
		int y = 10;
		GridCell gc = new GridCell(x,y);
		assertTrue(gc.getAlts().size() == 0);
		gc.updateAltitude(10.0);
		gc.updateAltitude(15.0);
		gc.updateAltitude(20.0);
		assertTrue(gc.getAlts().size() == 3);
		assertTrue(gc.estimateAltitude() == 15.0);
	}
	
	@Test
	public void testLowerTowerTerritoryLevel(){
		GridCell a = new GridCell(0,0);
		a.setOwner(new Pair<Player,Integer>(null,10));
		a.lowerTowerTerritoryLevel(1, 1);
		assertTrue(a.getOwner().getSecond() == 9);
		a.lowerTowerTerritoryLevel(9, 1);
		assertTrue(a.getOwner().getSecond() == 1);
		
		a.setOwner(new Pair<Player,Integer>(null,null));
		a.lowerTowerTerritoryLevel(1, 1);
		assertTrue(a.getOwner().getSecond() == 1);
		a.lowerTowerTerritoryLevel(9, 1);
		assertTrue(a.getOwner().getSecond() == 1);
	}
	
	
	@Test
	public void testRaiseTowerTerritoryLevel(){
		GridCell a = new GridCell(0,0);
		a.setOwner(new Pair<Player,Integer>(null,10));
		a.raiseTowerTerritoryLevel(1, 11);
		assertTrue(a.getOwner().getSecond() == 11);
		a.raiseTowerTerritoryLevel(9, 1);
		assertTrue(a.getOwner().getSecond() == 1);
		
		a.setOwner(new Pair<Player,Integer>(null,null));
		a.raiseTowerTerritoryLevel(1, 10);
		assertTrue(a.getOwner().getSecond() == 10);
		a.raiseTowerTerritoryLevel(9, 20);
		assertTrue(a.getOwner().getSecond() == 19);
	}

	@Test
	public void testResolveOwner(){
		GridCell gc = new GridCell(0,0);
		gc.getProposedOwner().clear();
		Player a = new Player("a",PasswordUtils.hashPassword("a"));
		Player b = new Player("b",PasswordUtils.hashPassword("a"));
		Player c = new Player("c",PasswordUtils.hashPassword("a"));
		
		gc.getProposedOwner().put(a, 10);
		gc.getProposedOwner().put(b, 11);
		gc.getProposedOwner().put(c, 9);
		
		gc.resolveOwner();
		assertTrue(gc.getOwner().getFirst().equals(b));
	}
	
	@Test
	public void testEquals(){
		GridCell a = new GridCell(1,1);
		GridCell b = new GridCell(1,1);
		
		//#unfortunate coincidence
		//TreeMap<Double, Double> c = new TreeMap<Double,Double>();
		//TreeMap<Double, Double> d = new TreeMap<Double,Double>();
		//d.put(1.0d,1.0d);
		//System.out.println(c.hashCode());
		//System.out.println(d.hashCode());
		
		assertEquals(a,a);
		assertTrue(!a.equals(null));
		assertTrue(!a.equals("foo"));
		assertEquals(a,b);
		assertEquals(a.hashCode(),b.hashCode());
		
		TreeMap<Double, Double> m = new TreeMap<Double,Double>();
		m.put(2.0d, 1.0d);
		a.setAlts(m);
		b.setAlts(null);
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		
		a.setAlts(b.getAlts());
		assertEquals(a,b);
		
		a.setTower(new Tower(new Player("playerName",PasswordUtils.hashPassword("playerPassword")),0,0));
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		
		b.setTower(new Tower(new Player("playerName",PasswordUtils.hashPassword("playerPassword")),0,0));
		assertEquals(a,b);
		assertEquals(a.hashCode(),b.hashCode());
		
		a.addBomb(new Bomb(new Player("playerName",PasswordUtils.hashPassword("playerPassword")),0,0,0,5));
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		
		b.addBomb(new Bomb(new Player("playerName",PasswordUtils.hashPassword("playerPassword")),0,0,0,5));
		assertEquals(a,b);
		assertEquals(a.hashCode(),b.hashCode());
		
		a.setX(11);
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		b.setX(a.getX());
		
		a.setY(11);
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		b.setY(a.getY());
		
		//steps taken is transient and shouldn't affect equality
		a.setStepsTaken(11);
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertTrue(a.hashCode() == b.hashCode());
		b.setStepsTaken(a.getStepsTaken());
		
		assertTrue(a.equals(b));
		a.setOwner(null);
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		a.setOwner(new Pair<Player,Integer>(new Player("playerName",PasswordUtils.hashPassword("playerPassword")),10));
		assertTrue(!a.equals(b));
		assertTrue(!b.equals(a));
		assertTrue(a.hashCode() != b.hashCode());
		b.setOwner(a.getOwner());
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
	}


}
