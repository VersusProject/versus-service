package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class TestArity2With3Function<A,B,C,D> extends Function<C>
{
	protected Arity2Function<A,B,C> fut = new Arity2Function<A,B,C>(); // function under test
	protected Boolean testResult;
	
	public TestArity2With3Function(){super();}
	public TestArity2With3Function(String type){super(type);this.type=type;}
	public TestArity2With3Function(String type, Arity2Function<A,B,C> fut ){super();this.type=type; fut(fut); }

	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public void fut( Arity2Function<A,B,C> v ) { this.fut=v; }
	public Arity2Function<A,B,C> fut(){ return fut;}
	public Boolean testResult(){return testResult;}
	public void testResult(Boolean v){this.testResult=v;}
	
	public TA<Arity4Input> unitTests(){ return (TA<Arity4Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity4Input>)v;}
	public void addUnitTest( Arity4Input<A,B,C,D> v ){ unitTests.add( (Arity4Input<A,B,C,D>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		

	public TA<Arity4Input> failureModeTests(){ return (TA<Arity4Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity4Input>)v;}
	public void addFailureModeTest( Arity4Input<A,B,C,D> v ){ failureModeTests.add( (Arity4Input<A,B,C,D>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }			
	
	public void test( A a, B b, C c ) 
	{
		String SEP = ", ";
		
		if ( fut==null ) return;
		
		System.out.println("Test fx: [" + ((fut!=null)? fut.toString() : "fut is null") + "]" );
		
		Boolean _result = evalAt( a, b, c );
		
		//if ( !a.equals(null) && !b.equals(null) && !c.equals(null) && !d.equals(null) ) {
		if ( a!=null && b!=null && c!=null && result()!=null ) {
			System.out.println("(Arity, Input, Result) = [1" + SEP +  a + SEP + b + SEP + c + SEP + result() + "]"  );
		}
		else if ( result()==null ){
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + b + SEP + c + SEP + "null]"  );
		}
		
		if ( fut.raisedException() )  {
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "raised exception = (" + fut.e().getMessage() + "]"  );
		}
		
		//	fut.tracker().report();
		
		// fut.reset();	 // NOTE: We don't want to reset on each one if we're running image files through. 
						 //		This is the primary case where we need tracking across individual test calls.
			
			testResult(_result);
	}
	
	// override for specialized test implementations
	public Boolean evalAt( A a, B b, C c )
	{
		//fut.evalAt( a, b );
		result( fut.evalAt( a, b ) );
		return true;
	}
	
	public void test( Arity3Input<A,B,C> in ) 
	{
		A a = in.i1();
		B b = in.i2();
		C c = in.i3();
		test(a,b,c);
	}	
}