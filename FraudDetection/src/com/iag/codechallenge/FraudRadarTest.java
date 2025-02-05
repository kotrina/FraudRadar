package com.iag.codechallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static com.iag.codechallenge.FraudDetection.FraudResult;


class FraudRadarTest {

	 	@TempDir
	    static java.nio.file.Path tempDir;

	 	@Test
	 	void checkFraud_OneLineFile_NoFraudExpected() {
	 	   
	 	    String filePath = Paths.get("", "1lines").toString();
	 	    System.out.println(filePath);
	 	    List<FraudResult> result = executeTest(filePath);
	 	    
	 	    assertNotNull(result, "The result should not be null.");
	 	    assertEquals(0, result.size(), "The result should not contain fraudulent lines.");
	 	}

	 	@Test
	 	void checkFraud_TwoLines_SecondLineFraudulent() {
	 		
	 		String filePath = Paths.get("", "2lines").toString();
	 		List<FraudResult> result = executeTest(filePath);

	 	    assertNotNull(result, "The result should not be null.");
	 	    assertEquals(1, result.size(), "The result should contain the number of lines of the file.");
	 	    assertTrue(result.get(0).isFraudulent(), "The first line is fraudulent.");
	 	    assertEquals(2, result.get(0).getOrderId(), "The first fraudulent order ID should be 2.");
	 	    
	 	}

	 	@Test
	 	void checkFraud_ThreeLines_SecondLineFraudulent() {
	 		
	 		String filePath = Paths.get("", "3lines").toString();
	 	    List<FraudResult> result = executeTest(filePath);

	 	    assertNotNull(result, "The result should not be null.");
	 	    assertEquals(1, result.size(), "The result should contain one fraudulent line.");
	 	    assertTrue(result.get(0).isFraudulent(), "The first line is fraudulent.");
	 	    assertEquals(2, result.get(0).getOrderId(), "The first fraudulent order ID should be 2.");
	 	    
	 	}

	 	@Test
	 	void checkFraud_FourLines_MoreThanOneFraudulent() {
	 		
	 		String filePath = Paths.get("", "4lines").toString();
	 	    List<FraudResult> result = executeTest(filePath);

	 	    assertNotNull(result, "The result should not be null.");
	 	    assertEquals(2, result.size(), "The result should contain two fraudulent lines.");
	 	    
	 	}


	    private static List<FraudResult> executeTest(String filePath){
	    	List<FraudResult> results = null;
	    	try {
	    		FraudDetection fraudRadar = new FraudDetection();
	    		results = fraudRadar.check(filePath);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return results;
	    }
}
