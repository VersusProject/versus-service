package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity8Function<A,B,C,D,E,F,G,H,I> extends Function<I>
{
	public Arity8Function(){super();}
	public Arity8Function(String type){super(type);}
	
	public I evalAt( A a, B b, C c, D d, E e, F f, G g, H h ) {
		return null;
	}
	
	public I evalDerivativeAt( A a, B b, C c, D d, E e, F f, G g, H h ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e, F f, G g, H h, I i ) {
		if ( evalAt(a,b,c,d,e,f,g,h).equals(i) )
			return true;
		return false;
	}	
	
}