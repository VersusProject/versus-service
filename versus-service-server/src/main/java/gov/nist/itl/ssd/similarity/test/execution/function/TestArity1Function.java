package gov.nist.itl.ssd.similarity.test.execution.function;
import fj.data.Array;
import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.execution.*;

public class TestArity1Function<A,B> extends Function<B>
{
	protected Arity1Function<A,B> fut = new Arity1Function<A,B>(); // function under test
	
	protected Boolean testResult;
	
	public TestArity1Function(){super();}
	public TestArity1Function(String type){super(type);this.type=type;}
	public TestArity1Function(String type, Arity1Function<A,B> fut ){super();this.type=type; fut(fut); }
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public TA<Arity2Input> unitTests(){ return (TA<Arity2Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity2Input>)v;}
	public void addUnitTest( Arity2Input<A,B> v ){ unitTests.add( (Arity2Input<A,B>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		
	
	public TA<Arity2Input> failureModeTests(){ return (TA<Arity2Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity2Input>)v;}
	public void addFailureModeTest( Arity2Input<A,B> v ){ failureModeTests.add( (Arity2Input<A,B>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }		
	
	public void fut( Arity1Function<A,B> v ) { this.fut=v; }
	public Arity1Function<A,B> fut(){ return fut;}
	
	public Boolean testResult(){return testResult;}
	public void testResult(Boolean v){this.testResult=v;}
	
	public void test( A a ) 
	{
		String SEP = ", ";
		
		if ( fut==null ) return;
		
		System.out.println("Test fx: [" + ((fut!=null)? fut.toString() : "fut is null") + "]" );
		
		Boolean _result = evalAt( a );
		
		if ( a != null  && result() != null ) {
			System.out.println("(Arity, Input, Result) = [1" + SEP +  a + SEP + result() + "]"  );
		}
		else if ( result() == null ) {
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "null]"  );
		}
		
		if ( fut.raisedException() )  {
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "raised exception = (" + fut.e().getMessage() + "]"  );
		}
		
		//fut.tracker().report();
		
		//fut.reset();
			
		testResult(_result);
	}
	
	// override for specific test implementations
	public Boolean evalAt( A a ) {
		B b = fut.evalAt( a );
		result( b );
		return true;
	}
	
	public void test( Arity1Input<A> in ) 
	{
		A a = in.i1();
		test(a);
	}	
}
