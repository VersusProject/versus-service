package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity3Function<A,B,C,D> extends Function<D>
{
	public Arity3Function(){super();}
	public Arity3Function(String type){super(type);this.type = type;}
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public TA<Arity4Input> unitTests(){ return (TA<Arity4Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity4Input>)v;}
	public void addUnitTest( Arity4Input<A,B,C,D> v ){ unitTests.add( (Arity4Input<A,B,C,D>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		

	public TA<Arity4Input> failureModeTests(){ return (TA<Arity4Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity4Input>)v;}
	public void addFailureModeTest( Arity4Input<A,B,C,D> v ){ failureModeTests.add( (Arity4Input<A,B,C,D>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }			

	
	public D evalAt( A a, B b, C c ) {
		return null;
	}
	
	public D evalDerivativeAt( A a, B b, C c ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d ) {
		if ( evalAt(a,b,c).equals(d) )
			return true;
		return false;
	}
}