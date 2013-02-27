package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class Arity1Function<A,B> extends Function<B>
{
	public Arity1Function(){
		super();
	}
	public Arity1Function(String type){
		super(type); 
		this.type = type;
	}
	
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	//public void handleException( Exception e ) { super.handleException(e); }

	public TA<Arity2Input> unitTests(){ return (TA<Arity2Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity2Input>)v;}
	public void addUnitTest( Arity2Input<A,B> v ){ unitTests.add( (Arity2Input<A,B>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }	

	public TA<Arity2Input> failureModeTests(){ return (TA<Arity2Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity2Input>)v;}
	public void addFailureModeTest( Arity2Input<A,B> v ){ failureModeTests.add( (Arity2Input<A,B>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }	
	
	public B evalAt( A a ) {
		return null;
	}
	
	public B evalDerivativeAt( A a ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b ) {
		if ( evalAt(a).equals(b) )
			return true;
		return false;
	}
	
	public static void main( String[] args ) {
		Arity1Function<Double,Double> f = new Arity1Function<Double,Double>();
		
	}
	 
}
