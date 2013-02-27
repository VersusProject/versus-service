package gov.nist.itl.ssd.similarity.test.input.source;

import java.util.Random;

public class RandomPixelDataSouce extends RandomDataSource<Integer>
{
	public RandomPixelDataSouce(){super();init();}
	public RandomPixelDataSouce(Integer start, Integer end){super();init(); start(start); end(end);}
	
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
		//Integer value = start + (nextNum * (end - start + 1));
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
		RandomPixelDataSouce rds = new RandomPixelDataSouce(0, 255);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
