package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt1;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt2;
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

public class Components<A> 
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
	
	protected static TA<String> all_measures_str = new TA(Array.array(
			"EuclideanL2MeasureAlt1",
			"EuclideanL2MeasureAlt2",
			"AdditiveSymmetricChiSquaredMeasure",
		    "AdjustedRandIndexMeasure",
			"AvgDifferenceMeasure",
			"BhattacharyyaMeasure",
			"CanberraMeasure",
			"ChebyshevLInfMeasure",
			"CityBlockL1Measure",
			"ClarkMeasure",
			"CosineMeasure",
			"CzekanowskiDMeasure",
			"CzekanowskiMeasure",
			"DiceDMeasure",
			"DiceMeasure",
			"DicePixelMeasure",
			"DivergenceMeasure",
			"EuclideanL2Measure",
			"FidelityMeasure",
			"GowerMeasure",
			"HarmonicMeanMeasure",
			"HellingerMeasure",
			"InnerProductMeasure",
			"IntersectionDMeasure",
			"IntersectionMeasure",
			"JaccardDMeasure",
			"JaccardMeasure",
			"JaccardPixelMeasure",
			"JeffreysMeasure",
			"JensenDifferenceMeasure",
			"JensenShannonMeasure",
			"KDivergenceMeasure",
			"KulczynskiMeasure",
			"KulczynskiSMeasure",
			"KullbackLeiblerMeasure",
			"KumarHassebrookPCEMeasure",
			"KumarJohnsonDifferenceMeasure",
			"LorentzianMeasure",
			"MatusitaDMeasure",
			"MatusitaMeasure",
			"MinkowskiMeasure",
			"MotykaDMeasure",
			"MotykaMeasure",
			"NeymanChiSquaredMeasure",
			"PearsonChiSquaredMeasure",
			"ProbabilisticSymmetricChiSquaredMeasure",
			"RandIndexMeasure",
			"RuzickaMeasure",
			"SoergelMeasure",
			"SorensenMeasure",
			"SquaredChiSquaredMeasure",
			"SquaredChordDMeasure",
			"SquaredChordMeasure",
			"SquaredEuclideanMeasure",
			"TanejaDifferenceMeasure",
			"TanimotoMeasure",
			"TopsoeMeasure",
			"TotalErrorRateEvaluationMeasure",
			"TotalErrorRateTestMeasure",
			"WaveHedgesMeasure"			
			));	
	
	protected static TA<String> histogram_measures_str = new TA(Array.array(
			"EuclideanL2MeasureAlt1",
			"EuclideanL2MeasureAlt2",
			"AdditiveSymmetricChiSquaredMeasure",
			"AvgDifferenceMeasure",
			"BhattacharyyaMeasure",
			"CanberraMeasure",
			"ChebyshevLInfMeasure",
			"CityBlockL1Measure",
			"ClarkMeasure",
			"CosineMeasure",
			"CzekanowskiDMeasure",
			"CzekanowskiMeasure",
			"DiceDMeasure",
			"DiceMeasure",
			"DivergenceMeasure",
			"EuclideanL2Measure",
			"FidelityMeasure",
			"GowerMeasure",
			"HarmonicMeanMeasure",
			"HellingerMeasure",
			"InnerProductMeasure",
			"IntersectionDMeasure",
			"IntersectionMeasure",
			"JaccardDMeasure",
			"JaccardMeasure",
			"JeffreysMeasure",
			"JensenDifferenceMeasure",
			"JensenShannonMeasure",
			"KDivergenceMeasure",
			"KulczynskiMeasure",
			"KulczynskiSMeasure",
			"KullbackLeiblerMeasure",
			"KumarHassebrookPCEMeasure",
			"KumarJohnsonDifferenceMeasure",
			"LorentzianMeasure",
			"MatusitaDMeasure",
			"MatusitaMeasure",
			"MinkowskiMeasure",
			"MotykaDMeasure",
			"MotykaMeasure",
			"NeymanChiSquaredMeasure",
			"PearsonChiSquaredMeasure",
			"ProbabilisticSymmetricChiSquaredMeasure",
			"RuzickaMeasure",
			"SoergelMeasure",
			"SorensenMeasure",
			"SquaredChiSquaredMeasure",
			"SquaredChordDMeasure",
			"SquaredChordMeasure",
			"SquaredEuclideanMeasure",
			"TanejaDifferenceMeasure",
			"TanimotoMeasure",
			"TopsoeMeasure",
			"WaveHedgesMeasure"			
			));

	protected static TA<String> pixel_measures_str = new TA(Array.array(
		    "AdjustedRandIndexMeasure",
			"DicePixelMeasure",
			"JaccardPixelMeasure",
			"RandIndexMeasure",
			"TotalErrorRateEvaluationMeasure",
			"TotalErrorRateTestMeasure"
			));	

	protected static TA<Measure> histogram_measures = new TA(Array.array(
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
	
	protected static TA<LabeledMeasure> pixel_measures = new TA(Array.array(
			new AdjustedRandIndexMeasure(),
			new DicePixelMeasure(),
			new JaccardPixelMeasure(),
			new RandIndexMeasure(),
			new TotalErrorRateEvaluationMeasure(),
			new TotalErrorRateTestMeasure()
			));
	
	protected static TA<Adapter> adapters = new TA(Array.array(
			new ImageObjectAdapter(),
			new LabeledImageObjectAdapter()
			));	
	
	protected static TA<Extractor> extractors = new TA(Array.array(
			new RGBHistogramExtractor(),
			new GrayscaleHistogramExtractor(),
			new ArrayFeatureExtractor(),
			new LabeledArrayFeatureExtractor()
			));	 
	
	protected static TA<Descriptor> descriptors = new TA(Array.array(
			new RGBHistogramDescriptor(),
			new GrayscaleHistogramDescriptor(),
			new ThreeDimensionalDoubleArrayFeature(),
			new LabeledThreeDimensionalDoubleArrayFeature()
			));	 
	
	public Components(){super(); load(); }
	
	public TA<String> all_measures_str(){ return all_measures_str; }
	public TA<String> histogram_measures_str(){ return histogram_measures_str; }
	public TA<String> pixel_measures_str(){ return pixel_measures_str; }

	protected  TA<Component> COMPONENTS = new TA();
	public TA<Component> components(){ return COMPONENTS; }
	public void components( TA<Component> v ){ this.COMPONENTS=v; }
	
		public void load() 
		{
		 // descriptors
			entry("RGBHistogramDescriptor", new Component<Descriptor>("Descriptor") {			
				public void initComponent() {
					component( new RGBHistogramDescriptor() );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
				}	
			});
			entry("GrayscaleHistogramDescriptor", new Component<Descriptor>("Descriptor") {			
				public void initComponent() {
					component( new GrayscaleHistogramDescriptor() );
	                  addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                  addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );
				}	
			});
			entry("ThreeDimensionalDoubleArrayFeature", new Component<Descriptor>("Descriptor") {			
				public void initComponent() {
					component( new ThreeDimensionalDoubleArrayFeature() );
	                  addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
				}	
			});
			entry("LabeledThreeDimensionalDoubleArrayFeature", new Component<Descriptor>("Descriptor") {			
				public void initComponent() {
					component( new LabeledThreeDimensionalDoubleArrayFeature() );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );
				}	
			});
		
		// extractors
			entry("RGBHistogramExtractor", new Component<Extractor>("Extractor") {			
				public void initComponent() {
					component( new RGBHistogramExtractor() );
	                addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
				}	
			});
			entry("GrayscaleHistogramExtractor", new Component<Extractor>("Extractor") {			
				public void initComponent() {
					component( new GrayscaleHistogramExtractor() );
	                addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );
	                addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
				}	
			});
			entry("ArrayFeatureExtractor", new Component<Extractor>("Extractor") {			
				public void initComponent() {
					component( new ArrayFeatureExtractor() );
	                addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
	                  addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
				}	
			});
			entry("LabeledArrayFeatureExtractor", new Component<Extractor>("Extractor") {			
				public void initComponent() {
					component( new LabeledArrayFeatureExtractor() );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
				}	
			});		

			// adapters
			entry("ImageObjectAdapter", new Component<Adapter>("Adapter") {			
				public void initComponent() {
					component( new ImageObjectAdapter() );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );

				}	
			});
			entry("LabeledImageObjectAdapter", new Component<Adapter>("Adapter") {			
				public void initComponent() {
					component( new LabeledImageObjectAdapter() );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	
			
		// histogram measures
			entry("EuclideanL2MeasureAlt1", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new EuclideanL2MeasureAlt1() );
					measureOpImpl( "histogram_measure_euclidean_alt1" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("EuclideanL2MeasureAlt2", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new EuclideanL2MeasureAlt2() );
					measureOpImpl( "histogram_measure_euclidean_alt2" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("AdditiveSymmetricChiSquaredMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new AdditiveSymmetricChiSquaredMeasure() );
					measureOpImpl( "histogram_measure_additive_symmetric_chiSquared" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("AvgDifferenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new AvgDifferenceMeasure() );
					measureOpImpl( "histogram_measure_avg_difference" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("BhattacharyyaMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new BhattacharyyaMeasure() );
					measureOpImpl( "histogram_measure_bhattacharyya" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("CanberraMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new CanberraMeasure() );
					measureOpImpl( "histogram_measure_canberra" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("ChebyshevLInfMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new ChebyshevLInfMeasure() );
					measureOpImpl( "histogram_measure_chebyshev" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("CityBlockL1Measure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new CityBlockL1Measure() );
					measureOpImpl( "histogram_measure_city_block" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("ClarkMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new ClarkMeasure() );
					measureOpImpl( "histogram_measure_clark" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("CosineMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new CosineMeasure() );
					measureOpImpl( "histogram_measure_cosine" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("CzekanowskiDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new CzekanowskiDMeasure() );
					measureOpImpl( "histogram_measure_czekanowski_dCze" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("CzekanowskiMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new CzekanowskiMeasure() );
					measureOpImpl( "histogram_measure_czekanowski" );					
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("DiceDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new DiceDMeasure() );
					measureOpImpl( "histogram_measure_dice_dDice1" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("DiceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new DiceMeasure() );
					measureOpImpl( "histogram_measure_dice" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("DivergenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new DivergenceMeasure() );
					measureOpImpl( "histogram_measure_divergence" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("EuclideanL2Measure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new EuclideanL2Measure() );
					measureOpImpl( "histogram_measure_euclidean" );					
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("FidelityMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new FidelityMeasure() );
					measureOpImpl( "histogram_measure_fidelity" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("GowerMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new GowerMeasure() );
					measureOpImpl( "histogram_measure_gower" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("HarmonicMeanMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new HarmonicMeanMeasure() );
					measureOpImpl( "histogram_measure_harmonic_mean" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("HellingerMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new HellingerMeasure() );
					measureOpImpl( "histogram_measure_hellinger" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("InnerProductMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new InnerProductMeasure() );
					measureOpImpl( "histogram_measure_inner_product" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("IntersectionDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new IntersectionDMeasure() );
					measureOpImpl( "histogram_measure_intersection_dNonIS" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("IntersectionMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new IntersectionMeasure() );
					measureOpImpl( "histogram_measure_intersection_IS" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("JaccardDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JaccardDMeasure() );
					measureOpImpl( "histogram_measure_jaccard_dJac1" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("JaccardMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JaccardMeasure() );
					measureOpImpl( "histogram_measure_jaccard" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("JeffreysMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JeffreysMeasure() );
					measureOpImpl( "histogram_measure_jeffreys" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("JensenDifferenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JensenDifferenceMeasure() );
					measureOpImpl( "histogram_measure_jensen_difference" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("JensenShannonMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JensenShannonMeasure() );
					measureOpImpl( "histogram_measure_jensen_shannon" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KDivergenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KDivergenceMeasure() );
					measureOpImpl( "histogram_measure_k_divergence" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KulczynskiMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KulczynskiMeasure() );
					measureOpImpl( "histogram_measure_kulczynski" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KulczynskiSMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KulczynskiSMeasure() );
					measureOpImpl( "histogram_measure_kulczynski_s" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KullbackLeiblerMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KullbackLeiblerMeasure() );
					measureOpImpl( "histogram_measure_kullback_leibler" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KumarHassebrookPCEMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KumarHassebrookPCEMeasure() );
					measureOpImpl( "histogram_measure_kumar_hassebrook_pce" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("KumarJohnsonDifferenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new KumarJohnsonDifferenceMeasure() );
					measureOpImpl( "histogram_measure_kumar_johnson_difference" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("LorentzianMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new LorentzianMeasure() );
					measureOpImpl( "histogram_measure_lorentzian" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("MatusitaDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new MatusitaDMeasure() );
					measureOpImpl( "histogram_measure_matusita_dM2" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("MatusitaMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new MatusitaMeasure() );
					measureOpImpl( "histogram_measure_matusita" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("MinkowskiMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new MinkowskiMeasure() );
					measureOpImpl( "histogram_measure_minkowski" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("MotykaDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new MotykaDMeasure() );
					measureOpImpl( "histogram_measure_motyka_dMot" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("MotykaMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new MotykaMeasure() );
					measureOpImpl( "histogram_measure_motyka" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("NeymanChiSquaredMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new NeymanChiSquaredMeasure() );
					measureOpImpl( "histogram_measure_neyman_chiSquared" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("PearsonChiSquaredMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new PearsonChiSquaredMeasure() );
					measureOpImpl( "histogram_measure_pearson_chiSquared" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("ProbabilisticSymmetricChiSquaredMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new ProbabilisticSymmetricChiSquaredMeasure() );
					measureOpImpl( "histogram_measure_probabilistic_symmetric_chiSquared" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("RuzickaMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new RuzickaMeasure() );
					measureOpImpl( "histogram_measure_ruzicka" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SoergelMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SoergelMeasure() );
					measureOpImpl( "histogram_measure_soergel" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SorensenMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SorensenMeasure() );
					measureOpImpl( "histogram_measure_sorensen" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SquaredChiSquaredMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SquaredChiSquaredMeasure() );
					measureOpImpl( "histogram_measure_squared_chiSquared" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SquaredChordDMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SquaredChordDMeasure() );
					measureOpImpl( "histogram_measure_squared_chord" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SquaredChordMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SquaredChordMeasure() );
					measureOpImpl( "histogram_measure_squared_chord_Ssqc1" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("SquaredEuclideanMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new SquaredEuclideanMeasure() );
					measureOpImpl( "histogram_measure_squared_euclidean" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("TanejaDifferenceMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new TanejaDifferenceMeasure() );
					measureOpImpl( "histogram_measure_taneja_difference" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("TanimotoMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new TanimotoMeasure() );
					measureOpImpl( "histogram_measure_tanimoto" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("TopsoeMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new TopsoeMeasure() );
					measureOpImpl( "histogram_measure_topsoe" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	

			entry("WaveHedgesMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new WaveHedgesMeasure() );
					measureOpImpl( "histogram_measure_wave_hedges" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("RGBHistogramDescriptor", (Descriptor)new RGBHistogramDescriptor()) );
					addCompatibility( new Component<Extractor>("RGBHistogramExtractor", (Extractor)new RGBHistogramExtractor()) );
	                		addCompatibility( new Component<Descriptor>("GrayscaleHistogramDescriptor", (Descriptor)new GrayscaleHistogramDescriptor()) );
	                		addCompatibility( new Component<Extractor>("GrayscaleHistogramExtractor", (Extractor)new GrayscaleHistogramExtractor()) );                  
				}	
			});	
			

		// pixel measures
			entry("AdjustedRandIndexMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new AdjustedRandIndexMeasure() );
					measureOpImpl( "pixel_measure_ari" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	

			entry("DicePixelMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new DicePixelMeasure() );
					measureOpImpl( "pixel_measure_dice" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	

			entry("JaccardPixelMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new JaccardPixelMeasure() );
					measureOpImpl( "pixel_measure_jaccard" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	

			entry("RandIndexMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new RandIndexMeasure() );
					measureOpImpl( "pixel_measure_ri" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	

			entry("TotalErrorRateEvaluationMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new TotalErrorRateEvaluationMeasure() );
					measureOpImpl( "pixel_measure_tee" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	

			entry("TotalErrorRateTestMeasure", new Component<Measure>("Measure") {
				public void initComponent() {
					component( new TotalErrorRateTestMeasure() );
					measureOpImpl( "pixel_measure_tet" );
					addCompatibility( new Component<Adapter>("ImageObjectAdapter", (Adapter)new ImageObjectAdapter()) );
	                  		addCompatibility( new Component<Descriptor>("ThreeDimensionalDoubleArrayFeature", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );
	                  		addCompatibility( new Component<Extractor>("ArrayFeatureExtractor", (Extractor)new ArrayFeatureExtractor()) );
					addCompatibility( new Component<Adapter>("LabeledImageObjectAdapter", (Adapter)new LabeledImageObjectAdapter()) );
					addCompatibility( new Component<Descriptor>("LabeledThreeDimensionalDoubleArrayFeature", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );
					addCompatibility( new Component<Extractor>("LabeledArrayFeatureExtractor", (Extractor)new LabeledArrayFeatureExtractor()) );					  
				}	
			});	
			
	}
	
		public void entry(String name, Component component ) {			
			component.name(name);						
			COMPONENTS.add(component);
		}

		public int length(){ return COMPONENTS.length(); }
		
		public Component getByName( String name ) 
		{
			int len = COMPONENTS.length();
			Component comp = null;
			for (int i=0; i < len; i++)
			{
			  if ( COMPONENTS.get(i) == null ) continue;
			  
			  	comp = (Component)COMPONENTS.get(i);
			  
				if ( comp.name() == null ) continue;	
				//System.out.println("Component.getByName.name = (name,comp.name())=(" + name + "," + comp.name() + ")" );
				if ( Utils._cmp(comp.name(), name) ) return comp;
			}		
			return new Component("EMPTY_COMP");				
		}
		
		public Component[] getByType( String type ) 
		{		
			int len = COMPONENTS.length();
			TA _list = new TA();
			Component comp = null;			
			int ctr = 0;
			for (int i=0; i < len; i++){		
				if ( COMPONENTS.get(i) == null ) continue;			
				comp = (Component)COMPONENTS.get(i);
				if ( comp.type() != null ) {
					if ( Utils._cmp(comp.type(), type) ) {						
						_list.add( comp );
						ctr++;
					}
				}			
			}	
			Component[] list = new Component[ctr];
			for (int i=0; i < ctr; i++) {
				list[i] = (Component)_list.get(i);
			}
			return list;
		}	

	public static int indexOf( Object o, TA a ) {
		int len = a.length();
		Object o2 = null;
		for (int i=0; i < len; i++) {
			o2 = a.get(i);
			if ( o2.equals(o) ) return i;
		}
		return -1;
	}
	
	public static void main( String[] args ) 
	{
		//genAll();
		//genHS();
		//genPx();

		Components c = new Components();
		int len = c.length();
		TA<Component> COMP = c.components();
		for (int i=0; i < len; i++) {
			System.out.println( "component (name,type) = (" + COMP.get(i).name() + ", " + COMP.get(i).type() + ")" );
		}
	}
}
