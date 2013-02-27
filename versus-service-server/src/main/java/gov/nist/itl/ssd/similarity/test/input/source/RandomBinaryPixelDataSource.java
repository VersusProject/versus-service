package gov.nist.itl.ssd.similarity.test.input.source;

import java.util.Random;

public class RandomBinaryPixelDataSource extends RandomDataSource<Integer>
{
	public RandomBinaryPixelDataSource(){super();init();}
	public RandomBinaryPixelDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
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
	
	public static void main( String[] args ) 
	{
		RandomBinaryPixelDataSource rds = new RandomBinaryPixelDataSource(0, 1);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
