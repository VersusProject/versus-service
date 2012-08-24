package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity3Input<A,B,C> extends Input<C>
{
	protected A i1;
	protected B i2;
	protected C i3;
	
	public Arity3Input(){super();}
	public Arity3Input(A a, B b, C c){super(); i1(a); i2(b); i3(c); }
	public Arity3Input(String type){super(type);}
	
	public void i1(A v){this.i1=v;}
	public A i1(){return this.i1;}
	public void i2(B v){this.i2=v;}
	public B i2(){return this.i2;}
	public void i3(C v){this.i3=v;}
	public C i3(){return this.i3;}
}
