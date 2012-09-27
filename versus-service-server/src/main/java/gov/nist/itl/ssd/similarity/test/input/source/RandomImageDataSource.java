package gov.nist.itl.ssd.similarity.test.input.source;

import gov.nist.itl.versus.similarity.comparisons.ImageData;
import gov.nist.itl.versus.similarity.comparisons.MathOpsE;

import java.util.Random;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Input;


public class RandomImageDataSource  extends RandomDataSource<Integer>
{
	protected MathOpsE mopsE = new MathOpsE();
	
	public RandomImageDataSource(){super();init();}
	public RandomImageDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
	public void init(){
		if ( usingSeed() )
			rng( new Random( seed() ) );
		else
			rng( new Random() );
		start( 0 );
		  end( 1 );
	}
	
	@Override
	public Integer get() {
		Integer nextNum = rng.nextInt(256); 
		Integer value = nextNum;
		return value;
	}
	
	public Double getAsDouble() {
		return get().doubleValue();
	}
		
	@Override
	public Integer[] getN(int n) {
		Integer d[] = new Integer[n];
		for (int i=0; i < n; i++) d[i] = get();
		return d;
	}
	
	public Double[] getNAsDouble(int n) {
		Double d[] = new Double[n];
		for (int i=0; i < n; i++) d[i] = getAsDouble();
		return d;
	}	
	
	public ImageData getImage( int rows, int cols, int bands )
	{
		double[][][] pixels = new double[rows][cols][bands];
		for (int i=0; i < rows; i++)
			for (int j=0; j < cols; j++)
				for (int k=0; k < bands; k++)
					pixels[i][j][k] = getAsDouble();
		ImageData img = new ImageData(pixels);
		return img;
	}
	
	
	public TA<ImageData> getImages(int N, int rows, int cols, int bands) {
		TA<ImageData> t = new TA();
		for (int i=0; i < N; i++)
			t.add( getImage(rows,cols,bands) );
		return t;
	}
	
	public Arity2Input<ImageData,ImageData> getImagePair(int rows, int cols, int bands) {
		TA<ImageData> imgs = getImages(2,rows,cols,bands);
		Arity2Input<ImageData,ImageData> pr = new Arity2Input<ImageData,ImageData>();
		pr.i1( imgs.get(0) );
		pr.i2( imgs.get(1) );
		return pr;
	}
	
	public Arity3Input<ImageData,ImageData,ImageData> getImage3(int rows, int cols, int bands) {
		TA<ImageData> imgs = getImages(3,rows,cols,bands);
		Arity3Input<ImageData,ImageData,ImageData> input3 = new Arity3Input<ImageData,ImageData,ImageData>();
		input3.i1( imgs.get(0) );
		input3.i2( imgs.get(1) );
		input3.i3( imgs.get(2) );
		return input3;
	}
	
	public Double[] getRawHistogram( ImageData img )
	{
		double[][][] pixels = img.getValues();
		TA<Double> diffVals = new TA();
		int len1 = pixels.length;
		int len2 = pixels[0].length;
		int len3 = pixels[0][0].length;
		for (int i=0; i < len1; i++)
			for (int j=0; j < len2; j++)
				for (int k=0; k < len3; k++)
					if ( !diffVals.exists( pixels[i][j][k])) 
						diffVals.add( pixels[i][j][k] );
		Double max = 0.0d;
		Double min = 0.0d;
		int len = diffVals.length();
		for (int i=0; i < len; i++) 
			if ( max < diffVals.get(i) ) max = diffVals.get(i);	
		for (int i=0; i < len; i++) 
			if ( min > diffVals.get(i) ) min = diffVals.get(i);
		Double bins = max + 1;	// range
		Double[] hist = new Double[bins.intValue()];
		Double val = 0.0d;
		Double freq = 0.0d;
		
		len = hist.length;
		for (int i=0; i < len; i++) hist[i] = 0.0d;
		
		for (int i=0; i < len1; i++)
			for (int j=0; j < len2; j++)
				for (int k=0; k < len3; k++) {		
					
						val = pixels[i][j][k] ;
						hist[val.intValue()]++;
				}		
	    return hist;
	}
	
	public Double[] getRawHistogram( ImageData img, int bins )
	{
		double[][][] pixels = img.getValues();
		TA<Double> diffVals = new TA();
		int len1 = pixels.length;
		int len2 = pixels[0].length;
		int len3 = pixels[0][0].length;
		for (int i=0; i < len1; i++)
			for (int j=0; j < len2; j++)
				for (int k=0; k < len3; k++)
					if ( !diffVals.exists( pixels[i][j][k])) 
						diffVals.add( pixels[i][j][k] );
		Double[] hist = new Double[bins];
		Double val = 0.0d;
		Double freq = 0.0d;
		
		int len = hist.length;
		for (int i=0; i < len; i++) hist[i] = 0.0d;
		
		for (int i=0; i < len1; i++)
			for (int j=0; j < len2; j++)
				for (int k=0; k < len3; k++) {							
						val = pixels[i][j][k] ;
						hist[val.intValue()]++;
				}		
	    return hist;
	}	
	
