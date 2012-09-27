package gov.nist.itl.ssd.similarity.test.fault.math_inconsistency;

import static gov.nist.itl.ssd.similarity.test.config.Config.FSEP;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.image;
import static gov.nist.itl.ssd.similarity.test.utils.Utils.out;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.config.Config;
import gov.nist.itl.ssd.similarity.test.execution.FaultTracker;
import gov.nist.itl.ssd.similarity.test.execution.Test;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Function;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Components;
import gov.nist.itl.ssd.similarity.test.execution.function.Functions;
import gov.nist.itl.ssd.similarity.test.execution.function.Tests;
import gov.nist.itl.ssd.similarity.test.input.source.RandomImageDataSource;
import gov.nist.itl.ssd.similarity.test.report.gen.Reporter;
import fj.data.Array;
import gov.nist.itl.versus.similarity.comparisons.ImageData;

public class TestMathIncompatibilities 
{

	protected Config config;
	protected Functions FXS;
	protected Components COMPS;
	protected Tests TESTS;
	protected FaultTracker tracker;
	protected Reporter reporter;		// overall-reporter
	protected Test testHarness;
	
	public TestMathIncompatibilities(){ super(); initialize(); }
	
	public TestMathIncompatibilities( 
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
		reporter= new Reporter("Test Mathematical Incompatibilities");
		tracker = new FaultTracker("Test Mathematical Incompatibilities");
		FXS 	= new Functions();
		COMPS	= new Components();
		TESTS	= new Tests();
	}


