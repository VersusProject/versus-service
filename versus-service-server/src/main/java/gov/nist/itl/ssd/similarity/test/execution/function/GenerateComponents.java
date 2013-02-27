package gov.nist.itl.ssd.similarity.test.execution.function;

import java.io.PrintWriter;
import java.util.Set;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.TCLFile;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt1;
import gov.nist.itl.ssd.similarity.test.demo.scenario.altImpls.EuclideanL2MeasureAlt2;
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
import gov.nist.itl.versus.similarity.comparisons.measure.impl.*;


public class GenerateComponents 
{
	protected static TA<Measure> all_measures = new TA(Array.array(
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
	
	public static void genAll()
	{
		int len = all_measures_str.length();
		String s = "";
		String EOL = "\n";
		String meas = "";

		try {
			TCLFile file = new TCLFile();			
			PrintWriter pw = file.open_overwrite("measureList.txt");
			Object o = null;
			Set features = null;
			int idx = -1;
			
			for (int i=0; i < len; i++) {
				meas = all_measures_str.get(i);
				s = "			entry(\"" + meas + "\", new Component<Measure>(\"Measure\") {" + EOL;
				s += "				public void initComponent() {"+ EOL;
				s += "					component( new " + meas + "() );"+ EOL;
				s += "				}	"+ EOL;
				s += "			});	"+ EOL;
				pw.println(s);
			}
		file.close(pw);
		}
		catch(Exception e ) {}
	}	
	
	public static void genHS()
	{
		int len = histogram_measures_str.length();
		String s = "";
		String EOL = "\n";
		String meas = "";

		try {
			TCLFile file = new TCLFile();			
			PrintWriter pw = file.open_overwrite("histogram_measureList.txt");
			Object o = null;
			Set features = null;
			int idx = -1;
			
			for (int i=0; i < len; i++) {
				meas = histogram_measures_str.get(i);
				s = "			entry(\"" + meas + "\", new Component<Measure>(\"Measure\") {" + EOL;
				s += "				public void initComponent() {"+ EOL;
				s += "					component( new " + meas + "() );"+ EOL;
					//features = (Set)((Measure)all_measures.get(i)).supportedFeaturesTypes();

				s += "                  addCompatibility( new Component<Adapter>(\"HistogramAdapter\", (Adapter)new ImageObjectAdapter()) ); " + EOL;
				s += "                  addCompatibility( new Component<Descriptor>(\"HistogramDescriptor\", (Descriptor)new RGBHistogramDescriptor()) );"+ EOL;
				s += "                  addCompatibility( new Component<Descriptor>(\"HistogramDescriptor\", (Descriptor)new GrayscaleHistogramDescriptor()) );"+ EOL;
				s += "                  addCompatibility( new Component<Extractor>(\"HistogramExtractor\", (Extractor)new RGBHistogramExtractor()) );"+ EOL;
				s += "                  addCompatibility( new Component<Extractor>(\"HistogramExtractor\", (Extractor)new GrayscaleHistogramExtractor()) );"+ EOL;				
				s += "				}	"+ EOL;
				s += "			});	"+ EOL;
				pw.println(s);
			}
		file.close(pw);
		}
		catch(Exception e ) {}
	}
	
	public static void genPx()
	{
		int len = pixel_measures_str.length();
		String s = "";
		String EOL = "\n";
		String meas = "";

		try {
			TCLFile file = new TCLFile();			
			PrintWriter pw = file.open_overwrite("pixel_measureList.txt");
			Object o = null;
			Set features = null;
			int idx = -1;
			
			for (int i=0; i < len; i++) {
				meas = pixel_measures_str.get(i);
				s = "			entry(\"" + meas + "\", new Component<Measure>(\"Measure\") {" + EOL;
				s += "				public void initComponent() {"+ EOL;
				s += "					component( new " + meas + "() );"+ EOL;
					//features = (Set)((Measure)all_measures.get(i)).supportedFeaturesTypes();

				s += "                  addCompatibility( new Component<Adapter>(\"PixelAdapter\", (Adapter)new ImageObjectAdapter()) );"+ EOL;
				s += "                  addCompatibility( new Component<Adapter>(\"PixelAdapter\", (Adapter)new LabeledImageObjectAdapter()) );"+ EOL;			
				s += "                  addCompatibility( new Component<Descriptor>(\"PixelDescriptor\", (Descriptor)new ThreeDimensionalDoubleArrayFeature()) );"+ EOL;
				s += "                  addCompatibility( new Component<Descriptor>(\"PixelDescriptor\", (Descriptor)new LabeledThreeDimensionalDoubleArrayFeature()) );"+ EOL;
				s += "                  addCompatibility( new Component<Extractor>(\"PixelExtractor\",   (Extractor)new ArrayFeatureExtractor()) );"+ EOL;
				s += "                  addCompatibility( new Component<Extractor>(\"PixelExtractor\",   (Extractor)new LabeledArrayFeatureExtractor()) );"+ EOL;				
				s += "				}	"+ EOL;
				s += "			});	"+ EOL;
				pw.println(s);
			}
		file.close(pw);
		}
		catch(Exception e ) {}
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
