package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class TestArity2Function<A,B,C> extends Function<C>
{
	protected Arity2Function<A,B,C> fut = new Arity2Function<A,B,C>(); // function under test

	protected Boolean testResult;
	
	public TestArity2Function(){super();}
	public TestArity2Function(String type){super(type);this.type=type;}
	public TestArity2Function(String type, Arity2Function<A,B,C> fut ){super();this.type=type; fut(fut); }
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public void fut( Arity2Function<A,B,C> v ) { this.fut=v; }
	public Arity2Function<A,B,C> fut(){ return fut;}
	
	public Boolean testResult(){return testResult;}
	public void testResult(Boolean v){this.testResult=v;}

	public TA<Arity3Input> unitTests(){ return (TA<Arity3Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity3Input>)v;}
	public void addUnitTest( Arity3Input<A,B,C> v ){ unitTests.add( (Arity3Input<A,B,C>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		

	public TA<Arity3Input> failureModeTests(){ return (TA<Arity3Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity3Input>)v;}
	public void addFailureModeTest( Arity3Input<A,B,C> v ){ failureModeTests.add( (Arity3Input<A,B,C>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }		
	
	
	public void test( A a, B b ) 
	{
		String SEP = ", ";
		
		if ( fut==null ) return;
		
		//System.out.println("Test fx: [" + ((fut!=null)? fut.toString() : "fut is null") + "]" );
		
		Boolean _result = evalAt( a, b );
		
		if ( a != null && b != null && result() != null ) {
			//System.out.println("(Arity, Input, Result) = [1" + SEP +  a + SEP + b + SEP + result() +"]"  );
		}
		else if ( result() == null ){
			//System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + b + SEP + "null]"  );
		}
		
		if ( fut.raisedException() )  {
			//System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "raised exception = (" + fut.e().getMessage() + "]"  );
		}
		
		//fut.tracker().report();
		//fut.reset();
		
		testResult(_result);
		
	}
	
	// override for specialized test implementations
	public Boolean evalAt( A a, B b )
	{
		C c = fut.evalAt( a, b );
		result( c );
		return true;	}
	
	public void test( Arity2Input<A,B> in ) 
	{
		A a = in.i1();
		B b = in.i2();
		test(a,b);
	}	
}