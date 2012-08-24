package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity6Function<A,B,C,D,E,F,G> extends Function<G>
{
	public Arity6Function(){super();}
	public Arity6Function(String type){super(type);}
	
	public TA<Arity7Input> unitTests(){ return (TA<Arity7Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity7Input>)v;}
	public void addUnitTest( Arity7Input<A,B,C,D,E,F,G> v ){ unitTests.add( (Arity7Input<A,B,C,D,E,F,G>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		

	public TA<Arity7Input> failureModeTests(){ return (TA<Arity7Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity7Input>)v;}
	public void addFailureModeTest( Arity7Input<A,B,C,D,E,F,G> v ){ failureModeTests.add( (Arity7Input<A,B,C,D,E,F,G>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	public G evalAt( A a, B b, C c, D d, E e, F f ) {
		return null;
	}
	
	public G evalDerivativeAt( A a, B b, C c, D d, E e, F f ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e, F f, G g ) {
		if ( evalAt(a,b,c,d,e,f).equals(g) )
			return true;
		return false;
	}	
	
}