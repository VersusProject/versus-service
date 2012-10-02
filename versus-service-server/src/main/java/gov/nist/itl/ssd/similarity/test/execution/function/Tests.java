package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt1;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt2;
import gov.nist.itl.ssd.similarity.test.style.failuremode.TestFile;
import gov.nist.itl.ssd.similarity.test.utils.Utils;
import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.adapter.impl.ImageObjectAdapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.GrayscaleHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.RGBHistogramDescriptor;
import edu.illinois.ncsa.versus.descriptor.impl.ThreeDimensionalDoubleArrayFeature;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.extract.impl.ArrayFeatureExtractor;
import edu.illinois.ncsa.versus.extract.impl.GrayscaleHistogramExtractor;
import edu.illinois.ncsa.versus.extract.impl.RGBHistogramExtractor;
import edu.illinois.ncsa.versus.measure.Measure;
import fj.data.Array;
import gov.nist.itl.versus.similarity.comparisons.ImageData;
import gov.nist.itl.versus.similarity.comparisons.adapter.impl.LabeledImageObjectAdapter;
import gov.nist.itl.versus.similarity.comparisons.descriptor.impl.LabeledThreeDimensionalDoubleArrayFeature;
import gov.nist.itl.versus.similarity.comparisons.extract.impl.LabeledArrayFeatureExtractor;
import gov.nist.itl.versus.similarity.comparisons.measure.LabeledMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.AdditiveSymmetricChiSquaredMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.AdjustedRandIndexMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.AvgDifferenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.BhattacharyyaMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.CanberraMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.ChebyshevLInfMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.CityBlockL1Measure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.ClarkMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.CosineMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.CzekanowskiDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.CzekanowskiMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.DiceDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.DiceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.DicePixelMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.DivergenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.EuclideanL2Measure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.FidelityMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.GowerMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.HarmonicMeanMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.HellingerMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.InnerProductMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.IntersectionDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.IntersectionMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JaccardDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JaccardMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JaccardPixelMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JeffreysMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JensenDifferenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.JensenShannonMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KDivergenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KulczynskiMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KulczynskiSMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KullbackLeiblerMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KumarHassebrookPCEMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.KumarJohnsonDifferenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.LorentzianMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.MatusitaDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.MatusitaMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.MinkowskiMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.MotykaDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.MotykaMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.NeymanChiSquaredMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.PearsonChiSquaredMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.ProbabilisticSymmetricChiSquaredMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.RandIndexMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.RuzickaMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SoergelMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SorensenMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SquaredChiSquaredMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SquaredChordDMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SquaredChordMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.SquaredEuclideanMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.TanejaDifferenceMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.TanimotoMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.TopsoeMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.TotalErrorRateEvaluationMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.TotalErrorRateTestMeasure;
import gov.nist.itl.versus.similarity.comparisons.measure.impl.WaveHedgesMeasure;

public class Tests<A> 
{
	
	public static TA<Measure> all_measures = new TA(Array.array(
			new EuclideanL2MeasureAlt1(),
			new EuclideanL2MeasureAlt2(),
			new AdditiveSymmetricChiSquaredMeasure(),
		    (Measure)new AdjustedRandIndexMeasure(),
			new AvgDifferenceMeasure(),
			new BhattacharyyaMeasure(),
			new CanberraMeasure(),
			new ChebyshevLInfMeasure(),
			new CityBlockL1Measure(),
			new ClarkMeasure(),
			new CosineMeasure(),
			new CzekanowskiDMeasure(),
			new CzekanowskiMeasure(),
			new DiceDMeasure(),
			new DiceMeasure(),
			(Measure)new DicePixelMeasure(),
			new DivergenceMeasure(),
			new EuclideanL2Measure(),
			new FidelityMeasure(),
			new GowerMeasure(),
			new HarmonicMeanMeasure(),
			new HellingerMeasure(),
			new InnerProductMeasure(),
			new IntersectionDMeasure(),
			new IntersectionMeasure(),
			new JaccardDMeasure(),
			new JaccardMeasure(),
			(Measure)new JaccardPixelMeasure(),
			new JeffreysMeasure(),
			new JensenDifferenceMeasure(),
			new JensenShannonMeasure(),
			new KDivergenceMeasure(),
			new KulczynskiMeasure(),
			new KulczynskiSMeasure(),
			new KullbackLeiblerMeasure(),
			new KumarHassebrookPCEMeasure(),
			new KumarJohnsonDifferenceMeasure(),
			new LorentzianMeasure(),
			new MatusitaDMeasure(),
			new MatusitaMeasure(),
			new MinkowskiMeasure(),
			new MotykaDMeasure(),
			new MotykaMeasure(),
			new NeymanChiSquaredMeasure(),
			new PearsonChiSquaredMeasure(),
			new ProbabilisticSymmetricChiSquaredMeasure(),
			(Measure)new RandIndexMeasure(),
			new RuzickaMeasure(),
			new SoergelMeasure(),
			new SorensenMeasure(),
			new SquaredChiSquaredMeasure(),
			new SquaredChordDMeasure(),
			new SquaredChordMeasure(),
			new SquaredEuclideanMeasure(),
			new TanejaDifferenceMeasure(),
			new TanimotoMeasure(),
			new TopsoeMeasure(),
			(Measure)new TotalErrorRateEvaluationMeasure(),
			(Measure)new TotalErrorRateTestMeasure(),
			new WaveHedgesMeasure()			
			));

