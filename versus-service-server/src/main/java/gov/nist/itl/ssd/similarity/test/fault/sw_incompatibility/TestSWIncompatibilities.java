package gov.nist.itl.ssd.similarity.test.fault.sw_incompatibility;

import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.im2s;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.image;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.rname;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.VersusMeasureFx;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import gov.nist.itl.ssd.similarity.test.execution.Test;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Component;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.TestArity2Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.input.source.ImageFileNameList;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.Similarity;
import gov.nist.itl.versus.similarity.comparisons.ImageData;

public class TestSWIncompatibilities
{
	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter
	protected Test testHarness;
	
	public TestSWIncompatibilities(){ super(); initialize(); }
	
	public TestSWIncompatibilities( 
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
		reporter= new Reporter("Test SW Incompatibilities");
		tracker = new FaultTracker("Test SW Incompatibilities");
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();
	}

	
	public void testPixelMeasureUnitTests( String measureOpName )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Pixel Measure Unit Tests");
		
		String rBase = "testPixelMeasureUnitTests" + FSEP + measureOpName + FSEP;
		String rString = "";
		Exception _e = null;

		
		out("Pixel Test unit tests begin.");
		TestArity2Function<ImageData,ImageData,Double> testHarness = new TestArity2Function<ImageData,ImageData,Double>();
		Arity2Function<ImageData,ImageData,Double> curFx = null;

				curFx = (Arity2Function<ImageData,ImageData,Double>)FXS.getByName( measureOpName );
				if ( curFx == null ) return;
				TA<Arity3Input> unitTests = (TA<Arity3Input>)curFx.unitTests();
									