	protected TA<String> symmetry = new TA(Array.array(
			"histogram_measure_avg_difference",
			"histogram_measure_bhattacharyya",
			"histogram_measure_chebyshev",
			"histogram_measure_city_block",
			"histogram_measure_cosine",
			"histogram_measure_czekanowski",
			"histogram_measure_czekanowski_dCze",
			"histogram_measure_dice",
			"histogram_measure_dice_dDice1",
			"histogram_measure_euclidean",
			"histogram_measure_euclidean_alt1",
			"histogram_measure_euclidean_alt2",
			"histogram_measure_fidelity",
			"histogram_measure_gower",
			"histogram_measure_hellinger",
			"histogram_measure_inner_product",
			"histogram_measure_intersection_IS",
			"histogram_measure_intersection_dNonIS",
			"histogram_measure_jaccard",
			"histogram_measure_jaccard_dJac1",
			"histogram_measure_jensen_difference",
			"histogram_measure_kumar_hassebrook_pce",
			"histogram_measure_lorentzian",
			"histogram_measure_matusita",
			"histogram_measure_matusita_dM2",
			"histogram_measure_minkowski",
			"histogram_measure_motyka",
			"histogram_measure_motyka_dMot",
			"histogram_measure_ruzicka",
			"histogram_measure_soergel",
			"histogram_measure_sorensen",
			"histogram_measure_squared_chord",
			"histogram_measure_squared_chord_Ssqc1",
			"histogram_measure_squared_euclidean",
			"histogram_measure_tanimoto",
			"pixel_measure_ari",
			"pixel_measure_dice",
			"pixel_measure_jaccard",
			"pixel_measure_ri",
			"pixel_measure_tee",
			"pixel_measure_tet"
			)
			);
	protected TA<String> nonNegativity = new TA(Array.array(
			"histogram_measure_avg_difference",
			"histogram_measure_bhattacharyya",
			"histogram_measure_chebyshev",
			"histogram_measure_city_block",
			"histogram_measure_cosine",
			"histogram_measure_czekanowski",
			"histogram_measure_czekanowski_dCze",
			"histogram_measure_dice",
			"histogram_measure_dice_dDice1",
			"histogram_measure_euclidean",
			"histogram_measure_euclidean_alt1",
			"histogram_measure_euclidean_alt2",
			"histogram_measure_fidelity",
			"histogram_measure_gower",
			"histogram_measure_hellinger",
			"histogram_measure_inner_product",
			"histogram_measure_intersection_IS",
			"histogram_measure_intersection_dNonIS",
			"histogram_measure_jaccard",
			"histogram_measure_jaccard_dJac1",
			"histogram_measure_jensen_difference",
			"histogram_measure_kumar_hassebrook_pce",
			"histogram_measure_lorentzian",
			"histogram_measure_matusita",
			"histogram_measure_matusita_dM2",
			"histogram_measure_minkowski",
			"histogram_measure_motyka",
			"histogram_measure_motyka_dMot",
			"histogram_measure_ruzicka",
			"histogram_measure_soergel",
			"histogram_measure_sorensen",
			"histogram_measure_squared_chord",
			"histogram_measure_squared_euclidean",
			"histogram_measure_tanimoto",
			"pixel_measure_dice",
			"pixel_measure_jaccard",
			"pixel_measure_tee",
			"pixel_measure_tet"
			)
			);
	protected TA<String> identityOfIndiscernibles = new TA(Array.array(
			"histogram_measure_additive_symmetric_chiSquared",
			"histogram_measure_avg_difference",
			"histogram_measure_bhattacharyya",
			"histogram_measure_canberra",
			"histogram_measure_chebyshev",
			"histogram_measure_city_block",
			"histogram_measure_clark",
			"histogram_measure_cosine",
			"histogram_measure_czekanowski",
			"histogram_measure_czekanowski_dCze",
			"histogram_measure_dice",
			"histogram_measure_dice_dDice1",
			"histogram_measure_divergence",
			"histogram_measure_euclidean",
			"histogram_measure_euclidean_alt1",
			"histogram_measure_euclidean_alt2",
			"histogram_measure_fidelity",
			"histogram_measure_gower",
			"histogram_measure_harmonic_mean",
			"histogram_measure_hellinger",
			"histogram_measure_inner_product",
			"histogram_measure_intersection_IS",
			"histogram_measure_intersection_dNonIS",
			"histogram_measure_jaccard",
			"histogram_measure_jaccard_dJac1",
			"histogram_measure_jeffreys",
			"histogram_measure_jensen_difference",
			"histogram_measure_jensen_shannon",
			"histogram_measure_k_divergence",
			"histogram_measure_kulczynski",
			"histogram_measure_kulczynski_s",
			"histogram_measure_kullback_leibler",
			"histogram_measure_kumar_hassebrook_pce",
			"histogram_measure_kumar_johnson_difference",
			"histogram_measure_lorentzian",
			"histogram_measure_matusita",
			"histogram_measure_matusita_dM2",
			"histogram_measure_minkowski",
			"histogram_measure_motyka",
			"histogram_measure_motyka_dMot",
			"histogram_measure_neyman_chiSquared",
			"histogram_measure_pearson_chiSquared",
			"histogram_measure_probabilistic_symmetric_chiSquared",
			"histogram_measure_ruzicka",
			"histogram_measure_soergel",
			"histogram_measure_sorensen",
			"histogram_measure_squared_chiSquared",
			"histogram_measure_squared_chord",
			"histogram_measure_squared_chord_Ssqc1",
			"histogram_measure_squared_euclidean",
			"histogram_measure_taneja_difference",
			"histogram_measure_tanimoto",
			"histogram_measure_topsoe",
			"histogram_measure_wave_hedges"
			));
	protected TA<String> triangleInequality = new TA(Array.array(
			"histogram_measure_additive_symmetric_chiSquared",
			"histogram_measure_avg_difference",
			"histogram_measure_bhattacharyya",
			"histogram_measure_canberra",
			"histogram_measure_chebyshev",
			"histogram_measure_city_block",
			"histogram_measure_clark",
			"histogram_measure_czekanowski_dCze",
			"histogram_measure_dice_dDice1",
			"histogram_measure_divergence",
			"histogram_measure_euclidean",
			"histogram_measure_euclidean_alt1",
			"histogram_measure_euclidean_alt2",
			"histogram_measure_gower",
			"histogram_measure_harmonic_mean",
			"histogram_measure_hellinger",
			"histogram_measure_inner_product",
			"histogram_measure_intersection_dNonIS",					
			"histogram_measure_jaccard_dJac1",
			"histogram_measure_jeffreys",
			"histogram_measure_jensen_difference",
			"histogram_measure_jensen_shannon",
			"histogram_measure_k_divergence",
			"histogram_measure_kulczynski",
			"histogram_measure_kulczynski_s",
			"histogram_measure_kullback_leibler",
			"histogram_measure_kumar_johnson_difference",
			"histogram_measure_lorentzian",
			"histogram_measure_matusita",
			"histogram_measure_matusita_dM2",
			"histogram_measure_minkowski",
			"histogram_measure_motyka_dMot",
			"histogram_measure_neyman_chiSquared",
			"histogram_measure_pearson_chiSquared",
			"histogram_measure_probabilistic_symmetric_chiSquared",					
			"histogram_measure_soergel",
			"histogram_measure_sorensen",
			"histogram_measure_squared_chiSquared",
			"histogram_measure_squared_chord",
			"histogram_measure_squared_euclidean",
			"histogram_measure_taneja_difference",
			"histogram_measure_tanimoto",
			"histogram_measure_topsoe",
			"histogram_measure_wave_hedges",
			"pixel_measure_dice",
			"pixel_measure_jaccard",
			"pixel_measure_tee",
			"pixel_measure_tet"
			));
			