	public static TA<Measure> histogram_measures = new TA(Array.array(
			new EuclideanL2MeasureAlt1(),
			new EuclideanL2MeasureAlt2(),
			new AdditiveSymmetricChiSquaredMeasure(),
			new AvgDifferenceMeasure(),
			new BhattacharyyaMeasure(),
			new CanberraMeasure(),
			new ChebyshevLInfMeasure(),
			new CityBlockL1Measure(),
			new ClarkMeasure(),
			new CosineMeasure(),
			new CzekanowskiDMeasure(),
			new CzekanowskiMeasure(),
			new DiceDMeasure(),
			new DiceMeasure(),
			new DivergenceMeasure(),
			new EuclideanL2Measure(),
			new FidelityMeasure(),
			new GowerMeasure(),
			new HarmonicMeanMeasure(),
			new HellingerMeasure(),
			new InnerProductMeasure(),
			new IntersectionDMeasure(),
			new IntersectionMeasure(),
			new JaccardDMeasure(),
			new JaccardMeasure(),
			new JeffreysMeasure(),
			new JensenDifferenceMeasure(),
			new JensenShannonMeasure(),
			new KDivergenceMeasure(),
			new KulczynskiMeasure(),
			new KulczynskiSMeasure(),
			new KullbackLeiblerMeasure(),
			new KumarHassebrookPCEMeasure(),
			new KumarJohnsonDifferenceMeasure(),
			new LorentzianMeasure(),
			new MatusitaDMeasure(),
			new MatusitaMeasure(),
			new MinkowskiMeasure(),
			new MotykaDMeasure(),
			new MotykaMeasure(),
			new NeymanChiSquaredMeasure(),
			new PearsonChiSquaredMeasure(),
			new ProbabilisticSymmetricChiSquaredMeasure(),
			new RuzickaMeasure(),
			new SoergelMeasure(),
			new SorensenMeasure(),
			new SquaredChiSquaredMeasure(),
			new SquaredChordDMeasure(),
			new SquaredChordMeasure(),
			new SquaredEuclideanMeasure(),
			new TanejaDifferenceMeasure(),
			new TanimotoMeasure(),
			new TopsoeMeasure(),
			new WaveHedgesMeasure()			
			));
	
	public static TA<LabeledMeasure> pixel_measures = new TA(Array.array(
			new AdjustedRandIndexMeasure(),
			new DicePixelMeasure(),
			new JaccardPixelMeasure(),
			new RandIndexMeasure(),
			new TotalErrorRateEvaluationMeasure(),
			new TotalErrorRateTestMeasure()
			));
	
	public static TA<Adapter> adapters = new TA(Array.array(
			new ImageObjectAdapter(),
			new LabeledImageObjectAdapter()
			));	
	
	public static TA<Extractor> extractors = new TA(Array.array(
			new RGBHistogramExtractor(),
			new GrayscaleHistogramExtractor(),
			new ArrayFeatureExtractor(),
			new LabeledArrayFeatureExtractor()
			));	 
	
	public static TA<Descriptor> descriptors = new TA(Array.array(
			new RGBHistogramDescriptor(),
			new GrayscaleHistogramDescriptor(),
			new ThreeDimensionalDoubleArrayFeature(),
			new LabeledThreeDimensionalDoubleArrayFeature()
			));	 
	
	public TA<Descriptor> descriptors(){return descriptors;}
	public TA<Extractor> extractors(){ return extractors;}
	public TA<Adapter> adapters(){return adapters;}
	public TA<Measure> measures(){return all_measures;}
	public TA<Measure> histogram_measures(){return histogram_measures;}
	public TA<LabeledMeasure> pixel_measures(){return pixel_measures;}
	
	public Tests(){super(); load(); }
	
	protected  TA<Function> TESTS  = new TA();
	
