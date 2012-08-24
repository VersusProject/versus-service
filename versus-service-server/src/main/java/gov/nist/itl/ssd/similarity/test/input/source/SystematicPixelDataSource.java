package gov.nist.itl.ssd.similarity.test.input.source;

import java.util.Random;

public class SystematicPixelDataSource extends DataSource<Integer>
{
	protected Integer cur = 0;
	protected Integer start = 0;
	protected Integer end = 0;
	
	public SystematicPixelDataSource(){super();init();}
	public SystematicPixelDataSource(Integer start, Integer end){super();init(); start(start); end(end);}
	
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
	
	public static void main( String[] args ) 
	{
		SystematicPixelDataSource rds = new SystematicPixelDataSource(0, 255);
		System.out.println( rds.getAsDouble() );
		int n= 10;
		Double[] d = rds.getNAsDouble(n);
		for (int i=0; i<n; i++) System.out.println(""+ d[i]);
	}
}
