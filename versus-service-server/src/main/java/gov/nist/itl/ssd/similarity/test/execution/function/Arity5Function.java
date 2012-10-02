package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity5Function<A,B,C,D,E,F> extends Function<F>
{
	public Arity5Function(){super();}
	public Arity5Function(String type){super(type);}
	
	public TA<Arity6Input> unitTests(){ return (TA<Arity6Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity6Input>)v;}
	public void addUnitTest( Arity6Input<A,B,C,D,E,F> v ){ unitTests.add( (Arity6Input<A,B,C,D,E,F>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }
	
	public TA<Arity6Input> failureModeTests(){ return (TA<Arity6Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity6Input>)v;}
	public void addFailureModeTest( Arity6Input<A,B,C,D,E,F> v ){ failureModeTests.add( (Arity6Input<A,B,C,D,E,F>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	
	
	public F evalAt( A a, B b, C c, D d, E e ) {
		return null;
	}
	
	public F evalDerivativeAt( A a, B b, C c, D d, E e ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e, F f ) {
		if ( evalAt(a,b,c,d,e).equals(f) )
			return true;
		return false;
	}		
}