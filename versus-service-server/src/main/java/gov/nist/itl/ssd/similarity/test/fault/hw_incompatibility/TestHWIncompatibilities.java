package gov.nist.itl.ssd.similarity.test.fault.hw_incompatibility;
import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.image;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.rname;
import gov.nist.itl.versus.similarity.comparisons.exception.HWIndependenceException;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.TCLSystemProperty;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import gov.nist.itl.ssd.similarity.test.execution.Test;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;

public class TestHWIncompatibilities
{
	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter
	protected Test testHarness;
	
	public TestHWIncompatibilities(){ super(); initialize(); }
	
	public TestHWIncompatibilities( 
			FaultTracker tracker, 
			Reporter reporter, 
			Config config,
			Functions FXS,
			Components COMPS,
			Tests TESTS,
			Test testHarness
			)
	{
		this.tracker = tracker;
		this.reporter= reporter;
		this.config = config;
		this.FXS = FXS;
		this.COMPS = COMPS;
		this.TESTS = TESTS;
		this.testHarness=testHarness;
	}

	public void initialize() {
		config  = new Config();
		reporter= new Reporter("Test HW Incompatibilities");
		tracker = new FaultTracker("Test HW Incompatibilities");
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();
	}

	
	
	public void testHWCompatibilities( String fileName )
	{
		reporter = testHarness.reporter();
		out("BEGIN: Testing HW compatibilities for: (baseline: fileName) = (" + fileName + ")" );
		tracker = new FaultTracker("HW Compatibility Test");				
		HW hw = new HW();
		TA bl = null;
		TA loaded = null;
	
		int len1 = 0;
		int len2 = 0;
		
		Exception _e = null;
		String rBase = "testHWCompatibilities" + FSEP + fileName + FSEP ;
		String rString = "";
		
		try {
			bl = hw.baselineInfo();
			loaded = hw.readBaselineInfo(fileName);		
			len1 = bl.length();
			len2 = loaded.length();
			if ( len1 != len2 ) 
				throw new HWIndependenceException( "node has different number of baseline parameters than last known baseline"); // one has a diff number of properties than the other

			tracker.track();
			rString = rBase + "loaded with same number of parameters|SAME";
		}
		catch ( Exception e ) {
			tracker.track(e);
			rString = rBase + "loaded with same number of parameters [" + e.getMessage() + "]|DIFF";
		}		
		reporter.report( rString );
		

		// if get here, are same length
		int len = len1;
		TCLSystemProperty p1 = null;
		TCLSystemProperty p2 = null;
		for (int i=0; i < len; i++) {
			p1 = (TCLSystemProperty)bl.get(i);
			p2 = (TCLSystemProperty)loaded.get(i);
			try {
				if ( !p1.equals(p2) ) 
					throw new HWIndependenceException("node has different configuration for same property: (actual,expected)=("+p1 +"," + p2 + ")" );
				tracker.track();
				rString = rBase + "node has same property (" + p1.name() + ")|SAME";
			}
			catch( Exception e ) {
				tracker.track(e);
				rString = rBase + "node has same property (" + e.getMessage() + ")|DIFF";
			}
			
			reporter.report(rString);

		}
		
		out("FaultTracker: " + tracker.report());
		tracker.graph();
		tracker.saveGraph(config.fullDir() + rname(fileName) + "_" + "HWCompatibilityTest.png");
		String REPORT = "testHWCompatibilities" + FSEP + fileName + FSEP + tracker.report() + FSEP + image( rname(fileName) + "_"+ "HWCompatibilityTest.png") ;
		reporter.report(REPORT);
		
		out("END: Testing HW compatibilities for: (baseline: fileName) = (" + fileName + ")" );							
		
	}

	
}	