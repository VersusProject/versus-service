package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity7Input<A,B,C,D,E,F,G> extends Input<F>
{
	protected A i1;
	protected B i2;
	protected C i3;
	protected D i4;
	protected E i5;
	protected F i6;
	protected G i7;
	
	public Arity7Input(){super();}
	public Arity7Input(String type){super(type);}
	
	public void i1(A v){this.i1=v;}
	public A i1(){return this.i1;}
	public void i2(B v){this.i2=v;}
	public B i2(){return this.i2;}
	public void i3(C v){this.i3=v;}
	public C i3(){return this.i3;}
	public void i4(D v){this.i4=v;}
	public D i4(){return this.i4;}
	public void i5(E v){this.i5=v;}
	public E i5(){return this.i5;}
	public void i6(F v){this.i6=v;}
	public F i6(){return this.i6;}
	public void i7(G v){this.i7=v;}
	public G i7(){return this.i7;}		
}