	protected TA<String> metricity = new TA(Array.array(
			"histogram_measure_avg_difference",
			"histogram_measure_bhattacharyya",
			"histogram_measure_chebyshev",
			"histogram_measure_city_block",
			"histogram_measure_czekanowski_dCze",
			"histogram_measure_dice_dDice1",
			"histogram_measure_euclidean",
			"histogram_measure_euclidean_alt1",
			"histogram_measure_euclidean_alt2",
			"histogram_measure_gower",
			"histogram_measure_hellinger",
			"histogram_measure_inner_product",
			"histogram_measure_intersection_dNonIS",					
			"histogram_measure_jaccard_dJac1",
			"histogram_measure_jensen_difference",
			"histogram_measure_lorentzian",
			"histogram_measure_matusita",
			"histogram_measure_matusita_dM2",
			"histogram_measure_minkowski",
			"histogram_measure_motyka_dMot",					
			"histogram_measure_soergel",
			"histogram_measure_sorensen",
			"histogram_measure_squared_chord",
			"histogram_measure_squared_euclidean",
			"histogram_measure_tanimoto"
			));

	public void testPixelMeasureOpProperties( String measureOpName, int totalIterations )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Pixel Measure Property Tests");
		
		String rBase = "testPixelMeasurePropertyTests" + FSEP + measureOpName + FSEP;
		String rString = "";
		Exception _e = null;
		
		RandomImageDataSource rds = new RandomImageDataSource(0,255);
		Arity2Input<ImageData,ImageData> ipair = (Arity2Input<ImageData,ImageData>) rds.getImagePair(2,2,1);

		Arity2Function<ImageData,ImageData,Double> fx = (Arity2Function<ImageData,ImageData,Double>) FXS.getByName(measureOpName);

		int N = totalIterations;	// total tests to perform before stopping			
		Boolean actual = true;
		Boolean expected = true;			
		Boolean _prop = true;	// true until proven false
		Boolean _check = true;
		