				int len2 = unitTests.length();
				Boolean _result = true;
				for (int j=0; j < len2; j++) {
					rString = "";
					Arity3Input<ImageData,ImageData,Double> in = unitTests.get(j);
					try {
						_result = curFx.expectedValueAt(in.i1(), in.i2(), in.i3());
						_e = curFx.e();
						if ( _e != null ) throw _e;
						tracker.track();
						rString = rBase;
					}
					catch( Exception e ) {
						tracker.track(e);
						rString = rBase + "(" + e.getMessage() + ") ";
					}
					
					Double expected = in.i3();
					Double actual   = curFx.evalAt(in.i1(),in.i2());
					
					rString += "["+curFx.name() +"(" + im2s(in.i1()) + im2s(in.i2()) +")=(expected,actual)=("+expected+","+actual +")]|";
					
							if ( _result  ){
								rString += "PASSED";
							}
							else {
								rString += "FAILED";
							}				

					out( rString );		
					reporter.report(rString);
				}
				out("Tracker: " + tracker.report() );
				tracker.saveGraph(config.fullDir()  + measureOpName + "_PixelUnitTests.png");
				String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_PixelUnitTests.png");
				reporter.report(REPORT);
	}			

	
	public void testHistogramMeasureUnitTests( String measureOpName )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Histogram Unit Tests");
		String rBase = "testHistogramMeasureUnitTests" + FSEP + measureOpName + FSEP;
		
		out("Histogram Test unit tests begin.");
		TestArity2Function<Double[],Double[],Double> testHarness = new TestArity2Function<Double[],Double[],Double>();
		Arity2Function<Double[],Double[],Double> curFx = null;
		
				curFx = (Arity2Function<Double[],Double[],Double>)FXS.getByName( measureOpName );
				if ( curFx == null ) return;
				TA<Arity3Input> unitTests = (TA<Arity3Input>)curFx.unitTests();
												
				int len2 = unitTests.length();
				Boolean _result = true;
				String resultStr = "";
				Exception _e = null;
								
				for (int j=0; j < len2; j++) {
					resultStr = "";
					Arity3Input<Double[],Double[],Double> in = unitTests.get(j);
					try {
						_result = curFx.expectedValueAt(in.i1(), in.i2(), in.i3());
						_e = curFx.e();
						if ( _e != null ) throw _e;
						tracker.track();
						resultStr = rBase;
					}
					catch( Exception e ) {
						tracker.track(e);
						resultStr = rBase + "(" + e.getMessage() + ") ";
					}
					Double expected = in.i3();
					Double actual   = curFx.evalAt(in.i1(),in.i2());
							if ( _result  ){
								resultStr += "PASSED";
							}
							else {
								resultStr += "FAILED";
							}
					out("UnitTest: ["+curFx.name() +"(new Double[]{"+in.i1()[0] +"}, new Double[]{"+in.i2()[0] +"})=(expected,actual)=("+expected+","+actual +")]|" + resultStr );		
					reporter.report( "["+curFx.name() +"(new Double[]{"+in.i1()[0] +"}, new Double[]{"+in.i2()[0] +"})=(expected,actual)=("+expected+","+actual +")]|" + resultStr);
				}
				out("Tracker: " + tracker.report() );
				tracker.saveGraph(config.fullDir() + measureOpName + "_HistogramUnitTests.png");
				String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_HistogramUnitTests.png");
				reporter.report(REPORT);
	}	
	
	public void testSWCompatibilities( String measureName, String imgCollectionFileName )
	{
		reporter = testHarness.reporter();
		int totalComparisons = 0;			
		tracker = new FaultTracker("SW Compatibility Test");		
		out("BEGIN: Testing SW compatibilities for: (measureName, imgCollectionFileName) = (" + measureName + "," + imgCollectionFileName + ")" );			
			 	ImageFileNameList fl = new ImageFileNameList();
			 	
			 	String[] files = fl.load(imgCollectionFileName);
			 	
				int len = files.length - 1;
				String first = "";
				String second= "";

				// get specific measure object(s)
				TA<Measure>  measures = FXS.histogram_measures();
				Measure measure = null;
				Component measureComponent = null;

				measureComponent = COMPS.getByName( measureName );
				if ( measureComponent == null ) return;				
				measure = (Measure)measureComponent.component();		
		
				TA components = genMeasureCompatibilityAllPairs();
				int len2 = components.length();
				
				for (int i=0; i < len; i++) 
				{
					first = files[i];
					second= files[i+1];

					for (int j=0; j < len2; j++)
					{
	
			Arity2Input<Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>,Arity2Input<Component,Component>> 
								elem =
			(Arity2Input<Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>,Arity2Input<Component,Component>>)
									components.get(j);

					  Arity2Input<Component,Component> xpair = (Arity2Input<Component,Component>)elem.i2();
					  Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>> rest1 = 
							  (Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>)
							  	elem.i1();
					  Arity2Input<Component,Component> dpair = (Arity2Input<Component,Component>)rest1.i2();
					  Arity2Input<Component,Component> apair = (Arity2Input<Component,Component>)rest1.i1();
									
									String a1_name = apair.i1().name();
									String a2_name = apair.i2().name();
									String x1_name = xpair.i1().name();
									String x2_name = xpair.i2().name();
									String d1_name = dpair.i1().name();
									String d2_name = dpair.i2().name();
									String meas_name = measureComponent.name();	
									
									String SEP = ",";
									String str = "";
										//str += "(file1,file2,adapter1,adapter2,extractor1,extractor2,descriptor1,descriptor2,measure,result) = (";
			
									str += first + SEP;
									str += second+ SEP;
									str += a1_name + SEP;
									str += a2_name + SEP;
									str += x1_name + SEP;
									str += x2_name + SEP;
									str += d1_name + SEP;
									str += d2_name + SEP;
									str += measureName + SEP;						
								
									VersusMeasureFx<
									ImageObjectAdapter,ImageObjectAdapter,
									Extractor,Extractor,								 
									Descriptor,Descriptor,
									Measure,
									Similarity,
									Double> fxc = new VersusMeasureFx();
									String fileName1 = first;
									String fileName2 = second;		
			
									Adapter a1 = (Adapter)apair.i1().component();
									Adapter a2 = (Adapter)apair.i2().component();
									Extractor x1 = (Extractor)xpair.i1().component();
									Extractor x2 = (Extractor)xpair.i2().component();
									Descriptor d1= (Descriptor)dpair.i1().component();
									Descriptor d2= (Descriptor)dpair.i2().component();
									Measure    _m= (Measure)measureComponent.component();
												
									// do our compatibility testing only WRT a given measure at a time
									if ( measureComponent.name().compareTo(measureName)!=0 ) continue;
			
									// adapters match each other
									Boolean a1_isCompatibleWith_a2 = (apair.i1().isCompatibleWith( apair.i2().name() )) ? true : false;
									
									// adapters match extractors
									Boolean a1_isCompatibleWith_x1 = (apair.i1().isCompatibleWith( xpair.i1().name() )) ? true : false;
									Boolean a2_isCompatibleWith_x2 = (apair.i2().isCompatibleWith( xpair.i2().name() )) ? true : false;
									
									// extractors match descriptors
									Boolean x1_isCompatibleWith_d1 = (xpair.i1().isCompatibleWith( dpair.i1().name() )) ? true : false;
									Boolean x2_isCompatibleWith_d2 = (xpair.i2().isCompatibleWith( dpair.i2().name() )) ? true : false;
									
									// extractors match each other
									Boolean x1_isCompatibleWith_x2 = (xpair.i1().isCompatibleWith( xpair.i2().name() )) ? true : false;
									
									// descriptors match each other
									Boolean d1_isCompatibleWith_d2 = (dpair.i1().isCompatibleWith( dpair.i2().name() )) ? true : false;
									
									// measure matches (adapters,extractors,descriptors)
									Boolean m_isCompatibleWith_a1  = (measureComponent.isCompatibleWith( apair.i1().name() )) ? true : false;
									Boolean m_isCompatibleWith_a2  = (measureComponent.isCompatibleWith( apair.i2().name() )) ? true : false;
									Boolean m_isCompatibleWith_x1  = (measureComponent.isCompatibleWith( xpair.i1().name() )) ? true : false;
									Boolean m_isCompatibleWith_x2  = (measureComponent.isCompatibleWith( xpair.i2().name() )) ? true : false;
									Boolean m_isCompatibleWith_d1  = (measureComponent.isCompatibleWith( dpair.i1().name() )) ? true : false;
									Boolean m_isCompatibleWith_d2  = (measureComponent.isCompatibleWith( dpair.i2().name() )) ? true : false;
			
									Boolean expectedResult =
										a1_isCompatibleWith_a2 &&
										a1_isCompatibleWith_x1 &&
										a2_isCompatibleWith_x2 &&
										x1_isCompatibleWith_d1 &&
										x2_isCompatibleWith_d2 &&
										x1_isCompatibleWith_x2 &&
										d1_isCompatibleWith_d2 &&										
										m_isCompatibleWith_a1 &&
										m_isCompatibleWith_a2 &&
										m_isCompatibleWith_x1 &&
										m_isCompatibleWith_x2 &&
										m_isCompatibleWith_d1 &&
										m_isCompatibleWith_d2 
										;
			
						String tStr = "";
						tStr += str;
						tStr += "a1_a2 == " + a1_isCompatibleWith_a2 + "\n";						
						tStr += "a1_x1 == " + a1_isCompatibleWith_x1 + "\n";
						tStr += "a2_x2 == " + a2_isCompatibleWith_x2 + "\n";						
						tStr += "x1_d1 == " + x1_isCompatibleWith_d1 + "\n";
						tStr += "x2_d2 == " + x2_isCompatibleWith_d2 + "\n";						
						tStr += "x1_x2 == " + x1_isCompatibleWith_x2 + "\n";						
						tStr += "d1_d2 == " + d1_isCompatibleWith_d2 + "\n";						
						tStr += "m_a1 == " + m_isCompatibleWith_a1 + "\n";
						tStr += "m_a2 == " + m_isCompatibleWith_a2 + "\n";
						tStr += "m_x1 == " + m_isCompatibleWith_x1 + "\n";
						tStr += "m_x2 == " + m_isCompatibleWith_x2 + "\n";						
						tStr += "m_d1 == " + m_isCompatibleWith_d1 + "\n";
						tStr += "m_d2 == " + m_isCompatibleWith_d2 + "\n";						
						tStr += "expectedResult == " + expectedResult + "\n";
						
									Boolean actualResult = false;
			
									try {
									   Double _dres = 0.0d;
									   _dres =
										fxc.op( 
											measure, 
											fileName1, 
											fileName2,
											(ImageObjectAdapter)a1,
											(ImageObjectAdapter)a2,
											x1,
											x2,
											d1,
											d2
										);	
									   
									    if ( fxc.raisedException()== true )
									    	throw fxc.e();
										
										if ( _dres != null )
											actualResult = true;
										
										str += "(result="+_dres+"), "+ actualResult + ")";
										tracker.track();
									}
									catch( Exception e ) {
										actualResult = false;
										tracker.track(e);
										str += "(result=" + e.getMessage() +  "), " + actualResult + ") ";
									}
			
									str += "(expected,actual)=>(" + expectedResult + "," + actualResult + ") ";
			
									if ( expectedResult != actualResult  ){
										str += "|FAILED";
									}
									else {
										str += "|PASSED";
									}								
									
									out( str );
									
									String REPORT = "testSWCompatibilities" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + str ;
									reporter.report(REPORT);
									
									totalComparisons++;
									
					}
				
			}				

			out("FaultTracker: " + tracker.report());
			tracker.graph();
			tracker.saveGraph(config.fullDir() + measureName + "_" + rname(imgCollectionFileName) + "_"+ "SWCompatibilityTest.png");
			
			String REPORT = "testSWCompatibilities" + FSEP + measureName + FSEP + imgCollectionFileName + FSEP + tracker.report() + FSEP + image( measureName + "_" + rname(imgCollectionFileName) + "_"+ "SWCompatibilityTest.png") ;
			reporter.report(REPORT);
			
			
			out("END: Testing SW compatibilities for: (measureName, imgCollectionFileName) = (" + measureName + "," + imgCollectionFileName + ")" );					
	}	



