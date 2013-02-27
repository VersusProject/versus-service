package gov.nist.itl.ssd.similarity.test.execution.function;

import gov.nist.itl.ssd.similarity.test.TA;
import fj.data.Array;

public class TestArity5Function<A,B,C,D,E,F> extends Function<D>
{
	protected Arity5Function<A,B,C,D,E,F> fut = new Arity5Function<A,B,C,D,E,F>(); // function under test
	
	public TestArity5Function(){super();}
	public TestArity5Function(String type){super(type);this.type=type;}
	public TestArity5Function(String type, Arity5Function<A,B,C,D,E,F> fut ){super();this.type=type; fut(fut); }
	
	public void name(String name){this.name=name;}
	public String name(){ return name; }
	public void type(String type){this.type=type;}
	public String type(){ return type; }
	
	public void fut( Arity5Function<A,B,C,D,E,F> v ) { this.fut=v; }
	public Arity5Function<A,B,C,D,E,F> fut(){ return fut;}
	
	public TA<Arity6Input> unitTests(){ return (TA<Arity6Input>)unitTests;}
	public void unitTests(TA v){ this.unitTests=(TA<Arity6Input>)v;}
	public void addUnitTest( Arity6Input<A,B,C,D,E,F> v ){ unitTests.add( (Arity6Input<A,B,C,D,E,F>)v); }
	public void addUnitTests( Array v ){ unitTests.add(v); }		
	
	public TA<Arity6Input> failureModeTests(){ return (TA<Arity6Input>)failureModeTests;}
	public void failureModeTests(TA v){ this.failureModeTests=(TA<Arity6Input>)v;}
	public void addFailureModeTest( Arity6Input<A,B,C,D,E,F> v ){ failureModeTests.add( (Arity6Input<A,B,C,D,E,F>)v); }
	public void addFailureModeTests( Array v ){ failureModeTests.add(v); }		
	
	public void test( A a, B b, C c, D d, E e ) 
	{
		String SEP = ", ";
		
		if ( fut==null ) return;
		
		System.out.println("Test fx: [" + ((fut!=null)? fut.toString() : "fut is null") + "]" );
		
		F f = evalAt( a, b, c, d, e );
		
		//if ( !a.equals(null) && !b.equals(null) && !c.equals(null) && !d.equals(null) ) {
		if ( a!=null && b!=null && c!=null && d!=null && e!=null && f!=null) {
			System.out.println("(Arity, Input, Result) = [1" + SEP +  a + SEP + b + SEP + c + SEP + d + SEP + e + SEP + f + "]"  );
		}
		else if ( f==null ){
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + b + SEP + c + SEP + d + SEP +  e + SEP + "null]"  );
		}
		
		if ( fut.raisedException() )  {
			System.out.println("(Arity, Input, Result) = [1" + SEP + a + SEP + "raised exception = (" + fut.e().getMessage() + "]"  );
		}
		
		//	fut.tracker().report();
		
		// fut.reset();	 
	}
	
	// override for specialized test implementations
	public F evalAt( A a, B b, C c, D d, E e )
	{
		F f = fut.evalAt( a, b, c, d, e );
		return f;
	}
	
	public void test( Arity5Input<A,B,C,D,E> in ) 
	{
		A a = in.i1();
		B b = in.i2();
		C c = in.i3();
		D d = in.i4();
		E e = in.i5();
		test(a,b,c,d,e);
	}	
}