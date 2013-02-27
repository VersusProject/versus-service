package gov.nist.itl.ssd.similarity.test.execution;

import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.config.Config.HISTOGRAM_MEASURE_CATEGORY;
import static gov.nist.itl.ssd.similarity.test.config.Config.PIXEL_MEASURE_CATEGORY;
import static gov.nist.itl.ssd.similarity.test.config.Config.UNKNOWN_MEASURE_CATEGORY;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.href;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.function.Component;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.fault.hw_incompatibility.TestHWIncompatibilities;
import gov.nist.itl.ssd.similarity.test.fault.image_incompatibility.TestImageIncompatibilities;
import gov.nist.itl.ssd.similarity.test.fault.math_inconsistency.TestMathIncompatibilities;
import gov.nist.itl.ssd.similarity.test.fault.singularity_treatment.TestSingularities;
import gov.nist.itl.ssd.similarity.test.fault.sw_incompatibility.TestSWIncompatibilities;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;

public class Test
{
	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter	
	protected TA<String> histogram_measure_names;
	protected TA<String> pixel_measure_names;

	protected TestHWIncompatibilities 		hwCompatTests;
	protected TestSWIncompatibilities 		swCompatTests;
	protected TestImageIncompatibilities 	imgCompatTests;
	protected TestMathIncompatibilities 	mathCompatTests;
	protected TestSingularities 			singularityTests;
	
	public Test()
	{ 
		super(); 
		initialize(); 
	}

	
	public void initialize() 
	{
		config  = new Config();
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();		
		tracker = new FaultTracker("Test");
		histogram_measure_names = COMPS.histogram_measures_str();
		pixel_measure_names     = COMPS.pixel_measures_str();
		 
		hwCompatTests = new TestHWIncompatibilities(tracker, reporter, config,FXS,COMPS,TESTS,this);
		swCompatTests = new TestSWIncompatibilities(tracker, reporter, config,FXS,COMPS,TESTS,this);
		imgCompatTests= new TestImageIncompatibilities(tracker, reporter, config,FXS,COMPS,TESTS,this);
		mathCompatTests=new TestMathIncompatibilities(tracker, reporter, config,FXS,COMPS,TESTS,this);
		singularityTests=new TestSingularities(tracker, reporter, config,FXS,COMPS,TESTS,this);		
	}
	
	public Reporter reporter(){return reporter;}
	public void reporter(Reporter reporter){this.reporter=reporter;}
	
	public void test()
	{		
			testAllMeasures();
	}
	
	public void test( String cmd ) 
	{
		if ( cmd.compareTo("testAllMeasures")==0 ) {
			testAllMeasures();
		}	
		else {
			testSpecificMeasure( cmd );	// where cmd==measureName
		}	
	}
	
	public String testAllMeasures()
	{
		out("DEBUG: testAllMeasures begin.");
		Reporter reporter = new Reporter( config.mainPageDataFile(), config );

		String content = "";
		
		TA<String> all_measures_str = COMPS.all_measures_str();
		int len = all_measures_str.length();
		String mName = "";
		Component comp = null;
		for (int i=0; i < len; i++) {
			mName = all_measures_str.get(i);						
			comp  = COMPS.getByName(mName);
			
			String HTML_MEASURE_REPORT = "Measure-specific Test Report: " + mName 
					+ FSEP 
					+ href(  testMeasure(mName)   ); 
			
			reporter.report( HTML_MEASURE_REPORT );			
		}

		// HW Compatibility test
		testHWCompatibilities( config.hwCompatibilityOutputFile() );
				
		reporter.doneReporting();
		String HTML_PAGE_URL = config.mainPageReportFile();
		reporter.generateHtmlPage(HTML_PAGE_URL, config.mainPageDataFile());
		
		out("DEBUG: testAllMeasures end.");		
		return HTML_PAGE_URL;
	}	
	
	public String testSpecificMeasure( String measureName )
	{
		out("DEBUG: testSpecificMeasure begin.");
		Reporter reporter = new Reporter( config.mainPageDataFile(), config );

		String content = "";
		
		TA<String> all_measures_str = COMPS.all_measures_str();
		int len = all_measures_str.length();
		Component comp = null;
		comp  = COMPS.getByName(measureName);
			
			String HTML_MEASURE_REPORT = "Measure-specific Test Report: " + measureName 
					+ FSEP 
					+ href(  testMeasure(measureName)   ); 
			
			reporter.report( HTML_MEASURE_REPORT );			
	
		// HW Compatibility test
		testHWCompatibilities( config.hwCompatibilityOutputFile() );
				
		reporter.doneReporting();
		String HTML_PAGE_URL = config.mainPageReportFile();
		reporter.generateHtmlPage(HTML_PAGE_URL, config.mainPageDataFile());
		
		out("DEBUG: testSpecificMeasure end.");		
		return HTML_PAGE_URL;
	}	
	
	
	public String testMeasure( String measureName ) 
	{
		out("DEBUG: testSpecificMeasure(" + measureName + ") begin.");		
		reporter = new Reporter( config.fullDir() + "/" + config.testDataFilePrefix() + measureName + config.testDataFileExt(), config);
		String content = "";
		
		// determine measureOpName
			Component comp = null;
			comp  = COMPS.getByName(measureName);
			String measureOpName = comp.measureOpImpl();
			
	  out("DEBUG: testMeasure: " + measureName + "; measureOpName = [" + measureOpName + "]");
	  
	  // determine measure category	
		int category = getMeasureCategory(measureName);

	  // run category-specific measure tests
		if ( category == HISTOGRAM_MEASURE_CATEGORY ) { 
			testHistogramMeasure(measureName, measureOpName );	
		}
		else {
			testPixelMeasure(measureName, measureOpName);
		}

		// SW compatibility test
		testSWCompatibilities(measureName, config.swCompatibilityFileset() );
		
		reporter.doneReporting();
		String HTML_PAGE_URL = config.fullDir() + "/" + config.testHtmlFilePrefix() + measureName + config.testHtmlFileExt();
		reporter.generateHtmlPage(HTML_PAGE_URL,  config.fullDir() + "/" + config.testDataFilePrefix() + measureName + config.testDataFileExt() );
		
		out("DEBUG: testSpecificMeasure(" + measureName + ") end.");		
		
		return config.testHtmlFilePrefix() + measureName + config.testHtmlFileExt();
	}
	
