package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity4Input<A,B,C,D> extends Input<D>
{
	protected A i1;
	protected B i2;
	protected C i3;
	protected D i4;
	
	public Arity4Input(){super();}
	public Arity4Input(A a, B b, C c, D d){super(); i1(a); i2(b); i3(c); i4(d);}
	public Arity4Input(String type){super(type);}
	
	public void i1(A v){this.i1=v;}
	public A i1(){return this.i1;}
	public void i2(B v){this.i2=v;}
	public B i2(){return this.i2;}
	public void i3(C v){this.i3=v;}
	public C i3(){return this.i3;}
	public void i4(D v){this.i4=v;}
	public D i4(){return this.i4;}
	
}
