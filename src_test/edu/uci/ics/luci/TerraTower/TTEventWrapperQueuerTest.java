package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.TerraTower.world.WorldManager;
import edu.uci.ics.luci.utility.Globals;

public class TTEventWrapperQueuerTest {

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
	
	private class ResultChecker implements TTEventHandlerResultListener{
		
		public Boolean resultOK = null;
		private String expect;
		
		ResultChecker(boolean expectError){
			if(expectError){
				this.expect = "true";
			}
			else{
				this.expect = "false";
			}
		}

		@Override
		public void onFinish(JSONObject result) {
			if(result == null){
				resultOK = false;
			}
			
			if(result.get("error").equals(this.expect)){
				resultOK = true;
			}
			else{
				resultOK = false;
			}
		}
		
	}

	@Test
	public void testLogging() {
		
		List<TTEventWrapper> events = new ArrayList<TTEventWrapper>();
		
		String logFileName = "test/test_"+this.getClass().getCanonicalName();     
		
		GlobalsTerraTower globals = new GlobalsTerraTower("TEST_VERSION");
		Globals.setGlobals(globals);
		globals.setTesting(true);
		
		TTEventWrapperQueuer eventPublisher = TerraTower.createEventQueue(logFileName);     
		globals.addQuittable(eventPublisher);
		
		WorldManager wm = new WorldManager();
		globals.setWorldManager(wm);
		
		String worldName = "Earth";
		String password = "EarthPassword";
		
		TTEventCreateWorld ttEvent1 = new TTEventCreateWorld(worldName,password);
		ResultChecker resultChecker = new ResultChecker(false);
		TTEventWrapper event = new TTEventWrapper(TTEventType.CREATE_WORLD,ttEvent1,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		while(resultChecker.resultOK == null){};
		assertTrue(resultChecker.resultOK);
		
		TTEventCreateMap ttEvent = new TTEventCreateMap(worldName,password,-180.0,180.0,10,-90,90,10);
		resultChecker = new ResultChecker(false);
		event = new TTEventWrapper(TTEventType.CREATE_MAP,ttEvent,resultChecker);
		events.add(event);
		eventPublisher.onData(event);
		while(resultChecker.resultOK == null){};
		assertTrue(resultChecker.resultOK);
		
		Globals.getGlobals().setQuitting(true);
		
		for(TTEventWrapper loopEvent: events){
			BufferedReader reader = null;
			boolean foundTheLine = false;
			try{
				Path newFile = Paths.get(logFileName);
				reader = Files.newBufferedReader(newFile, Charset.defaultCharset());
				String lineFromFile = "";
				while((lineFromFile = reader.readLine()) != null){
					if(lineFromFile.contains(loopEvent.toJSON().toJSONString())){
						foundTheLine = true;
					}
				}
				assertTrue(foundTheLine);
				reader.close();
			}catch(IOException e){
				fail(e.toString());
			}
			finally{
				if(reader!= null){
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		
	}

}
