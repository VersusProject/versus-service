package gov.nist.itl.ssd.similarity.test.input.source;

import gov.nist.itl.ssd.similarity.test.*;

public class DataSource<A> 
{
	
	protected A a;
	
	public DataSource(){super();}
	
	public void a( A a ){ this.a=a;}
	public A a(){ return a; }
	
}
