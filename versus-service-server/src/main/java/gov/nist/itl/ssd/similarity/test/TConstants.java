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

import gov.nist.itl.versus.similarity.comparisons.MathOpsE;
import java.util.Random;


public class TConstants 
{

	public static TA _;		// positive infinity
	public static TA __;	// negative infinity
	public static TA _DOT ;	// indeterminate result (NaN? or null?)
	public static int INF_RANK = -1;

	// string constants
	public static String EOL = "\n";
	public static String SEPC = ",";
	public static String SEP  = "\t";
	
	// testing constants
	//public static FaultTracker tracker = new FaultTracker();
	public static MathOpsE mopsE = new MathOpsE();
	public static Random random = new Random();
	
}
