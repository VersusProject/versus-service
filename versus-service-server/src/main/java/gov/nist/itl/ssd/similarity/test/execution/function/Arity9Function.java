package gov.nist.itl.ssd.similarity.test.execution.function;

public class Arity9Function<A,B,C,D,E,F,G,H,I,J> extends Function<J>
{
	public Arity9Function(){super();}
	public Arity9Function(String type){super(type);}
	
	public J evalAt( A a, B b, C c, D d, E e, F f, G g, H h, I i ) {
		return null;
	}
	
	public J evalDerivativeAt( A a, B b, C c, D d, E e, F f, G g, H h, I i ) {
		return null;
	}
	
	public Boolean expectedValueAt( A a, B b, C c, D d, E e, F f, G g, H h, I i, J j ) {
		if ( evalAt(a,b,c,d,e,f,g,h,i).equals(j) )
			return true;
		return false;
	}		
}