package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity7Function<A,B,C,D,E,F,G,H> extends Function<H>
{
	public Arity7Function(){super();}
	public Arity7Function(String type){super(type);}
	
	public H evalAt( A a, B b, C c, D d, E e, F f, G g ) {
		return null;
	}
	
	public H evalDerivativeAt( A a, B b, C c, D d, E e, F f, G g ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e, F f, G g, H h ) {
		if ( evalAt(a,b,c,d,e,f,g).equals(h) )
			return true;
		return false;
	}	
	
}