		for (int i=0; i < N; i++ ) 
		{
			try {
				tracker.track();
				_check = TESTS.testSymmetryMD( fx, ipair.i1(), ipair.i2() );
				if ( _check != _prop ) {
					break;
				}
				ipair = (Arity2Input<ImageData,ImageData>) rds.getImagePair(2,2,1);					
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

	// symmetry
		
		actual = _check;
		expected = symmetry.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "Symmetry" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report( rString );
		}
		else {
			rString = rBase + FSEP + "Symmetry" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report( rString );
		}

		
		_prop = _prop && _check;
					
		Double d = 0.0d;
		for (int i=0; i < N; i++ ) 
		{
			try {
				
				ipair = (Arity2Input<ImageData,ImageData>) rds.getImagePair(2,2,1);
				d = fx.evalAt( ipair.i1(), ipair.i2() );
				_check = TESTS.testNonNegativity(d);
				tracker.track();
				if ( _check != _prop ) break;
				ipair = (Arity2Input<ImageData,ImageData>) rds.getImagePair(2,2,1);
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

	// non-negativity	

		actual = _check;
		expected = nonNegativity.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "NonNegativity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report( rString );
		}
		else {
			rString = rBase + FSEP + "NonNegativity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report( rString );
		}

		_prop = _prop && _check;
		

		ipair.i1(  rds.getImage(2, 2, 1) );
		ipair.i2(  ipair.i1() );
		for (int i=0; i < N; i++ ) 
		{
			try {
				d = fx.evalAt( ipair.i1(), ipair.i2() );
				_check = TESTS.testIdentityOfIndiscernibles( ipair.i1(), ipair.i2(), d );
				tracker.track();
				if ( _check != _prop ) break;
				ipair.i1(  rds.getImage(2, 2, 1) );
				ipair.i2(  ipair.i1() );
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

	// identity of indiscernibles
		
		actual = _check;
		expected = identityOfIndiscernibles.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "IdentityOfIndiscernibles" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report(rString);
		}
		else {
			rString = rBase + FSEP + "IdentityOfIndiscernibles" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report(rString);
		}

		_prop = _prop && _check;
		

		Arity3Input<ImageData,ImageData,ImageData> i3h = (Arity3Input<ImageData,ImageData,ImageData>)rds.getImage3(2,2,1);
		Double xy = 0.0d;
		Double yz = 0.0d;
		Double xz = 0.0d;
		for (int i=0; i < N; i++ ) 
		{
			try {
				xy = fx.evalAt(i3h.i1(), i3h.i2() );
				yz= fx.evalAt(i3h.i2(), i3h.i3() );
				xz = fx.evalAt(i3h.i1(), i3h.i3() );
				tracker.track();
				if ( xz == null || yz == null || xz == null ) continue;						
				_check = TESTS.testTriangleInequality(xz, xy, yz );
				if ( _check != _prop ) break;
				i3h = (Arity3Input<ImageData,ImageData,ImageData>)rds.getImage3(2,2,1);
				tracker.track();
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

	// triangle inequality
		
		actual = _check;
		expected = triangleInequality.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "TriangleInequality" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report(rString);
		}
		else {
			rString = rBase + FSEP + "TriangleInequality" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report(rString);
		}

		_prop = _prop && _check;
	
	// metricity

		actual = _check;
		expected = metricity.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "Metricity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report(rString);
		}
		else {
			rString = rBase + FSEP + "Metricity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report(rString);
		}
		
		out("Tracker: " + tracker.report() );
		tracker.saveGraph(config.fullDir()  + measureOpName + "_PixelPropertyTests.png");
		String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_PixelPropertyTests.png");
		reporter.report(REPORT);
	}		
	

			
	public void testHistogramMeasureOpProperties( String measureOpName, int totalIterations )
	{
		reporter = testHarness.reporter();
		tracker = new FaultTracker("Histogram Measure Property Tests");
		
		String rBase = "testHistogramMeasureOpProperties" + FSEP + measureOpName + FSEP;
		String rString = "";
		Exception _e = null;
		
		RandomImageDataSource rds = new RandomImageDataSource(0,255);
		Arity2Input<Double[],Double[]> ipair = (Arity2Input<Double[],Double[]>) rds.getNormalizedHistogramPair(2,2,1,256);

		Arity2Function<Double[],Double[],Double> fx = (Arity2Function<Double[],Double[],Double>) FXS.getByName(measureOpName);

		int N = totalIterations;	// total tests to perform before stopping
		Boolean actual = true;
		Boolean expected = true;
		
		Boolean _prop = true;	// true until proven false
		Boolean _check = true;
		for (int i=0; i < N; i++ ) 
		{
			try {
				_check = TESTS.testSymmetryD1D( fx, ipair.i1(), ipair.i2() );
				tracker.track();
				if ( _check != _prop ) {
					break;
				}
				ipair = (Arity2Input<Double[],Double[]>) rds.getNormalizedHistogramPair(2,2,1,256);						
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

		
	// symmetry
		
		actual = _check;
		expected = symmetry.exists( measureOpName );
		if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
			rString = rBase + FSEP + "Symmetry" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
			reporter.report(rString);
		}
		else {
			rString = rBase + FSEP + "Symmetry" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
			reporter.report(rString);
		}
		
		_prop = _prop && _check;
		
		Double d = 0.0d;
		
		 _check = true;
		for (int i=0; i < N; i++ ) 
		{
			try {
				ipair = (Arity2Input<Double[],Double[]>) rds.getNormalizedHistogramPair(2,2,1,256);
				d = fx.evalAt( ipair.i1(), ipair.i2() );
				_check = TESTS.testNonNegativity(d);
				tracker.track();
				if ( _check != _prop ) break;
				ipair = (Arity2Input<Double[],Double[]>) rds.getNormalizedHistogramPair(2,2,1,256);
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

		// non-negativity
		
			actual = _check;
			expected = nonNegativity.exists( measureOpName );
			if ( !(expected==false && actual==true) &&  (actual != expected)  ) { // want only to declare those things properties that are always consistent.
				rString = rBase + FSEP + "NonNegativity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
				reporter.report( rString );
			}
			else {
				rString = rBase + FSEP + "NonNegativity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
				reporter.report( rString );
			}
			
		_prop = _prop && _check;


		 _check = true;
		ipair.i1(  rds.getNormalizedHistogram(2, 2, 1, 256) );
		ipair.i2(  ipair.i1() );
		for (int i=0; i < N; i++ ) 
		{
			try {
				d = fx.evalAt( ipair.i1(), ipair.i2() );
				_check = TESTS.testIdentityOfIndiscernibles( ipair.i1(), ipair.i2(), d );
				tracker.track();
				if ( _check != _prop ) break;
				ipair.i1(  rds.getNormalizedHistogram(2, 2, 1, 256) );
				ipair.i2(  ipair.i1() );
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}

	// identity of indiscernibles
			actual = _check;
			expected = identityOfIndiscernibles.exists( measureOpName );
			if ( !(expected==false && actual==true) &&(actual != expected)  ) { // want only to declare those things properties that are always consistent.
				rString = rBase + FSEP + "IdentityOfIndiscernibles" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
				reporter.report( rString );
			}
			else {
				rString = rBase + FSEP + "IdentityOfIndiscernibles" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
				reporter.report( rString );
			}
		
		_prop = _prop && _check;
		

		
		 _check = true;
		Arity3Input<Double[],Double[],Double[]> i3h = (Arity3Input<Double[],Double[],Double[]>)rds.getNormalizedHistogram3(2,2,1,256);
		Double xy = 0.0d;
		Double yz = 0.0d;
		Double xz = 0.0d;
		for (int i=0; i < N; i++ ) 
		{
			try {
				xy = fx.evalAt(i3h.i1(), i3h.i2() );
				yz= fx.evalAt(i3h.i2(), i3h.i3() );
				xz = fx.evalAt(i3h.i1(), i3h.i3() );
				tracker.track();
				if ( xz == null || yz == null || xz == null ) continue;						
				_check = TESTS.testTriangleInequality(xz, xy, yz );
				if ( _check != _prop ) break;
				i3h = (Arity3Input<Double[],Double[],Double[]>)rds.getNormalizedHistogram3(2,2,1,256);
			}
			catch( Exception e ) {
				tracker.track(e);
			}
		}
		
	// triangle inequality	

			actual = _check;
			expected = triangleInequality.exists( measureOpName );
			if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
				rString = rBase + FSEP + "TriangleInequality" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
				reporter.report( rString );
			}
			else {
				rString = rBase + FSEP + "TriangleInequality" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
				reporter.report( rString );
			}
		
		_prop = _prop && _check;
	
		// metricity
		
			actual = _check;
			expected = metricity.exists( measureOpName );
			if ( !(expected==false && actual==true) && (actual != expected)  ) { // want only to declare those things properties that are always consistent.
				rString = rBase + FSEP + "Metricity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "FAIL";
				reporter.report( rString );
			}
			else {
				rString = rBase + FSEP + "Metricity" + FSEP + "(actual,expected)=("+actual+","+expected+")" + FSEP + "PASS";
				reporter.report( rString );
			}

			out("Tracker: " + tracker.report() );
			tracker.saveGraph(config.fullDir()  + measureOpName + "_HistogramPropertyTests.png");
			String REPORT = rBase + FSEP + tracker.report() + FSEP + image( measureOpName + "_HistogramPropertyTests.png");
			reporter.report(REPORT);
	}

		
}
