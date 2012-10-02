package gov.nist.itl.ssd.similarity.test.input.source;

import java.util.Random;


public class RandomDataSource<A> extends DataSource<A>{

	protected Random rng;
	protected A start;
	protected A end;
	protected Long seed;
	
	public boolean usingSeed = false;
	
	public RandomDataSource(){super(); init(); }
	
	public RandomDataSource(Long seed){
		super();
		seed(seed);
		usingSeed(true);
		init();
		}
	
	public void init(){
		rng = new Random();
		start = null;
		end = null;
		seed= null;
	}
		
	public void start(A v){this.start=v;}
	public A start(){ return start;}
	public void end( A v) { this.end=v; }
	public A end(){ return end; }
	public void seed( Long v ) { this.seed = v; }
	public Long seed(){ return seed; }
	public boolean usingSeed(){ return usingSeed; }
	public void usingSeed( boolean v ) { this.usingSeed=v; }
	
	public void rng( Random v ){ this.rng = v; }
	public Random rng(){ return rng; }
	
	public A get() {
		return null;
	}
	
	public A[] getN(int n) {
		return null;
	}	
}

