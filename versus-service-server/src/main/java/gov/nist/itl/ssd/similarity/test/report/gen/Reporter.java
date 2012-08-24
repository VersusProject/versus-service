package gov.nist.itl.ssd.similarity.test.report.gen;

import java.util.Date;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.*;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.TCLFile;
import gov.nist.itl.ssd.similarity.test.config.Config;

public class Reporter 
{
	protected Config config;
	protected PrintWriter pw;
	protected BufferedReader rdr;
	protected TCLFile io;
	
	protected String rawReportFileName;
	protected boolean toConsole = false;
	protected String mainPageUrl;
	

	
	public Reporter() {
		config = new Config();
		toConsole(true);
		initialize();
	}
	
	public Reporter( String rawReportFileName ) {
		config = new Config();
		rawReportFileName(rawReportFileName);
		initialize();
	}
	
	public Reporter( String rawReportFileName, Config config ) {
		this.config = config;
		rawReportFileName(rawReportFileName);
		initialize();
	}
	
	
	public boolean toConsole(){return toConsole;}
	public void toConsole(boolean v){this.toConsole=v;}
	
	public String rawReportFileName(){return rawReportFileName;}
	public void rawReportFileName(String v){this.rawReportFileName=v;}
	public String mainPageUrl(){return mainPageUrl;}
	public void mainPageUrl(String v){this.mainPageUrl=v;}

	
	public void initialize() 
	{		
		 io = new TCLFile();
		   try {
			   	 io.dirs_create(config.fullDir());	// create this right away
			   	 
			   	 if ( !toConsole() ) {		// open the file where we save everything.
					 openReportFile(rawReportFileName);
				 }
		   }
		   catch( Exception e ) {
			   
		   }
		
	}
	
	
	public void openReportFile( String fileName ) {
		try {
			pw = io.open_overwrite(fileName);
		}
		catch( Exception e ) {
			System.out.println("Error: could not open report file for writing, switching to console mode");
			toConsole(true);
		}		
	}
	
	public void report( String s ) 
	{
		String str = config.dateTime() + "|" + s;
		
		if ( !toConsole() )
			pw.println( str );
		else
			System.out.println(str);
	}
	
	public void doneReporting()
	{
		try {
			if ( !toConsole() ) {
				if ( pw != null )
					io.close( pw );
				if ( rdr != null )
					io.close(rdr);
			}
			
		}
		catch( Exception e ) {
			System.out.println("Error: problems closing report.");
		}
	}
	
	public TA loadReport( String fileName )
	{
		TA reportLines = new TA();
		String[] lines = null;
		try {
			rdr = io.open(fileName);
			lines = io.readAllLines(rdr);
			int len = lines.length;
			for (int i=0; i < len; i++)
				reportLines.add( tokenize(lines[i],'|') );
		}
		catch( Exception e ) {
			System.out.println("Error: problem loading report (" + fileName + ")" );
		}
		return reportLines;
	}
	
	public String[] tokenize( String s, char delimiter ) {
		TA tokens = new TA();
		int len = s.length();
		String str = "";
		for (int i=0; i < len; i++) {
			if ( s.charAt(i) == delimiter ) {
				if ( str.length() != 0 )
					tokens.add(str);
				str = "";
			}
			else {
				str += s.charAt(i);
			}
		}
		// get last
		if ( str.length() != 0 )
			tokens.add(str);
		
		return tokens.asStringArray();
	}
	
	public void generateCSSFile( String fileName )
	{
		String str ="";
		
		str += "body {\n";
		str += "	line-height: 1.6em;\n";
		str += "}\n";
		str += "\n";
		str += "table {\n";
		str += "	font-family: \"Lucida Sans Unicode\", \"Lucida Grande\", Sans-Serif;\n";
		str += "	font-size: 12px;\n";
		str += "	background: #fff;\n";
		str += "	margin: 45px;\n";
		str += "	width: 800px;\n";
		str += "\n";
		str += "	border-collapse: collapse;\n";
		str += "	border-top: 7px solid #9baff1;\n";
		str += "	border-bottom: 7px solid #9baff1;\n";
		str += "	border: 1px solid #69c;\n";
		str += "	border-color: #039;\n";
		str += "	border-width: 0 0 1px 1px;\n";
		str += "	border-style: solid;\n";
		str += "	border-right: 1px solid #9baff1;\n";
		str += "}\n";
		str += "\n";
		str += "thead {\n";
		str += "\n";
		str += "}\n";
		str += "\n";
		str += "th {\n";
		str += "	font-size: 14px;\n";
		str += "	font-weight: normal;\n";
		str += "	padding: 10px 8px;\n";
		str += "	border-bottom: 2px solid #6678b1;\n";
		str += "	background: #e8edff;\n";
		str += "	text-align: left;\n";
		str += "	border-right: 1px solid #9baff1;\n";
		str += "	border-left: 1px solid #9baff1;\n";
		str += "	color: #039;\n";
		str += "}\n";
		str += "\n";
		str += "tr {\n";
		str += "	background: #d0dafd;\n";
		str += "	color: #339;\n";
		str += "	\n";
		str += "}\n";
		str += "\n";
		str += "td {\n";
		str += "	color: #669;\n";
		str += "	padding: 9px 8px 0px 8px;\n";
		str += "	border-left: 1px solid #9baff1;\n";
		str += "	border-bottom: 1px solid #9baff1;\n";
		str += "}\n";
		str += "\n";
		str += "tbody {\n";
		str += "	color: #111;\n";
		str += "}\n";
		str += "\n";
		str += "tr td {\n";
		str += "	color: #009;\n";
		str += "\n";
		str += "}\n";
		str += "\n";
		str += "tfoot {\n";
		str += "\n";
		str += "}\n";

		try {

			String cssFile =  fileName ;
			
			pw = io.open_overwrite(cssFile);
			pw.println( str );
			io.close(pw);
		}
		catch( Exception e ) {
			System.out.println("Error: problem writing CSS file");
		}
	}
	
	public void generateHtmlPage( String htmlName, String fileName )
	{
		TA reportLines = loadReport(fileName);
		String[] tokens = null;
		int len = reportLines.length();
		String HTML  = "<html><link rel=\"stylesheet\" href=\"styles.css\" type=\"text/css\"  /><body>\n";
		String HTMLE = "</body></html>\n";
		String TBL   = "<table border=0>\n";
		String TBLE  = "</table>\n";
		String ROW   = "<tr valign=MIDDLE colspan=1>\n";
		String ROWE  = "</tr>\n";
		String CELL  = "<td>\n";
		String CELLE  = "</td>\n";
		String TITLE = "<h1>\n";
		String TITLEE= "</h1>\n";
		String PAGE = "";
	
		PAGE += HTML;
		PAGE += TITLE + "Test Report : " + config.dateTime() + TITLEE;
		PAGE += TBL;
		
		
		for( int i=0; i < len; i++) {
			PAGE += ROW;
			tokens = (String[])reportLines.get(i);
			int len2 = tokens.length;
			for (int j=0; j < len2; j++) {
				PAGE += CELL + tokens[j] + CELLE;
			}
			PAGE += ROWE;
		}		
		PAGE += TBLE;
		PAGE += HTMLE;
		
		try {

			io.dirs_create(config.fullDir());
			
			generateCSSFile( config.fullDir() + "/" + "styles.css" );
			
			String htmlPage =  htmlName ;
			
			pw = io.open_overwrite(htmlPage);
			pw.println( PAGE );
			io.close(pw);
		}
		catch( Exception e ) {
			System.out.println("Error: problem writing HTML page");
		}
	}
}
