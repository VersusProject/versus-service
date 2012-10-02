
/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgment if the
 * software is used.
 *
 *
 *  @author  Benjamin Long, blong@nist.gov
 *  @version 1.1
 *    
 */

package gov.nist.itl.ssd.similarity.test;

import gov.nist.itl.ssd.similarity.test.execution.Test;

public class Tool 
{
	protected String mainResultsPage;
	protected String[] measureResultsPages;
	protected int cmd;
	public static int TEST_ALL_MEASURES = 1;
	public static int TEST_SPECIFIC_MEASURE = 2;
	protected String measureName;
	
	protected Test t;
	
	public Tool( String measureName ) 
	{
		cmd( TEST_SPECIFIC_MEASURE );
		measureName( measureName );
		testMeasure( measureName );
	}
	
	public Tool() {
		init();
	}
	
	public int cmd(){ return cmd; }
	public void cmd(int v){this.cmd=v;}
	public void measureName(String v){this.measureName=v;}
	public String measureName(){return measureName;}
	
	public void init()
	{
		cmd = TEST_ALL_MEASURES;
	}
	
	// performs the requested test(s) and returns a URI/URL to the test results Main Page.
	public String test()
	{
		String resultsMainPageUrl = "";
		if ( cmd == TEST_ALL_MEASURES )
			resultsMainPageUrl = testAllMeasures();
		else if ( cmd == TEST_SPECIFIC_MEASURE ) {
			resultsMainPageUrl = testMeasure( measureName() );
		}
		return resultsMainPageUrl;
	}
	
	public String test( String command ) 
	{
		String resultsMainPageUrl = "";
		if ( command.compareTo("testAllMeasures")==0 ) {
			cmd = TEST_ALL_MEASURES;
			resultsMainPageUrl = testAllMeasures();
		}
		else { // assume cmd is measure name
			cmd = TEST_SPECIFIC_MEASURE;
			measureName(command);
			//resultsMainPageUrl = testMeasure( measureName() );
			resultsMainPageUrl = testMeasure( command );
		}		
		return resultsMainPageUrl;
	}
	
	public String testAllMeasures()
	{
		t = new Test();
		return t.testAllMeasures();		
	}
	
	public String testMeasure(String measureName) 
	{
		t = new Test();
		return t.testSpecificMeasure( measureName );
	}
	
	
	public static void main( String[] args ) 
	{
		Tool tool = new Tool();
		
		if ( args.length > 2 ) {
			usage(args);
		}
		else if ( args.length == 2 ) {
			tool.measureName( args[1] );
			tool.cmd( Tool.TEST_SPECIFIC_MEASURE );
		}
		else if ( args.length == 1 ) {
			tool.measureName( "" );
			tool.cmd( Tool.TEST_ALL_MEASURES );			
		}
		
		String mainResultsPage = tool.test();
		
		System.out.println( "Testing complete.\nResults located at url: " + mainResultsPage );
	}
	
			public static void usage( String[] args ) 
			{
				String str = "";
				String EOL = "\n";
				
				str += "java Tool [<measureName>]" + EOL;
				str += "  1. Default usage uses no command-line parameters and runs tests for all measures on the given node." + EOL;
				str += "  2. If given an optional single argument (measurementName), runs tests for only the measure specified." + EOL;
				
				System.out.println( str );
				
				System.exit(1);
			}

}
