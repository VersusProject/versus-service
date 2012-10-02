package gov.nist.itl.ssd.similarity.test.input.source;

import gov.nist.itl.versus.similarity.comparisons.ImageData;
import gov.nist.itl.versus.similarity.comparisons.MathOpsE;
import gov.nist.itl.ssd.similarity.test.TA;

public class SystematicBinaryImageDataSource  extends DataSource<Integer>
{
	protected MathOpsE mopsE = new MathOpsE();
	protected Integer cur = 0;
	protected Integer start = 0;
	protected Integer end = 0;
	
	public SystematicBinaryImageDataSource(){super();init();}
	public SystematicBinaryImageDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
	public void start(Integer v){this.start=v; cur(start); }
	public Integer start(){ return start;}
	public void end(Integer v){this.end=v;}
	public Integer end(){return end;}
	public void cur(Integer v){this.cur=v;}
	public Integer cur(){return cur;}
	
	public void init() {
		start( 0 );
		  end( 1 );
	}
	
	public Integer ng()
	{
		Integer g = cur;
		if ( g <= end() ) 
			cur++;
		else {
			cur = 0;
			g = cur;
		}
		return g;
	}

	public Integer get() {
		Integer nextNum = ng(); 
		//Integer value = start + (nextNum * (end - start + 1));
		Integer value = nextNum;
		return value;
	}
	
	public Double getAsDouble() {
		return get().doubleValue();
	}
		
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
	
	public static void main( String[] args ) 
	{
		SystematicBinaryImageDataSource rds = new SystematicBinaryImageDataSource(0, 1);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
