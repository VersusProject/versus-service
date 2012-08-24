package gov.nist.itl.ssd.similarity.test.execution;

import javax.swing.JComponent;

import gov.nist.itl.ssd.similarity.test.reporting.FCChart;
import gov.nist.itl.ssd.similarity.test.reporting.Graph;
import fj.data.Array;
import gov.nist.itl.versus.similarity.comparisons.exception.*;


public class FaultTracker 
{
		protected boolean showUncategorizedExceptions = true;
	
		protected int totalHWIndependenceExceptions 		= 0;
		protected int totalSWIndependenceExceptions 		= 0;
		protected int totalImageCompatibilityExceptions 	= 0;
		protected int totalMathCompatibilityExceptions 		= 0;
		protected int totalSingularityTreatmentExceptions 	= 0;
		protected int totalUncategorizedExceptions 			= 0;
		protected int totalImageComparisons 				= 0;
		protected int totalTransforms  						= 0;
		protected int totalFailedComparisons 				= 0;
		
		protected String errorMsg 							= "NONE";
		protected String errorType							= "NONE";
		protected String elapsedTime 						= "";
		protected long memFreeBefore   						= 0;
		protected long memUsedBefore   						= 0;
		protected long memFreeAfter    						= 0;
		protected long memUsedAfter    						= 0;
		protected long maxMemAvailBefore					= 0;
		protected long maxMemAvailAfter						= 0;		
		
		// result
		protected Double result 							= 0.0d;
		
		// context
		protected String context = "";
		
		// reporting
		protected int indentLevel = 1;		
		
		
		public FaultTracker()
		{
			init();
		}
		
		public FaultTracker( String context ){
			this.context = context;
			init();
		}
		
		public String report1()
		{
			String SEP = " ";
			String EOL = "\t";
			String TBL   = "<table border=0>\n";
			String TBLE  = "</table>\n";
			String ROW   = "<tr valign=MIDDLE colspan=1>\n";
			String ROWE  = "</tr>\n";
			String COL  = "<td>\n";
			String COLE = "</td>\n";
			String TITLE = "<h1>\n";
			String TITLEE= "</h1>\n";
			
			String s = "";
			s += TBL ;
			s += ROW + COL + "context: " + COLE + COL + ((context!=null) ? context : "") + COLE + ROWE + EOL;
			s += ROW + COL +"totalHWIndependenceExceptions:" + COLE + COL + totalHWIndependenceExceptions + COLE + ROWE + EOL;
			s += ROW + COL +"totalSWIndependenceExceptions:" + COLE + COL + totalSWIndependenceExceptions  + COLE + ROWE + EOL;
			s += ROW + COL +"totalImageCompatibilityExceptions:" + COLE + COL + totalImageCompatibilityExceptions  + COLE + ROWE + EOL;
			s += ROW + COL +"totalMathCompatibilityExceptions:"  + COLE + COL + totalMathCompatibilityExceptions  + COLE + ROWE + EOL;
			s += ROW + COL +"totalSingularityTreatmentExceptions:" + COLE + COL + totalSingularityTreatmentExceptions + COLE + ROWE + EOL;
			s += ROW + COL +"totalUncategorizedExceptions:" + COLE + COL + totalUncategorizedExceptions  + COLE + ROWE + EOL;
			s += ROW + COL +"totalOperations:" + COLE + COL + totalImageComparisons  + COLE + ROWE + EOL;
			s += ROW + COL +"totalFailedComparisons:" + COLE + COL + totalFailedComparisons  + COLE + ROWE + EOL;
			s += TBLE;

			return s;
		}
		
		public String report()
		{
			String SEP = " ";
			String EOL = "\t";
			String s = "";
			s += "context: " + ((context!=null) ? context : "") + EOL;
			s += "totalHWIndependenceExceptions:" + SEP + totalHWIndependenceExceptions + EOL;
			s += "totalSWIndependenceExceptions:" + SEP + totalSWIndependenceExceptions  + EOL;
			s += "totalImageCompatibilityExceptions:" + SEP + totalImageCompatibilityExceptions  + EOL;
			s += "totalMathCompatibilityExceptions:"  + SEP + totalMathCompatibilityExceptions  + EOL;
			s += "totalSingularityTreatmentExceptions:" + SEP + totalSingularityTreatmentExceptions + EOL;
			s += "totalUncategorizedExceptions:" + SEP + totalUncategorizedExceptions  + EOL;
			s += "totalOperations:" + SEP + totalImageComparisons  + EOL;
			s += "totalFailedComparisons:" + SEP + totalFailedComparisons  ;
			return s;
		}		
		
		public void init()
		{
			resetHW();
			resetSW();
			resetImg();
			resetMath();
			resetSingularity();
			resetTotal();
			resetFailures();			
		}
		
		public void context( String context ) { this.context = context; }
		public String context() { return context; }
		
		public void hw( int v ) { this.totalHWIndependenceExceptions=v; }
		public int hw() { return totalHWIndependenceExceptions; }
		public void incHW() { totalHWIndependenceExceptions++; }
		public void resetHW() { totalHWIndependenceExceptions = 0; }
		
		public void sw( int v ) { this.totalSWIndependenceExceptions=v; }
		public int sw() { return totalSWIndependenceExceptions; }
		public void incSW() { totalSWIndependenceExceptions++; }
		public void resetSW() { totalSWIndependenceExceptions = 0; }
		
