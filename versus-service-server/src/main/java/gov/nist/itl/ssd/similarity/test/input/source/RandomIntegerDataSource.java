package gov.nist.itl.ssd.similarity.test.input.source;

import java.util.Random;

public class RandomIntegerDataSource extends RandomDataSource<Integer>
{
	public RandomIntegerDataSource(){super();init();}
	public RandomIntegerDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
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
		Integer nextNum = rng.nextInt(); 
		//Integer value = start + (nextNum * (end - start + 1));
		Integer value = nextNum;
		return value;
	}
	
	public Integer get(int n) {
		Integer nextNum = rng.nextInt(n); 
		//Integer value = start + (nextNum * (end - start + 1));
		Integer value = nextNum;
		return value;
	}
	
	
	@Override
	public Integer[] getN(int n) {
		Integer d[] = new Integer[n];
		for (int i=0; i < n; i++) d[i] = get();
		return d;
	}
	
	public static void main( String[] args ) 
	{
		RandomIntegerDataSource rds = new RandomIntegerDataSource(0, 255);
		System.out.println( rds.get() );
		int n= 10;
		Integer[] d = rds.getN(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