	public Double[] getNormalizedHistogram( Double[] rawHistogram )
	{
		Double[] normalizedHistogram = null;
		try {
			normalizedHistogram = mopsE.normalizeHistogram( rawHistogram );
		}
		catch( Exception e ) {
			System.out.println("Error: " + e.getMessage() );
		}
		return normalizedHistogram;
	}
	
	public boolean isNormalized( Double[] nh ) {
		Double sum = 0.0d;
		int len = nh.length;
		for (int i=0; i < len; i++) sum += nh[i];
		if ( sum.equals( 1.0d ) )
			return true;
		return false;
	}
	
	public Double[] getNormalizedHistogram( int rows, int cols, int bands ){
		return getNormalizedHistogram(getRawHistogram(getImage(rows,cols,bands)));
	}
	
	public Double[] getNormalizedHistogram( int rows, int cols, int bands, int bins ){
		return getNormalizedHistogram(getRawHistogram(getImage(rows,cols,bands),bins));
	}
	
	public TA<Double[]> getNormalizedHistogramN( int N, int rows, int cols, int bands ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getNormalizedHistogram(rows,cols,bands) );
		return hgs;
	} 
	
	public TA<Double[]> getNormalizedHistogramN( int N, int rows, int cols, int bands, int bins ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getNormalizedHistogram(rows,cols,bands,bins) );
		return hgs;
	} 	
	
	public Arity2Input<Double[],Double[]> getNormalizedHistogramPair(int rows, int cols, int bands) {
		TA<Double[]> nH = getNormalizedHistogramN(2,rows,cols,bands);
		Arity2Input<Double[],Double[]> pr = new Arity2Input<Double[],Double[]>();
		pr.i1( nH.get(0) );
		pr.i2( nH.get(1) );
		return pr;
	}
	
	public Arity2Input<Double[],Double[]> getNormalizedHistogramPair(int rows, int cols, int bands, int bins) {
		TA<Double[]> nH = getNormalizedHistogramN(2,rows,cols,bands, bins);
		Arity2Input<Double[],Double[]> pr = new Arity2Input<Double[],Double[]>();
		pr.i1( nH.get(0) );
		pr.i2( nH.get(1) );
		//System.out.println("DEBUG: getNormalizedHistogramPair. length check (len1,len2)=(" + pr.i1().length + "," + pr.i2().length + ")" );
		return pr;
	}
	
	
	public Arity3Input<Double[],Double[],Double[]> getNormalizedHistogram3(int rows, int cols, int bands) {
		TA<Double[]> nH = getNormalizedHistogramN(3,rows,cols,bands);
		Arity3Input<Double[],Double[],Double[]> in = new Arity3Input<Double[],Double[],Double[]>();
		in.i1( nH.get(0) );
		in.i2( nH.get(1) );
		in.i3( nH.get(2) );
		return in;
	}
	
	public Arity3Input<Double[],Double[],Double[]> getNormalizedHistogram3(int rows, int cols, int bands, int bins) {
		TA<Double[]> nH = getNormalizedHistogramN(3,rows,cols,bands, bins);
		Arity3Input<Double[],Double[],Double[]> in = new Arity3Input<Double[],Double[],Double[]>();
		in.i1( nH.get(0) );
		in.i2( nH.get(1) );
		in.i3( nH.get(2) );
		return in;
	}
	
	public TA<Double[]> getRawHistogramN( int N, ImageData img ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getRawHistogram(img) );
		return hgs;
	} 
	
	public TA<Double[]> getRawHistogramN( int N, ImageData img, int bins ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getRawHistogram(img, bins) );
		return hgs;
	} 
	
	public static void main( String[] args ) 
	{
		/*
		RandomImageDataSource rds = new RandomImageDataSource(0, 10);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
		*/
		
		//////////////////////
		RandomImageDataSource rds = new RandomImageDataSource(0,2);
		Arity2Input<Double[],Double[]> ipair = (Arity2Input<Double[],Double[]>) rds.getNormalizedHistogramPair(2,2,1);
		Double[] a = ipair.i1();
		Double[] b = ipair.i2();
		int len1 = a.length;
		int len2 = b.length;
		System.out.println("len1,len2)=(" + len1 + "," + len2 + ")" );
		int len = len1;
		//for (int i=0; i < len; i++ )
		//	System.out.println("(a,b)=(" + a[i] + "," + b[i] + ")" );
		
		//////////////////////
		ImageData img = rds.getImage(2,2,1);
		System.out.println("img (rows,cols,bands)= " + img.getNumRows() + "," + img.getNumCols() + "," + img.getNumBands());
		//System.out.println("image [" + img + "]" );
	
		
		////////////////////////
		System.out.println("get raw histogram: [") ;
		Double[] rh = rds.getRawHistogram(img);
		len = rh.length;
		System.out.println("rh len = " + len );
	//	for (int i=0; i < len; i++)
	//		System.out.println("" + rh[i] );
		
		
	}
}