///////////////////////////////////////////////////////////////////////
// generate data
	
	public TA genMeasureCompatibilityAllPairs()
	{
		TA<Arity2Input<Component,Component>> adapters 		= new TA<Arity2Input<Component,Component>>();
		TA<Arity2Input<Component,Component>> descriptors 	= new TA<Arity2Input<Component,Component>>();
		TA<Arity2Input<Component,Component>> extractors 	= new TA<Arity2Input<Component,Component>>();

		Arity2Input<Component,Component> input1 = null;
		Component[] _adapters = COMPS.getByType("Adapter");		
		int len = _adapters.length;
		for (int i=0; i < len; i++) {
			for (int j=0; j < len; j++) {
				if ( _adapters[i]==null || _adapters[j]==null) continue;
				input1 = new Arity2Input<Component,Component>();
				input1.i1( (Component)_adapters[i] );
				input1.i2( (Component)_adapters[j] );
				adapters.add( (Arity2Input<Component,Component>) input1 );
			}
		}

		Arity2Input<Component,Component> input2 = null;
		Component[] _descriptors = COMPS.getByType("Descriptor");
		len = _descriptors.length;
		for (int i=0; i < len; i++) {
			for (int j=0; j < len; j++) {
				input2 = new Arity2Input<Component,Component>();
				input2.i1( (Component)_descriptors[i] );
				input2.i2( (Component)_descriptors[j] );
				descriptors.add( (Arity2Input<Component,Component>) input2 );
			}
		}

		Arity2Input<Component,Component> input3 = null;
		Component[] _extractors = COMPS.getByType("Extractor");
		len = _extractors.length;
		for (int i=0; i < len; i++) {
			for (int j=0; j < len; j++) {
				input3 = new Arity2Input<Component,Component>();
				input3.i1( (Component)_extractors[i] );
				input3.i2( (Component)_extractors[j] );
				extractors.add( (Arity2Input<Component,Component>)input3 );
			}
		}
		
		TA combo1 = new TA();
		Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>> 
			input5 = null;
		
		len = adapters.length();
		int len2= descriptors.length();
		for (int i=0; i < len; i++) {
			for (int j=0; j < len2; j++) {
				input5 = new 
						Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>();
				input5.i1( (Arity2Input<Component,Component>) adapters.get(i) );
				input5.i2( (Arity2Input<Component,Component>) descriptors.get(j) );
				combo1.add( (Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>)input5 );
			}
		}
		
		TA combo2 = new TA();
		Arity2Input<Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>,Arity2Input<Component,Component>> 
			input6 = null;
		len = combo1.length();
		len2= extractors.length();
		for (int i=0; i < len; i++) {
			for (int j=0; j < len2; j++) {
				input6 = new 
						Arity2Input<Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>,Arity2Input<Component,Component>>();
				input6.i1( (Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>) combo1.get(i) );
				input6.i2( (Arity2Input<Component,Component>) extractors.get(j) );
				combo2.add( (
							Arity2Input<Arity2Input<Arity2Input<Component,Component>,Arity2Input<Component,Component>>,Arity2Input<Component,Component>>
							)
						input6 
						);
			}
		}

		return combo2;
		
	}

}