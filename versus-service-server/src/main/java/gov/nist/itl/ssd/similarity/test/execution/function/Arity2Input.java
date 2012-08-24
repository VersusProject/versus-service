package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity2Input<A,B> extends Input<B>
{
	protected A i1;
	protected B i2;
	
	public Arity2Input(){super();}
	public Arity2Input(A a, B b){super(); i1(a); i2(b);}
	public Arity2Input(String type){super(type);}
	
	public void i1(A v){this.i1=v;}
	public A i1(){return this.i1;}
	public void i2(B v){this.i2=v;}
	public B i2(){return this.i2;}
	
}
