package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity2Function<A,B,C> extends Function<C>
{
	public Arity2Function(){super();}
	public Arity2Function(String type){super(type); this.type = type; }
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public TA<Arity3Input> unitTests(){ return (TA<Arity3Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity3Input>)v;}
	public void addUnitTest( Arity3Input<A,B,C> v ){ unitTests.add( (Arity3Input<A,B,C>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		
	
	public TA<Arity3Input> failureModeTests(){ return (TA<Arity3Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity3Input>)v;}
	public void addFailureModeTest( Arity3Input<A,B,C> v ){ failureModeTests.add( (Arity3Input<A,B,C>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	public C evalAt( A a, B b ) {
		return null;
	}
	
	public C evalDerivativeAt( A a, B b ) {
		return null;
	}

	public Boolean expectedValueAt( A a, B b, C c ) {
		if ( evalAt(a,b).equals(c) ) {		
			return true;
		}		
		return false;
	}
	
}