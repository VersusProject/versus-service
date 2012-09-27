package gov.nist.itl.ssd.similarity.test.fault.singularity_treatment;

import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.im2s;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.image;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import gov.nist.itl.versus.similarity.comparisons.ImageData;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import gov.nist.itl.ssd.similarity.test.execution.Test;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.TestArity2Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;

public class TestSingularities 
{
	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter
	protected Test testHarness;
	
	public TestSingularities(){ super(); initialize(); }
	
	public TestSingularities( 
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
		reporter= new Reporter("Test Singularities");
		tracker = new FaultTracker("Test Singularities");
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();
	}

	
	public void testHistogramMeasureFailureModeTests( String measureOpName )
	{
		reporter = testHarness.reporter();
		out("Histogram measureOp failure mode tests begin.");
		String rBase = "testHistogramMeasureFailureModeTests" + FSEP + measureOpName + FSEP;
		
		tracker = new FaultTracker("Histogram Failure-Mode Tests");
		
		TestArity2Function<Double[],Double[],Double> testHarness = new TestArity2Function<Double[],Double[],Double>();
		Arity2Function<Double[],Double[],Double> curFx = null;

				curFx = (Arity2Function<Double[],Double[],Double>)FXS.getByName( measureOpName );
				if ( curFx == null ) return;
				TA<Arity3Input> failureModeTests = (TA<Arity3Input>)curFx.failureModeTests();
									
				int len2 = failureModeTests.length();
				Double _res = 0.0d;
				String resultStr = "";
				Exception _e = null;
				for (int j=0; j < len2; j++) {
					resultStr = "";
					Arity3Input<Double[],Double[],Double> in = failureModeTests.get(j);
					try {
						_res = curFx.evalAt(in.i1(),in.i2());
						_e   = curFx.e();
						if ( _e != null ) throw _e;
						tracker.track();
					}
					catch( Exception e ) {
						tracker.track(e);
						resultStr += "(" + e.getMessage() + ") ";
					}
					Double expected = in.i3();
					Double actual   = _res;
							if ( expected == actual  ){
								resultStr += "|PASSED";
							}
							else {
								resultStr += "|FAILED";
							}
					out("FailureModeTest: ["+curFx.name() +"(new Double[]{"+in.i1()[0] +"}, new Double[]{"+in.i2()[0] +"})=(expected,actual)=("+expected+","+ actual +")]|" + resultStr );
					
					reporter.report( "["+curFx.name() +"(new Double[]{"+in.i1()[0] +"}, new Double[]{"+in.i2()[0] +"})=(expected,actual)=("+expected+","+ actual +")]|" + resultStr  );
				}
				out("Tracker: " + tracker.report() );
				tracker.saveGraph(config.fullDir() + measureOpName + "_HistogramFailureModeTests.png");
				String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_HistogramFailureModeTests.png");
				reporter.report(REPORT);					
	}
	
	public void testPixelMeasureFailureModeTests( String measureOpName )
	{
		reporter = testHarness.reporter();
		String rBase = "testPixelMeasureFailureModeTests" + FSEP + measureOpName + FSEP;			
		tracker = new FaultTracker("Pixel Measure Failure-Mode Tests");
		out("Pixel measureOp failure mode tests begin.");
		TestArity2Function<ImageData,ImageData,Double> testHarness = new TestArity2Function<ImageData,ImageData,Double>();
		Arity2Function<ImageData,ImageData,Double> curFx = null;

				curFx = (Arity2Function<ImageData,ImageData,Double>)FXS.getByName( measureOpName );
				if ( curFx == null ) return;
				TA<Arity3Input> failureModeTests = (TA<Arity3Input>)curFx.failureModeTests();
									
				int len2 = failureModeTests.length();
				Double _res = 0.0d;
				String resultStr = "";
				Exception _e = null;
				for (int j=0; j < len2; j++) {
					Arity3Input<ImageData,ImageData,Double> in = failureModeTests.get(j);
					try {
						_res = curFx.evalAt(in.i1(),in.i2());
						_e   = curFx.e();
						if ( _e != null ) throw _e;
						tracker.track();
					}
					catch( Exception e ) {
						tracker.track(e);
						resultStr += "(" + e.getMessage() + ") ";
					}
					Double expected = in.i3();
					Double actual   = _res;
							if ( expected == actual  ){
								resultStr += "|PASSED";
							}
							else {
								resultStr += "|FAILED";
							}
					
					out("FailureModeTest: ["+curFx.name() +"(new ImageData{"+im2s(in.i1()) +"}, new ImageData{"+im2s(in.i2()) +"})=(expected,actual)=("+expected+","+ actual +")]|" + resultStr );						
					reporter.report( "["+curFx.name() +"(new ImageData{"+im2s(in.i1()) +"}, new ImageData{"+im2s(in.i2()) +"})=(expected,actual)=("+expected+","+ actual +")]|" + resultStr  );

					
				}
				out("Tracker: " + tracker.report() );
				tracker.saveGraph(config.fullDir() + measureOpName + "_PixelFailureModeTests.png");
				String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_PixelFailureModeTests.png");
				reporter.report(REPORT);					
	}	

}