	public void testHistogramMeasure( String measureName ) 
	{
		testHistogramRGBMeasure( measureName, 	config.dataDrivenHomogeneousFileset1());
		testHistogramGSMeasure( measureName, 	config.dataDrivenHomogeneousFileset1());
		testHistogramRGBMeasure( measureName, 	config.dataDrivenHeterogeneousFileset1());
		testHistogramGSMeasure( measureName, 	config.dataDrivenHeterogeneousFileset1());				
	}
	
	public void testHistogramMeasure( String measureName, String measureOpName ) 
	{		
		//testHistogramMeasure( measureName );			
		testHistogramMeasureUnitTests( measureOpName );		
		testHistogramMeasureFailureModeTests( measureOpName );		
		testHistogramMeasureOpProperties(measureOpName, 10);			
	}	
	
	public void testPixelMeasure( String measureName, String measureOpName ) 
	{
		//testPixelMeasure( measureName );
		testPixelMeasureUnitTests( measureOpName );
		testPixelMeasureFailureModeTests( measureOpName );
		testPixelMeasureOpProperties(measureOpName, 10);		
	}	
	
	public void testPixelMeasure( String measureName ) 
	{
		testPixelUnlabeledMeasure( measureName, config.dataDrivenHomogeneousFileset1()   );
		testPixelLabeledMeasure( measureName, 	config.dataDrivenHomogeneousFileset1()  );		
		testPixelUnlabeledMeasure( measureName, config.dataDrivenHeterogeneousFileset1() );	
		testPixelLabeledMeasure( measureName, 	config.dataDrivenHeterogeneousFileset1() );
	}	
	
	public int getMeasureCategory( String measureName ) 
	{
		if ( histogram_measure_names.exists(measureName) ) 
			return HISTOGRAM_MEASURE_CATEGORY;
		else if ( pixel_measure_names.exists(measureName) ) 
			return PIXEL_MEASURE_CATEGORY;
		else
			return UNKNOWN_MEASURE_CATEGORY;
	}		
				
	public void testPixelMeasureOpProperties( String measureOpName, int totalIterations )
	{
		mathCompatTests.testPixelMeasureOpProperties(measureOpName, totalIterations);
	}		
		
	public void testHistogramMeasureOpProperties( String measureOpName, int totalIterations )
	{
		mathCompatTests.testHistogramMeasureOpProperties(measureOpName, totalIterations);
	}		
				
	public void testPixelMeasureUnitTests( String measureOpName )
	{
		swCompatTests.testPixelMeasureUnitTests(measureOpName);
	}			
		
	public void testHistogramMeasureUnitTests( String measureOpName )
	{
		swCompatTests.testHistogramMeasureUnitTests(measureOpName);
	}	
		
	public void testHistogramMeasureFailureModeTests( String measureOpName )
	{
		singularityTests.testHistogramMeasureFailureModeTests(measureOpName);
	}
	
	public void testPixelMeasureFailureModeTests( String measureOpName )
	{
		singularityTests.testPixelMeasureFailureModeTests(measureOpName);			
	}	
	
	public void testHistogramRGBMeasure( String measureName, String imgCollectionFileName )
	{
		imgCompatTests.testHistogramRGBMeasure(measureName, imgCollectionFileName);
	}	
		
	public void testHistogramGSMeasure( String measureName, String imgCollectionFileName )
	{
		imgCompatTests.testHistogramGSMeasure(measureName, imgCollectionFileName);
	}		
	
	public void testPixelUnlabeledMeasure( String measureName, String imgCollectionFileName )
	{
		imgCompatTests.testPixelUnlabeledMeasure(measureName, imgCollectionFileName);
	}
	
	public void testPixelLabeledMeasure( String measureName, String imgCollectionFileName )
	{
		imgCompatTests.testPixelLabeledMeasure(measureName, imgCollectionFileName);		
	}	
	
	public void testHWCompatibilities( String fileName )
	{
		hwCompatTests.testHWCompatibilities(fileName);
	}
		
	public void testSWCompatibilities( String measureName, String imgCollectionFileName )
	{
		swCompatTests.testSWCompatibilities(measureName, imgCollectionFileName);
	}	

	
///////////////////////////////////////////////////////////////////////	
// main driver
	
	public static void main( String[] args ) 
	{
		System.out.println("Test begin.");
		Test t = new Test();
		t.test();
		System.out.println("Test end.");
	}
}
