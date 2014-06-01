package edu.uci.ics.luci.TerraTower;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.luci.utility.Globals;

public class PasswordUtilsTest {

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
		//this is to force a UTF-8 check
		Globals.setGlobals(new GlobalsTerraTower("TEST VERSION"));
		String p = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789`~!@#$%^&*()-_[{]};:'\"\\|,<.>/?åéøüñ");
		
		byte[] first = PasswordUtils.hashPassword(p);
		byte[] second = PasswordUtils.hashPassword(p);
		assertTrue(Arrays.equals(first,second));
		
		assertTrue(Arrays.equals(first,PasswordUtils.hexStringToByteArray(PasswordUtils.bytesToHexString(first)))); 
	}

}
