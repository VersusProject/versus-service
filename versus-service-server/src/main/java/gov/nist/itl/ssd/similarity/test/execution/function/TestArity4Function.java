package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class TestArity4Function<A,B,C,D,Ev> extends Function<Ev>
{
	protected Arity4Function<A,B,C,D,Ev> fut = new Arity4Function<A,B,C,D,Ev>(); // function under test
	protected Boolean testResult;
	
	public TestArity4Function(){super();}
	public TestArity4Function(String type){super(type);this.type=type;}
	public TestArity4Function(String type, Arity4Function<A,B,C,D,Ev> fut ){super();this.type=type; fut(fut); }
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public void fut( Arity4Function<A,B,C,D,Ev> v ) { this.fut=v; }
	public Arity4Function<A,B,C,D,Ev> fut(){ return fut;}
	public Boolean testResult(){return testResult;}
	public void testResult(Boolean v){this.testResult=v;}
	
	public TA<Arity5Input> unitTests(){ return (TA<Arity5Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity5Input>)v;}
	public void addUnitTest( Arity5Input<A,B,C,D,Ev> v ){ unitTests.add( (Arity5Input<A,B,C,D,Ev>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		
	
	public TA<Arity5Input> failureModeTests(){ return (TA<Arity5Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity5Input>)v;}
	public void addFailureModeTest( Arity5Input<A,B,C,D,Ev> v ){ failureModeTests.add( (Arity5Input<A,B,C,D,Ev>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }		
	
	public void test( A a, B b, C c, D d ) 
	{
		String SEP = ", ";
		
		if ( fut==null ) return;
		
		System.out.println("Test fx: [" + ((fut!=null)? fut.toString() : "fut is null") + "]" );
		
		Boolean _result = evalAt( a, b, c, d );
		
		if ( a!=null && b!=null && c!=null && d!=null && result()!=null ) {
			System.out.println("(Arity, Input, Result) = [1" + SEP +  a + SEP + b + SEP + c + SEP + d + SEP + result() + "]"  );
		}
		else if ( e==null ){
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + b + SEP + c + SEP + d + SEP + "null]"  );
		}
		
		if ( fut.raisedException() )  {
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "raised exception = (" + fut.e().getMessage() + "]"  );
		}
		
		//	fut.tracker().report();
		
		// fut.reset();	 
			testResult(_result);
	}
	
	// override for specialized test implementations
	public Boolean evalAt( A a, B b, C c, D d )
	{
		Ev e = fut.evalAt( a, b, c, d );
		result(e);
		return true;
	}
	
	public void test( Arity4Input<A,B,C,D> in ) 
	{
		A a = in.i1();
		B b = in.i2();
		C c = in.i3();
		D d = in.i4();
		test(a,b,c,d);
	}	
}