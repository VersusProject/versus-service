package gov.nist.itl.ssd.similarity.test.input.source;

import gov.nist.itl.versus.similarity.comparisons.ImageData;
import gov.nist.itl.versus.similarity.comparisons.MathOpsE;

import java.util.Random;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity2Input;
import gov.nist.itl.ssd.similarity.test.execution.function.Arity3Input;

public class RandomBinaryImageDataSource   extends RandomDataSource<Integer>
{
	protected MathOpsE mopsE = new MathOpsE();
	
	public RandomBinaryImageDataSource(){super();init();}
	public RandomBinaryImageDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
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
		Integer nextNum = rng.nextInt(2); 
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
	
	public Double[] getNormalizedHistogram( int rows, int cols, int bands ){
		return getNormalizedHistogram(getRawHistogram(getImage(rows,cols,bands)));
	}
	
	public TA<Double[]> getNormalizedHistogram( int N, int rows, int cols, int bands ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getNormalizedHistogram(rows,cols,bands) );
		return hgs;
	} 
	
	public Arity2Input<Double[],Double[]> getNormalizedHistogramPair(int rows, int cols, int bands) {
		TA<Double[]> nH = getNormalizedHistogram(2,rows,cols,bands);
		Arity2Input<Double[],Double[]> pr = new Arity2Input<Double[],Double[]>();
		pr.i1( nH.get(0) );
		pr.i2( nH.get(1) );
		return pr;
	}

	public Arity3Input<Double[],Double[],Double[]> getNormalizedHistogram3(int rows, int cols, int bands) {
		TA<Double[]> nH = getNormalizedHistogram(3,rows,cols,bands);
		Arity3Input<Double[],Double[],Double[]> in = new Arity3Input<Double[],Double[],Double[]>();
		in.i1( nH.get(0) );
		in.i2( nH.get(1) );
		in.i3( nH.get(2) );
		return in;
	}
	
	public TA<Double[]> getRawHistogram( int N, ImageData img ){
		TA<Double[]> hgs = new TA();
		for (int i=0; i < N; i++)
			hgs.add( getRawHistogram(img) );
		return hgs;
	} 
	
	
	
	public static void main( String[] args ) 
	{
		RandomBinaryImageDataSource rds = new RandomBinaryImageDataSource(0, 1);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