	public void load()
	{

		 // propertyTests
				 
			entry("AssociativityTestArity2Double",  new TestArity2With3Function<Double,Double,Double,Double>("TestArity2With3Function<Double,Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b, Double c ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double bc = fut.evalAt(b, c);
						
						Double ab_c = fut.evalAt(ab, c);
						Double a_bc = fut.evalAt(a, bc);
						if ( ab_c.equals(a_bc)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("TranslationInvarianceTestArity2Double",  new TestArity2With3Function<Double,Double,Double,Double>("TestArity2With3Function<Double,Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b, Double c ) {
					try {
						Double ab = fut.evalAt(a,   b);
						Double ac_bc = fut.evalAt(a+c, b+c);
						
						if ( ab.equals(ac_bc)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			
			entry("TriangleInequalityTestArity2Double",  new TestArity2With3Function<Double,Double,Double,Double>("TestArity2With3Function<Double,Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b, Double c ) {
					try {
						Double ac = fut.evalAt(a, c);
						Double ab = fut.evalAt(a, b);
						Double bc = fut.evalAt(b, c);
						
						if ( ac <= (ab + bc) )  
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			

			entry("StrongerTriangleInequalityTestArity2Double",  new TestArity2With3Function<Double,Double,Double,Double>("TestArity2With3Function<Double,Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b, Double c ) {
					try {
						Double ac = fut.evalAt(a, c);
						Double ab = fut.evalAt(a, b);
						Double bc = fut.evalAt(b, c);
						
						Double max = (ab > bc ) ? ab : bc;
						
						if ( ac <= max )  
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			

			entry("CommutativityTestArity2Double",  new TestArity2Function<Double,Double,Double>("TestArity2Function<Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double ba = fut.evalAt(b, a);
						
						if ( ab.equals(ba)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("SymmetryTestArity2Double",  new TestArity2Function<Double,Double,Double>("TestArity2Function<Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double ba = fut.evalAt(b, a);
						
						if ( ab.equals(ba)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("IdentityOfIndiscerniblesTestArity2Double",  new TestArity2Function<Double,Double,Double>("TestArity2Function<Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b ) {
					try {
						Double _result = fut.evalAt(a, b);
						
						if ( _result.equals(0.0d) && a.equals(b) ) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			
			entry("NonNegativityTestArity1Double",  new TestArity1Function<Double,Double>("TestArity1Function<Double,Double>") {			
				public Boolean evalAt( Double a ) {
					try {
						Double _result = fut.evalAt(a);
						
						if ( _result >= 0.0d ) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				
			
			
			entry("NonNegativityTestArity2Double",  new TestArity2Function<Double,Double,Double>("TestArity2Function<Double,Double,Double>") {			
				public Boolean evalAt( Double a, Double b ) {
					try {
						Double ab = fut.evalAt(a, b);
						
						if ( ab >= 0.0d ) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("TranslationInvarianceTestArity2D1", new TestArity2With3Function<Double[],Double[],Double[],Double[]>("TestArity2With3Function<Double[],Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b, Double[] c ) {
					try {
						
						int len = a.length;
						Double[] ac = new Double[len];
						Double[] bc = new Double[len];
						for (int i=0; i < len; i++) ac[i] = a[i] + c[i];
						for (int i=0; i < len; i++) bc[i] = b[i] + c[i];
						
						Double[] ab = fut.evalAt(a,   b);
						Double[] ac_bc = fut.evalAt(ac, bc);
						
						if ( ab.equals(ac_bc)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			
			
			entry("TriangleInequalityTestArity2D1", new TestArity2With3Function<Double[],Double[],Double[],Double[]>("TestArity2With3Function<Double[],Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b, Double[] c ) {
					try {
						Double[] ac = fut.evalAt(a, c);
						Double[] ab = fut.evalAt(a, b);
						Double[] bc = fut.evalAt(b, c);
						
						int len = ab.length;
						Double[] ab_bc_sum = new Double[len];
						for (int i=0; i < len; i++) {
							ab_bc_sum[i] = ab[i] + bc[i];
							if ( ac[i] > ab_bc_sum[i] ) return false;
						}	
						return true;						
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			
			entry("StrongerTriangleInequalityTestArity2D1", new TestArity2With3Function<Double[],Double[],Double[],Double[]>("TestArity2With3Function<Double[],Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b, Double[] c ) {
					try {
						Double[] ac = fut.evalAt(a, c);
						Double[] ab = fut.evalAt(a, b);
						Double[] bc = fut.evalAt(b, c);
						
						int len = ab.length;
						Double max = 0.0d;
						
						for (int i=0; i < len; i++) {
							max = (ab[i] > bc[i]) ? ab[i] : bc[i];
							if ( ac[i] > max ) return false;
						}	
						return true;						
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});						
			
			
			entry("AssociativityTestArity2D1",  new TestArity2With3Function<Double[],Double[],Double[],Double[]>("TestArity2With3Function<Double[],Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b, Double[] c ) {
					try {
						Double[] ab = fut.evalAt(a, b);
						Double[] bc = fut.evalAt(b, c);
						
						Double[] ab_c = fut.evalAt(ab, c);
						Double[] a_bc = fut.evalAt(a, bc);
						
						int len = ab_c.length;
						for (int i=0; i < len; i++) {
							if ( !ab_c[i].equals(a_bc[i]) )
								return false;
						}
						return true;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("IdentityOfIndiscerniblesTestArity2D1",  new TestArity2Function<Double[],Double[],Double[]>("TestArity2Function<Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double[] _result = fut.evalAt(a, b);
						
						int len = _result.length;
						Double[] zResult = new Double[len];
						for (int i=0; i < len; i++) zResult[i] = 0.0d;
						
						if ( _result.equals(zResult) && a.equals(b) ) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				
			
			entry("CommutativityTestArity2D1",  new TestArity2Function<Double[],Double[],Double[]>("TestArity2Function<Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double[] ab = fut.evalAt(a, b);
						Double[] ba = fut.evalAt(b, a);
						
						int len = ab.length;
						for (int i=0; i < len; i++) {
							if ( !ab[i].equals(ba[i])) 
								return false;							
						}							
						return true;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});			
			
			entry("CommutativityTestArity2D1D",  new TestArity2Function<Double[],Double[],Double>("TestArity2Function<Double[],Double[],Double>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double ba = fut.evalAt(b, a);
						
						if ( ab.equals(ba)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				

			entry("SymmetryTestArity2D1",  new TestArity2Function<Double[],Double[],Double[]>("TestArity2Function<Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double[] ab = fut.evalAt(a, b);
						Double[] ba = fut.evalAt(b, a);
						
						int len = ab.length;
						for (int i=0; i < len; i++) {
							if ( !ab[i].equals(ba[i])) 
								return false;							
						}							
						return true;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});
			
			entry("SymmetryTestArity2D1D", new TestArity2Function<Double[],Double[],Double>("TestArity2Function<Double[],Double[],Double>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double ba = fut.evalAt(b, a);
						if ( a == null || b==null | ab==null || ba==null ) return false;
						if ( ab.equals(ba)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});		
			
			entry("SymmetryTestArityMD", new TestArity2Function<ImageData,ImageData,Double>("TestArity2Function<ImageData,ImageData,Double>") {			
				public Boolean evalAt( ImageData a, ImageData b ) {
					try {
						Double ab = fut.evalAt(a, b);
						Double ba = fut.evalAt(b, a);
						if ( a == null || b==null | ab==null || ba==null ) return false;
						if ( ab.equals(ba)) 
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				
			

			entry("NonNegativityTestArity2D1",   new TestArity2Function<Double[],Double[],Double[]>("TestArity2Function<Double[],Double[],Double[]>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double[] ab = fut.evalAt(a, b);
						
						int len = ab.length;
						for (int i=0; i < len; i++) {
							if ( ab[i] < 0.0d ) 
								return false;
						}
						return true;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				

			entry("NonNegativityTestArity2D1D",  new TestArity2Function<Double[],Double[],Double>("TestArity2Function<Double[],Double[],Double>") {			
				public Boolean evalAt( Double[] a, Double[] b ) {
					try {
						Double ab = fut.evalAt(a, b);
						
							if ( ab < 0.0d ) 
								return false;
							else
								return true;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});	
			
			entry("TestAddToOne",  new TestArity4Function<Arity2Function<Double[],Double[],Double>,Arity2Function<Double[],Double[],Double>,Double[],Double[],Boolean>("TestAddToOne") {			
				public Boolean evalAt( Arity2Function<Double[],Double[],Double> a, Arity2Function<Double[],Double[],Double> b, Double[] P, Double[] Q ) {
					try {
						Double r1 = a.evalAt(P,Q);
						Double r2 = b.evalAt(P,Q);
						if ( r1 + r2 == 1.0d )
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});		
			
			entry("TestMultToOne",  new TestArity4Function<Arity2Function<Double[],Double[],Double>,Arity2Function<Double[],Double[],Double>,Double[],Double[],Boolean>("TestAddToOne") {			
				public Boolean evalAt( Arity2Function<Double[],Double[],Double> a, Arity2Function<Double[],Double[],Double> b, Double[] P, Double[] Q ) {
					try {
						Double r1 = a.evalAt(P,Q);
						Double r2 = b.evalAt(P,Q);
						if ( r1 * r2 == 1.0d )
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				

			entry("TestEquiv",  new TestArity4Function<Arity2Function<Double[],Double[],Double>,Arity2Function<Double[],Double[],Double>,Double[],Double[],Boolean>("TestAddToOne") {			
				public Boolean evalAt( Arity2Function<Double[],Double[],Double> a, Arity2Function<Double[],Double[],Double> b, Double[] P, Double[] Q ) {
					try {
						Double r1 = a.evalAt(P,Q);
						Double r2 = b.evalAt(P,Q);
						if ( r1.equals(r2) )
							return true;
						else
							return false;
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				
			
			
			entry("TestFile",  new TestArity1Function<String,Boolean>("TestArity1Function<String,Boolean>") {			
				public Boolean evalAt( String file ) {
					try {
						Boolean _result = true;
						Boolean _res = true;
						TestFile f = new TestFile();
						
						
						_res = f.testFileExists( file );
						out("FileTest: fileExists(" + file + "): (expected=true,actual=" + _res +") -> " + ((_res==true)? "PASSED" : "FAILED") );
						_result &= _res;
						
						_res = f.testFileIsReadable(file);
						out("FileTest: fileIsReadable(" + file + "): (expected=true,actual=" + _res +") -> " + ((_res==true)? "PASSED" : "FAILED") );
						_result &= _res;
						
							//_res = f.testFileIsValidSize(file);
							//out("FileTest: fileIsValidSize(" + file + "): (expected=true,actual=" + _res +") -> " + ((_res==true)? "PASSED" : "FAILED") );
							//_result &= _res;
												
						_res = f.testFileIsNotNull(file);
						out("FileTest: fileIsNotNull(" + file + "): (expected=true,actual=" + _res +") -> " + ((_res==true)? "PASSED" : "FAILED") );
						_result &= _res;						
						
						// f.testFileIsInExpectedDirectory();
						return _result;	
					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});		
			entry("TestCompatibilities",  new Arity1Function<String,Boolean>("Arity1Function<String,Boolean>") {			
				public Boolean evalAt( String file ) {
					try {

					}
					catch( Exception e ) { handleException(e); }
					return null;
				}		
				public void initFunction()
				{
					// XX
					addMeasureCategories( Array.array("XX") );	
					addMeasureOpEquivalents( Array.array("XX"));
					addDependentOps( Array.array("XX") );
					addProperties( Array.array("XX"));
					addFaultCategories( Array.array("XX") );					
				}
			});				
			
	}
	
	//////////////////////////////
	
	public Boolean testAssociativity( Double ab_c, Double a_bc )
	{
		Boolean _result = false;
		if ( ab_c.equals(a_bc)) 
			_result = true;
		out("PropertyTest Associativity (a,b)=("+ab_c+","+a_bc+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	
	public Boolean testAssociativity( Double[] ab_c, Double[] a_bc )
	{	
		Boolean _result = true;
		int len = ab_c.length;
		for (int i=0; i < len; i++) {
			if ( !ab_c[i].equals(a_bc[i]) ) {
				_result = false;
				break;
			}
		}
		out("PropertyTest Associativity (a,b)=("+d2s(ab_c)+","+d2s(a_bc)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;	
	}

	public Boolean testAssociativity( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double,Double,Double,Double> testHarness = new TestArity2With3Function<Double,Double,Double,Double>();		
		Function curFx = getByName("AssociativityTestArity2Double");
		testHarness = (TestArity2With3Function<Double,Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
		out("PropertyTest Associativity (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testAssociativity( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double[],Double[],Double[],Double[]> testHarness = new TestArity2With3Function<Double[],Double[],Double[],Double[]>();		
		Function curFx = getByName("AssociativityTestArity2D1");
		testHarness = (TestArity2With3Function<Double[],Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
		out("PropertyTest Associativity (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;		
	}
	
	public Boolean testCommutatitivy( Double ab, Double ba ) {
		Boolean _result = false;
		if ( ab.equals(ba) ) 
			_result = true;		
		out("PropertyTest Commutativity (a,b)=("+ab+","+ba+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	
	public Boolean testCommutatitivy( Double[] ab, Double[] ba ) 
	{
		Boolean _result = false;
		
		int len = ab.length;
		for (int i=0; i < len; i++) {
			if ( !ab[i].equals(ba[i]) ) { 
				_result = false;
				break;
			}
		}
		out("PropertyTest Commutativity (a,b)=("+d2s(ab)+","+d2s(ba)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	

	public Boolean testCommutativity( Arity2Function<Double,Double,Double> op, Double a, Double b  )
	{
		Boolean _result = true;
		TestArity2Function<Double,Double,Double> testHarness = new TestArity2Function<Double,Double,Double>();		
		Function curFx = getByName("CommutativityTestArity2Double");
		testHarness = (TestArity2Function<Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Commutativity (a,b)=("+a+","+b+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;	
	}	
	
	public Boolean testCommutativity( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double[]> testHarness = new TestArity2Function<Double[],Double[],Double[]>();		
		Function curFx = getByName("CommutativityTestArity2D1");
		testHarness = (TestArity2Function<Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Commutativity (a,b)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}
	
	public Boolean testCommutativityD1D( Arity2Function<Double[],Double[],Double> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double> testHarness = new TestArity2Function<Double[],Double[],Double>();		
		Function curFx = getByName("CommutativityTestArity2D1D");
		testHarness = (TestArity2Function<Double[],Double[],Double>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Commutativity (a,b)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testSymmetry( Double ab, Double ba ) {
		Boolean _result = false;
		if ( ab.equals(ba) ) 
			_result = true;
		out("PropertyTest Symmetry (a,b)=("+ab+","+ba+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	
	public Boolean testSymmetry( Double[] ab, Double[] ba ) 
	{
		Boolean _result = true;
		int len = ab.length;
		for (int i=0; i < len; i++) {
			if ( !ab[i].equals(ba[i]) ) {				
				_result = false;
				break;
			}				
		}		
		out("PropertyTest Symmetry (a,b)=("+d2s(ab)+","+d2s(ba)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	
	
	public Boolean testSymmetry( Arity2Function<Double,Double,Double> op, Double a, Double b  )
	{
		Boolean _result = true;
		TestArity2Function<Double,Double,Double> testHarness = new TestArity2Function<Double,Double,Double>();		
		Function curFx = getByName("SymmetryTestArity2Double");
		testHarness = (TestArity2Function<Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		//out("PropertyTest Symmetry (a,b,c)=("+a+","+b+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}	
	
	public Boolean testSymmetry( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double[]> testHarness = new TestArity2Function<Double[],Double[],Double[]>();		
		Function curFx = getByName("SymmetryTestArity2D1");
		testHarness = (TestArity2Function<Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Symmetry (a,b,c)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}	

	public Boolean testSymmetryD1D( Arity2Function<Double[],Double[],Double> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double> testHarness = new TestArity2Function<Double[],Double[],Double>();		
		Function curFx = getByName("SymmetryTestArity2D1D");
		testHarness = (TestArity2Function<Double[],Double[],Double>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Symmetry (a,b)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}	
	
	public Boolean testSymmetryMD( Arity2Function<ImageData,ImageData,Double> op, ImageData a, ImageData b )
	{
		Boolean _result = true;
		TestArity2Function<ImageData,ImageData,Double> testHarness = new TestArity2Function<ImageData,ImageData,Double>();		
		Function curFx = getByName("SymmetryTestArity2D1D");
		testHarness = (TestArity2Function<ImageData,ImageData,Double>)curFx;				
				testHarness.fut( (Arity2Function<ImageData,ImageData,Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
		out("PropertyTest Symmetry (a,b)=("+im2s(a)+","+im2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}	
	
	public Boolean testNonNegativity( Double a ) {
		Boolean _result = true;
		if ( a == null ) return false;
		if ( a < 0.0d ) 
			_result = false;
		//out("PropertyTest NonNegativity (a)=("+a+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;
	}
	
	public Boolean testNonNegativity( Double[] a ) {
		Boolean _result = true;
		int len = a.length;
		for (int i=0; i < len; i++) {
			if ( a[i] < 0.0d ) _result = false; break;
		}
		out("PropertyTest NonNegativity (a)=("+d2s(a)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return true;
	}
	
	
	public Boolean testNonNegativity( Arity1Function<Double,Double> op, Double a )
	{	
		Boolean _result = true;
	    Function curFx = null;	
		curFx = getByName("NonNegativityTestArity1Double");
		TestArity1Function testHarness = (TestArity1Function)curFx;	
		if ( curFx == null ) return null;
				testHarness.fut( op );								
				testHarness.test( a );
		_result = testHarness.testResult();		
		out("PropertyTest NonNegativity (a)=("+a+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}
	
	public Boolean testNonNegativity( Arity2Function<Double,Double,Double> op, Double a, Double b )
	{
		Boolean _result = true;
		TestArity2Function<Double,Double,Double> testHarness = new TestArity2Function<Double,Double,Double>();		
		Function curFx = getByName("NonNegativityTestArity2Double");
		testHarness = (TestArity2Function<Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();	
				out("PropertyTest NonNegativity (a,b)=("+a+","+b+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testNonNegativity( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double[]> testHarness = new TestArity2Function<Double[],Double[],Double[]>();		
		Function curFx = getByName("NonNegativityTestArity2D1");
		testHarness = (TestArity2Function<Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
				out("PropertyTest NonNegativity (a,b)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;						
	}	
	
	public Boolean testTriangleInequality( Double ac, Double ab, Double bc )
	{
		Boolean _result = false;
		if ( ac <= (ab + bc)) 
			_result = true;		
		out("PropertyTest TriangleInequality (a,b,c)=("+ac+","+ab+","+bc+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;	
	}
	
	public Boolean testStrongerTriangleInequality( Double ac, Double ab, Double bc )
	{
		Boolean _result = false;
		Double max = (ab > bc) ? ab : bc;
		if ( ac <= max ) 
			_result = true;
		out("PropertyTest StrongerTriangleInequality (a,b,c)=("+ac+","+ab+","+bc+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;	
	}	
	
	public Boolean testTriangleInequality( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double,Double,Double,Double> testHarness = new TestArity2With3Function<Double,Double,Double,Double>();		
		Function curFx = getByName("TriangleInequalityTestArity2Double");
		testHarness = (TestArity2With3Function<Double,Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest TriangleInequality (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testTriangleInequality( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double[],Double[],Double[],Double[]> testHarness = new TestArity2With3Function<Double[],Double[],Double[],Double[]>();		
		Function curFx = getByName("TriangleInequalityTestArity2D1");
		testHarness = (TestArity2With3Function<Double[],Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest TriangleInequality (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;						
	}
	
	public Boolean testStrongerTriangleInequality( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double,Double,Double,Double> testHarness = new TestArity2With3Function<Double,Double,Double,Double>();		
		Function curFx = getByName("StrongerTriangleInequalityTestArity2Double");
		testHarness = (TestArity2With3Function<Double,Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest StrongerTriangleInequality (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testStrongerTriangleInequality( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double[],Double[],Double[],Double[]> testHarness = new TestArity2With3Function<Double[],Double[],Double[],Double[]>();		
		Function curFx = getByName("StrongerTriangleInequalityTestArity2D1");
		testHarness = (TestArity2With3Function<Double[],Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest StrongerTriangleInequality (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;						
	}	
	
	public Boolean testIdentityOfIndiscernibles( Double a, Double b, Double c )
	{
		Boolean _result = false;
		if ( c.equals(0.0d) && a.equals(b) ) 
			_result = true;		
		out("PropertyTest IdentityOfIndiscernibles (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;		
	}
	
	public Boolean testIdentityOfIndiscernibles( Double[] a, Double[] b, Double c )
	{
		Boolean _result = true;		
		int len = a.length;
		for (int i=0; i < len; i++) {			
			if ( c.equals(0.0d) && !a[i].equals(b[i]) ) { 
				_result = false;
				break;
			}
		}			
		//out("PropertyTest IdentityOfIndiscernibles (a,b,c)=("+d2s(a)+","+d2s(b)+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;		
	}	
	
	public Boolean testIdentityOfIndiscernibles(ImageData a, ImageData b, Double c )
	{
		Boolean _result = true;		
		if ( c.equals(0.0d) && a.equals(b) )
			_result = true;
		else
			_result = false;
			
			//out("PropertyTest IdentityOfIndiscernibles (a,b,c)=("+d2s(a)+","+d2s(b)+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;		
	}	
	
	public Boolean testIdentityOfIndiscernibles( Arity2Function<Double,Double,Double> op, Double a, Double b )
	{
		Boolean _result = true;
		TestArity2Function<Double,Double,Double> testHarness = new TestArity2Function<Double,Double,Double>();		
		Function curFx = getByName("IdentityOfIndiscerniblesTestArity2Double");
		testHarness = (TestArity2Function<Double,Double,Double>)curFx;			
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b );
				_result = testHarness.testResult();
				out("PropertyTest IdentityOfIndiscernibles (a,b)=("+a+","+b+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;						
	}	

	public Boolean testIdentityOfIndiscernibles( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b )
	{
		Boolean _result = true;
		TestArity2Function<Double[],Double[],Double[]> testHarness = new TestArity2Function<Double[],Double[],Double[]>();		
		Function curFx = getByName("IdentityOfIndiscerniblesTestArity2D1");
		testHarness = (TestArity2Function<Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b );				
				_result = testHarness.testResult();
				out("PropertyTest IdentityOfIndiscernibles (a,b,c)=("+d2s(a)+","+d2s(b)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testTranslationInvariance( Double a, Double ac )
	{
		Boolean _result = false;
		if ( a.equals(ac) ) 
			_result = true;		
		out("PropertyTest TranslationInvariance (a,b,c)=("+a+","+ac+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;				
	}
	
	public Boolean testTranslationInvariance( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double,Double,Double,Double> testHarness = new TestArity2With3Function<Double,Double,Double,Double>();		
		Function curFx = getByName("TranslationInvarianceTestArity2Double");
		testHarness = (TestArity2With3Function<Double,Double,Double,Double>)curFx;				
				testHarness.fut( (Arity2Function<Double,Double,Double>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest TranslationInvariance (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	public Boolean testTranslationInvariance( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = true;
		TestArity2With3Function<Double[],Double[],Double[],Double[]> testHarness = new TestArity2With3Function<Double[],Double[],Double[],Double[]>();		
		Function curFx = getByName("TranslationInvarianceTestArity2D1");
		testHarness = (TestArity2With3Function<Double[],Double[],Double[],Double[]>)curFx;				
				testHarness.fut( (Arity2Function<Double[],Double[],Double[]>) op );				
				testHarness.test( a, b, c );
				_result = testHarness.testResult();
				out("PropertyTest TranslationInvariance (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}	
	
	public Boolean testPositiveDefiniteness( Double a, Double b, Double c )
	{
		Boolean _result = false;		
		if ( testNonNegativity(a) && testNonNegativity(b) && testNonNegativity(c)  &&
				testIdentityOfIndiscernibles(a,b,c)
		   ) 
		{
			_result = true;
		}
		out("PropertyTest PositiveDefiniteness (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;			
	}	

	public Boolean testPositiveDefiniteness( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = false;		
		if ( testNonNegativity(op,a,b) && testNonNegativity(op,b,a) && 
				testIdentityOfIndiscernibles(op,a,b) && testIdentityOfIndiscernibles(op,b,a)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest PositiveDefiniteness (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;						
	}	
	
	public Boolean testPositiveDefiniteness( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = false;		
		if ( 
				testNonNegativity(op,a,b) 
				   &&
				testIdentityOfIndiscernibles(op,a,b) 
		   ) 
		{
			_result = true;
		}
				
		out("PropertyTest PositiveDefiniteness (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;				
	}	
	
	public Boolean testMetricity( Double a, Double b, Double c )
	{
		Boolean _result = false;		
		if ( 
				testNonNegativity(a) && testNonNegativity(b)  
				   &&
				testIdentityOfIndiscernibles(a,b,c)
					&&
				testTriangleInequality(a,b,c)
					&&
				testSymmetry(a,b)
		   ) 
		{
			_result = true;
		}
				
		out("PropertyTest Metricity (a,b,c)=("+a+","+b+","+c+") : -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;				
	}	
	
	public Boolean testMetricity( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = false;	
		if ( 
				testNonNegativity(op,a,b) 
				   &&
				testIdentityOfIndiscernibles(op,a,b)
					&&
				testTriangleInequality(op,a,b,c)
					&&
				testSymmetry(op,a,b)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest Metricity (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}		
	
	public Boolean testMetricity( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{
		Boolean _result = false;	
		if ( 
				testNonNegativity(op,a,b) 
				   &&
				testIdentityOfIndiscernibles(op,a,b)
					&&
				testTriangleInequality(op,a,b,c)
					&&
				testSymmetry(op,a,b)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest Metricity (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}		
	
	public String d2s( Double[] d ) {
		int len = d.length;
		String str = "";
		str += "new Double[]{";
		for (int i=0; i < len; i++) {
			if ( i != 0 ) str += ",";
			if ( d[i] == null ) str += "null";
			else
				str += d[i];
		}
		str += "}";
		return str;
	}
	
	public String im2s( ImageData im ) {
		String str = "new ImageData( new double[][][]";
		double[][][] values = im.getValues();
		int len1 = values.length;
		int len2 = values[0].length;
		int len3 = values[0][0].length;
		str += "{";
		for (int i=0; i < len1; i++) {
			if ( i!=0 ) str += ",";
			str += "{";
			for (int j=0; j < len2; j++) {
				if (j!=0) str += ",";
				str += "{";
				for (int k=0; k < len3; k++) {
					if ( k!=0 ) str += ",";
					str += values[i][j][k] + "d";
				}
				str += "}";
			}
			str += "}";
		}
		str += "}";
		str += ")";
		return str;
	}	
	
	public Boolean testUltraMetricity( Double a, Double b, Double c )
	{		
		Boolean _result = false;		
		if ( 
				testNonNegativity(a) && testNonNegativity(b)  
				   &&
				testIdentityOfIndiscernibles(a,b,c)
					&&
				testStrongerTriangleInequality(a,b,c)
					&&
				testSymmetry(a,b)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest UltraMetricity (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;				
	}		
	
	public Boolean testUltraMetricity( Arity2Function<Double,Double,Double> op, Double a, Double b, Double c )
	{
		Boolean _result = false;
		if ( 
				testNonNegativity(op,a,b) 
				   &&
				testIdentityOfIndiscernibles(op,a,b)
					&&
				testStrongerTriangleInequality(op,a,b,c)
					&&
				testSymmetry(op,a,b)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest UltraMetricity  (a,b,c)=("+a+","+b+","+c+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}	
	
	public Boolean testUltraMetricity( Arity2Function<Double[],Double[],Double[]> op, Double[] a, Double[] b, Double[] c )
	{		
		Boolean _result = false;		
		if ( 
				testNonNegativity(op,a,b) 
				   &&
				testIdentityOfIndiscernibles(op,a,b)
					&&
				testStrongerTriangleInequality(op,a,b,c)
					&&
				testSymmetry(op,a,b)
		   ) 
		{
			_result = true;
		}				
		out("PropertyTest UltraMetricity  (a,b,c)=("+d2s(a)+","+d2s(b)+","+d2s(c)+"): -> " + ((_result==true) ? "PASSED" : "FAILED") );
		return _result;					
	}
	
	//////////////////////////////
	
	protected Functions FXS = new Functions();

	public void out( String s ) {
		//System.out.println(s);
	}
	
	
	//////////////////////////////
	
	public void entry(String name, Function function ) 
	{
		function.name(name);
		TESTS.add(function);
	}
	
	public Function getByName( String name ) 
	{
		int len = TESTS.length();
		Function curFx = null;
		for (int i=0; i < len; i++){
		  if ( TESTS.get(i) == null ) continue;		  
			curFx = (Function)TESTS.get(i);
			if ( curFx.name() == null ) continue;			
			if ( Utils._cmp(curFx.name(), name) ) return curFx;
		}		
		return new Function("EMPTY_TEST");		
	}
	
	public Function[] getByType( String type ) 
	{		
		int len = TESTS.length();
		Function curFx = null;
			
		int ctr = 0;
		TA _list = new TA();
		
		for (int i=0; i < len; i++){		
			if ( TESTS.get(i) == null ) continue;			
			curFx = (Function)TESTS.get(i);
			if ( curFx.type() != null ) {
				if ( Utils._cmp(curFx.type(), type) ) {
					_list.add( curFx );
					ctr++;
				}
			}			
		}
		Function[] list = new Function[ctr];
		for (int i=0; i < ctr; i++) {
			list[i] = (Function)_list.get(i);
		}
		return list;
	}	
}
