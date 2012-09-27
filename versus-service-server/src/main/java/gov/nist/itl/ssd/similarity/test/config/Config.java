
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

package gov.nist.itl.ssd.similarity.test.config;

import gov.nist.itl.versus.similarity.comparisons.MathOpsE;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nist.itl.ssd.similarity.test.TCLFile;

public class Config 
{
	protected DateFormat formatter;
	protected DateFormat dateformat;
	protected DateFormat timeformat;
	protected String FULL_DIR;	
	protected String BASE_DIR;
	protected String HTML_BASE_DIR;	
	protected String TODAYS_COUNTER_FILE_SUFFIX;

	// main html test page data file and html file
	protected String MAIN_PAGE_DATA_FILE;
	protected String MAIN_PAGE_REPORT_FILE;
	
	// specific test file conventions used
	protected String TEST_DATA_FILE_PREFIX;
	protected String TEST_DATA_FILE_EXT;
	protected String TEST_HTML_FILE_PREFIX;
	protected String TEST_HTML_FILE_EXT;
	
	// auxiliary files created or used
	protected String SW_COMPATIBILITY_FILESET;
	protected String HW_COMPATIBILITY_OUTPUT_FILE;
	protected String DATA_DRIVEN_HOMOGENEOUS_FILESET1;
	protected String DATA_DRIVEN_HETEROGENEOUS_FILESET1;
	
	protected Integer counter = 0;	// used to keep track of test directories on a given date
	protected String workingBaseDir;
	
	// static constants 
	public static String FSEP = "|";
	public static String SEP  = ",";
	public static String EOL  = "\n";
	public static MathOpsE mops = new MathOpsE();
	public static int HISTOGRAM_MEASURE_CATEGORY 	= 1;
	public static int PIXEL_MEASURE_CATEGORY 		= 2;
	public static int UNKNOWN_MEASURE_CATEGORY 		= 3;
	
	public Config()
	{
		initialize();
	}
	
	public String baseDir(){return BASE_DIR;}
	public void baseDir(String v){this.BASE_DIR=v;}
	public String htmlBaseDir(){return HTML_BASE_DIR;}
	public void htmlBaseDir(String v){this.HTML_BASE_DIR=v;}
	public String todaysCounterFileSuffix(){return TODAYS_COUNTER_FILE_SUFFIX;}
	public void todaysCounterFileSuffix(String v){this.TODAYS_COUNTER_FILE_SUFFIX=v;}
	public String todaysCounterfile(){ return baseDir() + "/" + date() + "_" + todaysCounterFileSuffix(); }
	public String mainPageDataFile(){ return fullDir() + "/" + MAIN_PAGE_DATA_FILE;}
	public void mainPageDataFile(String v){this.MAIN_PAGE_DATA_FILE=v;}
	public String mainPageReportFile(){ return fullDir() + "/" + MAIN_PAGE_REPORT_FILE; }
	public void mainPageReportFile(String v){this.MAIN_PAGE_REPORT_FILE=v;}
	public String testDataFilePrefix(){ return TEST_DATA_FILE_PREFIX; }
	public void testDataFilePrefix(String v){this.TEST_DATA_FILE_PREFIX=v;}
	public String testDataFileExt(){ return TEST_DATA_FILE_EXT;}
	public void testDataFileExt(String v){this.TEST_DATA_FILE_EXT=v;}
	public String testHtmlFilePrefix(){ return TEST_HTML_FILE_PREFIX;}
	public void testHtmlFilePrefix(String v){this.TEST_HTML_FILE_PREFIX=v;}
	public String testHtmlFileExt(){ return TEST_HTML_FILE_EXT;}
	public void testHtmlFileExt(String v){this.TEST_HTML_FILE_EXT=v;}
	public String swCompatibilityFileset(){ return baseDir() + "/" + SW_COMPATIBILITY_FILESET;}
	public void swCompatibilityFileset(String v){this.SW_COMPATIBILITY_FILESET=v;}
	public String hwCompatibilityOutputFile(){ return baseDir() + "/" + HW_COMPATIBILITY_OUTPUT_FILE; }
	public void hwCompatibilityOutputFile(String v){this.HW_COMPATIBILITY_OUTPUT_FILE=v;}
	public String dataDrivenHomogeneousFileset1(){ return baseDir() + "/" + DATA_DRIVEN_HOMOGENEOUS_FILESET1;}
	public void dataDrivenHomogeneousFileset1(String v){this.DATA_DRIVEN_HOMOGENEOUS_FILESET1=v;}
	public String dataDrivenHeterogeneousFileset1(){ return baseDir() + "/" + DATA_DRIVEN_HETEROGENEOUS_FILESET1;}
	public void dataDrivenHeterogeneousFileset1(String v){this.DATA_DRIVEN_HETEROGENEOUS_FILESET1=v;}	
	public String fullDir(){return FULL_DIR;}	
	public void fullDir(String v){this.FULL_DIR=v;}
	public void counter(Integer v){ this.counter = v; }	
	public Integer counter(){ return counter; }
	
