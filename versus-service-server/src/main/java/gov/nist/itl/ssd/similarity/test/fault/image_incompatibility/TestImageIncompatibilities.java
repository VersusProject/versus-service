package gov.nist.itl.ssd.similarity.test.fault.image_incompatibility;

import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.image;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.rname;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import gov.nist.itl.ssd.similarity.test.execution.Test;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.TestArity1Function;
import gov.nist.itl.ssd.similarity.test.execution.function.TestArity3Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.input.source.ImageFileNameList;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;
import edu.illinois.ncsa.versus.measure.Measure;

public class TestImageIncompatibilities 
{
	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter
	protected Test testHarness;
	
	public TestImageIncompatibilities(){ super(); initialize(); }
	
	public TestImageIncompatibilities( 
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
		this.testHarness = testHarness;
	}

	public void initialize() {
		config  = new Config();
		reporter= new Reporter("Test Image Incompatibilities");
		tracker = new FaultTracker("Test Image Incompatibilities");
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();
	}

	public void testHistogramRGBMeasure( String measureName, String imgCollectionFileName )
	{	
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Histogram RGB Measure Data-Driven Test");	
		out("Testing HistogramMeasureRGB: <Measure,String,String> Function" );
		TestArity3Function<Measure,String,String,Double> testHarness = new TestArity3Function<Measure,String,String,Double>();
			
			 	ImageFileNameList fl = new ImageFileNameList();
			 	String[] files = fl.load(imgCollectionFileName);
			 	
				int len = files.length - 1;
				String first = "";
				String second= "";
				
				TestArity1Function<String,Boolean> testFile = (TestArity1Function<String,Boolean>)TESTS.getByName("TestFile");

				// get specific measure object(s)
				TA<Measure>  measures = FXS.histogram_measures();
				Measure measure = null;
				int len3 = measures.length();
				
				int mIdx = -1;
				for (int i=0; i < len3; i++) {
					if ( ((String)COMPS.histogram_measures_str().get(i)).compareTo(measureName) == 0 ) {
						mIdx = i;
						measure = (Measure)FXS.histogram_measures().get(i);
						break;
					}
				}
				
				Function[] fxs = FXS.getByType("HistogramMeasureRGB");
				Function curFx = null;
				
				Double[] d1 = new Double[]{0.1d};
				Double[] d2 = new Double[]{0.2d};
				int len2 = fxs.length;	
				
				String rString = "";
				Exception _e = null;
				
				for (int i=0; i < len2; i++) {
						curFx = fxs[i];
						if ( curFx == null ) continue;
					
					
						testHarness.fut( (Arity3Function<Measure,String,String,Double>)curFx );
						
						for (int k=0; k < len; k++) 
						{
							try {							
									first = files[k];
									second= files[k+1];							
									testFile.evalAt(first);
									testFile.evalAt(second);																
									testHarness.test( measure, first, second );
									if ( testHarness.raisedException() ) {
										_e = testHarness.e();
										throw _e;
									}
									else {
										tracker.track();
									}
							}
							catch( Exception e ) {
								tracker.track(e);
							}							
						}						
				}				
				
				out("FaultTracker: " + tracker.report());
				tracker.graph();
				tracker.saveGraph(config.fullDir() + measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_HistogramRGB.png");
				String REPORT = "testHistogramRGBMeasure" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + tracker.report() + FSEP + image( measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_HistogramRGB.png") ;
				reporter.report(REPORT);
	}	
	
	
	public void testHistogramGSMeasure( String measureName, String imgCollectionFileName )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Data-Driven Test: Histogram GS Measure");
		
		out("Testing HistogramMeasureGS: <Measure,String,String> Function" );
		TestArity3Function<Measure,String,String,Double> testHarness = new TestArity3Function<Measure,String,String,Double>();
			
			 	ImageFileNameList fl = new ImageFileNameList();
			 	
			 	String[] files = fl.load(imgCollectionFileName);
			 	
				int len = files.length - 1;
				String first = "";
				String second= "";
				
				TestArity1Function<String,Boolean> testFile = (TestArity1Function<String,Boolean>)TESTS.getByName("TestFile");

				// get specific measure object(s)
				TA<Measure>  measures = FXS.histogram_measures();
				Measure measure = null;
				int len3 = measures.length();
				
				int mIdx = -1;
				for (int i=0; i < len3; i++) {
					if ( ((String)COMPS.histogram_measures_str().get(i)).compareTo(measureName) == 0 ) {
						mIdx = i;
						measure = (Measure)FXS.histogram_measures().get(i);
						break;
					}
				}
				
				Function[] fxs = FXS.getByType("HistogramMeasureGS");
				Function curFx = null;
				
				Exception _e = null;
				String rString = "";
				
				Double[] d1 = new Double[]{0.1d};
				Double[] d2 = new Double[]{0.2d};
				int len2 = fxs.length;	
				for (int i=0; i < len2; i++) {
						curFx = fxs[i];
						if ( curFx == null ) continue;
						
						testHarness.fut( (Arity3Function<Measure,String,String,Double>)curFx );
						
						for (int k=0; k < len; k++) 
						{
							try {
									first = files[k];
									second= files[k+1];
									testFile.evalAt( first );
									testFile.evalAt( second );							
									testHarness.test( measure, first, second );
									if ( testHarness.raisedException() ) {
										_e = testHarness.e();
										throw _e;
									}
									else {
										tracker.track();
									}
							}
							catch( Exception e ) {
								tracker.track(e);
							}
						}						
				}	
				
				out("FaultTracker: " + tracker.report());
				tracker.graph();
				tracker.saveGraph(config.fullDir() + measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_HistogramGS.png");
				String REPORT = "testHistogramGSMeasure" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + tracker.report() + FSEP + image( measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_HistogramGS.png") ;
				reporter.report(REPORT);				
	}		
	
	
	public void testPixelUnlabeledMeasure( String measureName, String imgCollectionFileName )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Data-Driven Test: Pixel Measure Unlabeled");		
		out("Testing PixelMeasureUnlabeled: <Measure,String,String> Function" );
		TestArity3Function<Measure,String,String,Double> testHarness = new TestArity3Function<Measure,String,String,Double>();
			
			 	ImageFileNameList fl = new ImageFileNameList();
			 	
			 	String[] files = fl.load(imgCollectionFileName);
			 	
				int len = files.length - 1;
				String first = "";
				String second= "";
				
				TestArity1Function<String,Boolean> testFile = (TestArity1Function<String,Boolean>)TESTS.getByName("TestFile");

				// get specific measure object(s)
				TA<Measure>  measures = FXS.pixel_measures();
				Measure measure = null;
				int len3 = measures.length();
				
				int mIdx = -1;
				for (int i=0; i < len3; i++) {
					if ( ((String)COMPS.pixel_measures_str().get(i)).compareTo(measureName) == 0 ) {
						mIdx = i;
						measure = (Measure)FXS.pixel_measures().get(i);
						break;
					}
				}
				
				Function[] fxs = FXS.getByType("PixelMeasureUnLabeled");
				Function curFx = null;
				
				Exception _e = null;
				String rString = "";
				
				Double[] d1 = new Double[]{0.1d};
				Double[] d2 = new Double[]{0.2d};
				int len2 = fxs.length;	
				for (int i=0; i < len2; i++) {
						curFx = fxs[i];
						if ( curFx == null ) continue;
						
						testHarness.fut( (Arity3Function<Measure,String,String,Double>)curFx );						
						for (int k=0; k < len; k++) 
						{
							try {
									first = files[k];
									second= files[k+1];
									testFile.evalAt(first);
									testFile.evalAt(second);							
									testHarness.test( measure, first, second );
									if ( testHarness.raisedException() ) {
										_e = testHarness.e();
										throw _e;
									}
									else {
										tracker.track();
									}
							}
							catch( Exception e ) {
								tracker.track(e);
							}
						}						
				}	
				out("FaultTracker: " + tracker.report());
				tracker.graph();
				tracker.saveGraph(config.fullDir() + measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_PixelUnlabeled.png");
				String REPORT = "testPixelUnlabeledMeasure" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + tracker.report() + FSEP + image( measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_PixelUnlabeled.png") ;
				reporter.report(REPORT);
	}
	

	
	public void testPixelLabeledMeasure( String measureName, String imgCollectionFileName )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Data-Driven Test : Pixel Measure : Labeled" );		
		
		out("Testing PixelMeasureLabeled: <Measure,String,String> Function" );
		TestArity3Function<Measure,String,String,Double> testHarness = new TestArity3Function<Measure,String,String,Double>();
			
			 	ImageFileNameList fl = new ImageFileNameList();
			 	
			 	String[] files = fl.load(imgCollectionFileName);
			 	
				int len = files.length - 1;
				String first = "";
				String second= "";
				
				TestArity1Function<String,Boolean> testFile = (TestArity1Function<String,Boolean>)TESTS.getByName("TestFile");

				// get specific measure object(s)
				TA<Measure>  measures = FXS.pixel_measures();
				Measure measure = null;
				int len3 = measures.length();
				
				int mIdx = -1;
				for (int i=0; i < len3; i++) {
					if ( ((String)COMPS.pixel_measures_str().get(i)).compareTo(measureName) == 0 ) {
						mIdx = i;
						measure = (Measure)FXS.pixel_measures().get(i);
						break;
					}
				}
				
				Function[] fxs = FXS.getByType("PixelMeasureLabeled");
				Function curFx = null;
				
				Exception _e = null;
				String rString = "";
				
				Double[] d1 = new Double[]{0.1d};
				Double[] d2 = new Double[]{0.2d};
				int len2 = fxs.length;	
				for (int i=0; i < len2; i++) {
						curFx = fxs[i];
						if ( curFx == null ) continue;
						
						testHarness.fut( (Arity3Function<Measure,String,String,Double>)curFx );						
						for (int k=0; k < len; k++) 
						{
							try {
								first = files[k];
								second= files[k+1];
								testFile.evalAt( first );
								testFile.evalAt( second );							
								testHarness.test( measure, first, second );
								if ( testHarness.raisedException() ) {
									_e = testHarness.e();
									throw _e;
								}
								else {
									tracker.track();
								}
							}
							catch( Exception e ) {
								tracker.track(e);
							}
						}						
				}			
				
				out("FaultTracker: " + tracker.report());
				tracker.graph();
				tracker.saveGraph(config.fullDir() + measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_PixelLabeled.png");
				String REPORT = "testPixelLabeledMeasure" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + tracker.report() + FSEP + image( measureName + "_" + rname(imgCollectionFileName) + "_"+ "DataDrivenTest_PixelLabeled.png") ;
				reporter.report(REPORT);
	}	

}
