package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class TTEventWrapperProducerTest {

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
	public void testLogging() {
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		Globals.setGlobals(new GlobalsTerraTower("TEST_VERSION"));
		Globals.getGlobals().setTesting(true);
		
		TTEventWrapperQueuer eventPublisher = TerraTower.createEventQueue(logFileName);     
		Globals.getGlobals().addQuittable(eventPublisher);
		
		TTEventCreateWorld ttEvent = new TTEventCreateWorld("Earth","EarthPassword");
		TTEventWrapper event = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent,null);
		eventPublisher.onData(event);
		
		/*
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
		}*/
		Globals.getGlobals().setQuitting(true);
		
		boolean foundTheLine = false;
		try{
			Path newFile = Paths.get(logFileName);
			BufferedReader reader = Files.newBufferedReader(newFile, Charset.defaultCharset());
			String lineFromFile = "";
			while((lineFromFile = reader.readLine()) != null){
				if(lineFromFile.contains(event.toJSON().toJSONString())){
					foundTheLine = true;
				}
			}
			assertTrue(foundTheLine);
		}catch(IOException e){
			fail(e.toString());
		}
		
		
	}

}