	public void initialize() 
	{
		 formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		 dateformat = new SimpleDateFormat("yyyy-MM-dd");
		 timeformat = new SimpleDateFormat("HH-mm-ss");
		 
		    BASE_DIR 					= "./tests";
		    HTML_BASE_DIR 				= BASE_DIR + "/html";	
			TODAYS_COUNTER_FILE_SUFFIX 	= "counter.txt";

			// other files used
			MAIN_PAGE_DATA_FILE 	= "testAllMeasuresReport.txt";
			MAIN_PAGE_REPORT_FILE	= "TestReportMainPage.html";
			
			// specific test file conventions used
			TEST_DATA_FILE_PREFIX  = "testMeasure_";
			TEST_DATA_FILE_EXT  	= ".txt";
			TEST_HTML_FILE_PREFIX  = "TestReport_";
			TEST_HTML_FILE_EXT  	= ".html";
			
			// auxiliary files created or used
			SW_COMPATIBILITY_FILESET			= "compatibilityFileset.txt";
			HW_COMPATIBILITY_OUTPUT_FILE		= "hwCompatData_baselineInfo.txt";
			DATA_DRIVEN_HOMOGENEOUS_FILESET1 	= "recommendationSystemFiles.txt";
			DATA_DRIVEN_HETEROGENEOUS_FILESET1  = "heterogeneousSystemFiles.txt";
		 
		 initCounter();
		 initVariables();
		
	}
	
	public void initCounter()
	{
		 TCLFile io = new TCLFile();
		   try {
			   	 io.dirs_create(BASE_DIR);	// create this right away
			   	 io.dirs_create(HTML_BASE_DIR);
			   	 File f = new File( todaysCounterfile() );
			   	 if ( !f.exists() ) {
			   		 try {
			   			 PrintWriter pw = io.open_overwrite(todaysCounterfile() );
			   			 pw.println( "" + counter() );
			   			 io.close(pw);
			   		 }
			   		 catch( Exception e ) {
			   			 System.out.println("Error: could not write counter file.");
			   		 }
			   	 }
			   	 else {
			   		 try {
			   			 BufferedReader rdr = io.open(todaysCounterfile());
			   			 String[] lines = io.readAllLines(rdr);
			   			 io.close(rdr);
			   			 int len = lines.length;
			   			 Integer val = null;
			   			 if ( len > 0 ) {
			   				 String s = lines[0];
			   				 val = Integer.parseInt(s);
			   				 val++;
			   				 counter(val);
			   				 PrintWriter pw = io.open_overwrite(todaysCounterfile());
			   				 pw.println("" + counter() );
			   				 io.close(pw);
			   			 }
			   		 }
			   		 catch( Exception e ) {
			   			 System.out.println("Error: could not read existing counter file.");
			   		 }
			   	 }
			   	 
			   	 // init variables relative to count
			   	 
		   }
		   catch( Exception e ) {
			   System.out.println("Error: Could not create base configuration directories.");
		   }	
	}
	
	public void initVariables()
	{
		FULL_DIR = HTML_BASE_DIR + "/" + date() + "/" + counter() + "/";	
		   try {
			   TCLFile io = new TCLFile();
			   	 io.dirs_create(FULL_DIR);
		   }
		   catch( Exception e ) {
			   System.out.println("Error: could not create full dir [" + FULL_DIR + "]" );
		   }
	}
	
	public String dateTime()
	{
		String str = "";
			Date date = new Date();
			str = formatter.format(date);
		return str;
	}
	
	public String date()
	{
		String str = "";
			Date date = new Date();
			str = dateformat.format(date);
		return str;
	}
	
	public String time()
	{
		String str = "";
			Date date = new Date();
			str = timeformat.format(date);
		return str;
	}
	
	public Date dateTimeFromString( String s ) {
		Date date = null;		
		try {
			date = (Date) formatter.parse(s);
		}
		catch( Exception e ) {
			System.out.println("Error: could not parse date-time from string");
		}
		return date;
	}
}
