package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity4Function<A,B,C,D,E> extends Function<E>
{
	public Arity4Function(){super();}
	public Arity4Function(String type){super(type);}
	
	public TA<Arity5Input> unitTests(){ return (TA<Arity5Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity5Input>)v;}
	public void addUnitTest( Arity5Input<A,B,C,D,E> v ){ unitTests.add( (Arity5Input<A,B,C,D,E>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }	

	public TA<Arity5Input> failureModeTests(){ return (TA<Arity5Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity5Input>)v;}
	public void addFailureModeTest( Arity5Input<A,B,C,D,E> v ){ failureModeTests.add( (Arity5Input<A,B,C,D,E>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	public E evalAt( A a, B b, C c, D d ) {
		return null;
	}
	
	public E evalDerivativeAt( A a, B b, C c, D d ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e ) {
		if ( evalAt(a,b,c,d).equals(e) )
			return true;
		return false;
	}		
}