		public void img( int v ) { this.totalImageCompatibilityExceptions=v; }
		public int img() { return totalImageCompatibilityExceptions; }
		public void incImg() { totalImageCompatibilityExceptions++; }
		public void resetImg() { totalImageCompatibilityExceptions = 0; }
		
		public void math( int v ) { this.totalMathCompatibilityExceptions=v; }
		public int math() { return totalMathCompatibilityExceptions; }
		public void incMath() { totalMathCompatibilityExceptions++; }
		public void resetMath() { totalMathCompatibilityExceptions = 0; }
		
		public void sing( int v ) { this.totalSingularityTreatmentExceptions=v; }
		public int sing() { return totalSingularityTreatmentExceptions; }
		public void incSingularity() { totalSingularityTreatmentExceptions++; }
		public void resetSingularity() { totalSingularityTreatmentExceptions = 0; }		
		
		public void uncat( int v ) { this.totalUncategorizedExceptions=v; }
		public int uncat() { return totalUncategorizedExceptions; }
		public void incUncategorized() { totalUncategorizedExceptions++; }
		public void resetUncategorized() { totalUncategorizedExceptions = 0; }		
		
		public void total( int v ) { this.totalImageComparisons=v; }
		public int total() { return totalImageComparisons; }
		public void incTotal() { totalImageComparisons++; }
		public void resetTotal() { totalImageComparisons = 0; }		
		
		public void failures( int v ) { this.totalFailedComparisons=v; }
		public int failures() { return totalFailedComparisons; }
		public void incFailures() { totalFailedComparisons++; }
		public void resetFailures() { totalFailedComparisons = 0; }			
		
		public boolean showUncategorizedExceptions(){ return showUncategorizedExceptions; }
		public void showUncategorizedExceptions( boolean v ){ this.showUncategorizedExceptions=v; }
		
		public void track( Exception e ) 
		{
			if ( e instanceof HWIndependenceException) {
				incHW(); incFailures();
				errorMsg = e.getMessage();
				errorType= "HWIndependenceException";
			}
			else if( e instanceof SWIndependenceException) {
				incSW(); incFailures();
				errorMsg = e.getMessage();
				errorType= "SWIndependenceException";
			}
			else if( e instanceof ImageCompatibilityException) {
				incImg(); incFailures();
				errorMsg = e.getMessage();
				errorType= "ImageCompatibilityException";
			}				
			else if( e instanceof MathCompatibilityException ) {
				incMath(); incFailures();
				errorMsg = e.getMessage();
				errorType= "MathCompatibilityException";
			}
			else if( e instanceof SingularityTreatmentException) {
				incSingularity(); incFailures();
				errorMsg = e.getMessage();
				errorType= "SingularityTreatmentException";
			}								
			else if( e instanceof Exception ) { 
				result = -1.0d;
				errorMsg = e.getMessage();
				if ( errorMsg != null && errorMsg.indexOf("edu.illinois.ncsa.versus.UnsupportedTypeException") != -1 ) {
					incImg();
					errorType= "ImageCompatibilityException";
				}
				else if( errorMsg!=null && errorMsg.indexOf("Expected a color image. Input image is grayscale.") != -1 ) {
					incImg();
					errorType= "ImageCompatibilityException";
				}
				else {						
						incUncategorized();
						
						if ( showUncategorizedExceptions() ) {
							//System.out.println(" => Uncategorized Exception = (message,cause) [(" + e.getMessage() + "," + e.getCause() + ")]" );
						}
							
						
					errorType= "Exception";
				}					
				incFailures();
			}
			incTotal();
		}
		
		public void saveGraph( String fileName )
		{
			Graph g = new Graph();
			g.saveGraphImage( graph(), fileName );
		}
		
		public JComponent graph()
		{
			//Graph graph = new Graph();
			//return graph.createHistogram(getHistogramGraphData(), "Counts per Fault Category (0=hw,1=sw,2=img,3=math,4=sing)", "Fault Categories", "Counts Per Category");
			//return graph.showGraph(getGraphData(), "Counts per Fault Category (0=hw,1=sw,2=img,3=math,4=sing,5=uncat)", "Fault Categories", "Counts Per Category");
			
			FCChart chart = new FCChart("", Array.array( (double)hw(), (double)sw(), (double)img(), (double)math(), (double)sing())  );
			return chart.getChart();
		}
		
		public Array<Array<Double>> getGraphData()
		{
			Array<Double> typeCodes = Array.array(
					0.0d,
					1.0d,
					2.0d,
					3.0d,
					4.0d
					)
					;
			Array<Double> values    = Array.array(
					(double)hw(),
					(double)sw(),
					(double)img(),
					(double)math(),
					(double)sing()
					)
					;
		
			Array<Array<Double>> gData = Array.array( typeCodes, values );
			return gData;
		}		
		
		public Array<Array<Double>> getHistogramGraphData()
		{
			Array<Double> typeCodes = Array.array(
					0.0d,
					1.0d,
					2.0d,
					3.0d,
					4.0d
					)
					;
			Array<Double> values    = Array.array(
					(double)hw(),
					(double)sw(),
					(double)img(),
					(double)math(),
					(double)sing()
					)
					;
		
			Array<Array<Double>> gData = Array.array( values, typeCodes );
			return gData;
		}
		
		public void track()
		{
			incTotal();
		}
		
		/*
		 * Outputs a string to the screen.
		 */
		public void oln( String str ) {
			System.out.println( str );
			//outln(pw, str );
		}
		
		public void o( String str ) {
			System.out.print( str );
		}			
}
