package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity1Input<A> extends Input<A>
{
	protected A i1;
	
	public Arity1Input(){super();}
	public Arity1Input(A v){super(); i1(v);}
	public Arity1Input(String type){super(type);}
	
	public void i1(A v){this.i1=v;}
	public A i1(){return this.i1;}	
}
