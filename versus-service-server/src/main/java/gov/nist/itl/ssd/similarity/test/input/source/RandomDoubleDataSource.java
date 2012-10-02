package gov.nist.itl.ssd.similarity.test.input.source;

import gov.nist.itl.ssd.similarity.test.*;
import gov.nist.itl.ssd.similarity.test.input.source.RandomDataSource;
import java.util.Random;

public class RandomDoubleDataSource extends RandomDataSource<Double>
{
	public RandomDoubleDataSource(){super();init();}
	public RandomDoubleDataSource(Double start, Double end){super();init(); start(start); end(end);}
	public void init(){
		if ( usingSeed() )
			rng( new Random( seed() ) );
		else
			rng( new Random() );
		start( 0.0d );
		  end( 1.0d );
	}
	
	@Override
	public Double get() {
		Double nextNum = rng.nextDouble(); 
		//Double value = start + (nextNum * (end - start + 1));
		Double value = start + (nextNum * (end - start ));
		return value;
	}
	
	@Override
	public Double[] getN(int n) {
		Double d[] = new Double[n];
		for (int i=0; i < n; i++) d[i] = get();
		return d;
	}
	
	public static void main( String[] args ) 
	{
		RandomDoubleDataSource rds = new RandomDoubleDataSource(0.0d, 255d);
		System.out.println( rds.get() );
		int n= 10;
		Double[] d = rds.getN(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
