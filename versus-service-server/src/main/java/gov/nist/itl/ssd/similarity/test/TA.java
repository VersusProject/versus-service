/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgment if the
 * software is used.
 *
 *
 *  @author  Benjamin Long, blong@nist.gov
 *  @version 1.1
 *    
 */

package gov.nist.itl.ssd.similarity.test;

import java.util.ArrayList;

import fj.data.Array;

public class TA<A> 
{
	private Array data = Array.empty();
	private int[] frame;
	private int[] shape;
	private int rank;
	private TType type;
	private int level;
	
	public TA() { this(Array.empty()); type(TType.EMPTY); }
	public TA(int length, TType type) {
		type(type); 
		this.data = mkarray(length);
	}
	public void fill( A fillValue ) { int len = length(); for (int i=0; i < len; i++) set(i,fillValue); }
	public void fill( int len, A fillValue ) { mkarray(len,fillValue); }
	
	public TA( TA a ) {
		this.data = a.data();
		this.frame= a.frame();
		this.shape= a.shape();
		this.rank = a.rank();
		this.type = a.type();
		this.level= a.level();
		}
	public TA( Array data ) { this.data = data; }
	public TA( Array data, int level) { this(data); this.level=level;}
	public TA( Array data, TType type) { this(data); this.type=type;}
	public TA( Array data, int[] shape ) { this(data); this.shape=shape;}
	public TA( Array data, int[] shape, int level) { this(data,shape); this.level=level;}
	public TA( Array data, int[] shape, int level,TType type) { this(data,shape,level); this.type=type;}
	public TA( A item ) { add(item); }
	public Array mkarray(int length, A e) { // where e=fillValue
		Array ar = Array.empty();
		for (int i=0; i < length; i++) {
			ar = ar.append( Array.single(e) );
		}
		return ar;
	}
	public Array mkarray(int length) { // where e=fillValue
		Array ar = Array.empty();
		A e = null;
		for (int i=0; i < length; i++) {
			ar = ar.append( Array.single(e) );
		}
		return ar;
	}
	
	public void add( A v ) {data = data.append( Array.single(v) );}
	public void add( Array<A> ar ) {
		int len = ar.length();
		for (int i=0; i < len; i++) add(ar.get(i));
	}
	
	public TA range( int begIdx, int endIdx ) {
		if ( !bounds(begIdx) || !bounds(endIdx)) return null;
		
		TA ar = new TA();
		int len = (endIdx - begIdx) + 1;
		for (int i=0; i < len; i++) {
			ar.add( get(i) );
		}
		return ar;
	}
	
	public void removeAll() {
		int len = length();
		A item = null;
		for (int i=0; i < len; i++) {
			item = (A)get(i);
			remove(item);
		}
	}
	
	public void remove( A item ) {
		if ( !exists(item) ) return;
		int len = data.length();
		A elem = null;
		Array ar = Array.empty();
		for (int i=0; i < len; i++) {
			elem = (A)data.get(i);
			if ( !elem.equals(item) ) ar = ar.append( Array.single( elem ) );
		}
		data = ar;
	}
	public boolean exists( A item ) {
		int len = data.length();
		A elem = null;
		for (int i=0; i < len; i++) {
			elem = (A)data.get(i);
			if ( elem.equals(item) ) return true;
		}
		return false;		
	}
	public void addUnique( A item ) {
		if ( !exists(item) ) 
			add( item );
	}
	public int length() { return data.length(); }
	public void set( int idx, A item ) { 
		int len = data.length();
		A elem = null;
		Array ar = Array.empty();
		for (int i=0; i < len; i++) {
			elem = (A)data.get(i);
			if ( i != idx ) 
				ar = ar.append( Array.single( elem ) );
			else
				ar = ar.append( Array.single( item ) );
		}
		data = ar;
	}
	public A get( int idx ) { 
		if (bounds(idx)) return (A)data.get(idx);
		return null;
	}
	
	public Object[] asArray() {  
		int len = data.length();
		Object[] l = new Object[len];
		for (int i=0; i < len; i++) {
			l[i] = data.get(i);
		}
		return l;
	}

	public String[] asStringArray() {  
		int len = data.length();
		String[] l = new String[len];
		for (int i=0; i < len; i++) {
			l[i] = (String)data.get(i);
		}
		return l;
	}

	public Double[] asDoubleArray() {  
		int len = data.length();
		Double[] l = new Double[len];
		for (int i=0; i < len; i++) {
			l[i] = (Double)data.get(i);
		}
		return l;
	}
	
	public Integer[] asIntegerArray() {  
		int len = data.length();
		Integer[] l = new Integer[len];
		for (int i=0; i < len; i++) {
			l[i] = (Integer)data.get(i);
		}
		return l;
	}
	
	public ArrayList asArrayList() {  
		int len = data.length();
		ArrayList l = new ArrayList();
		for (int i=0; i < len; i++) {
			l.add( data.get(i) );
		}
		return l;
	}
	public Array data(){return data;}	
	public boolean bounds(int i) { int len=length(); return (i>=0 && i<len) ? true : false; }
	
	public void frame(int[] frame){this.frame=frame;}
	public void shape(int[] shape){this.shape=shape;}
	public void rank(int rank){this.rank=rank;}
	public void level(int level){this.level=level;}
	public void type(TType type){this.type=type;}
	public int[] frame(){return frame;}
	public int[] shape(){return shape;}
	public int rank(){return rank;}
	public int level(){return level;}
	public TType type(){return type;}	
	
	public boolean sameShape( TA a ) {
		int[] s1 = shape();
		int[] s2 = a.shape();
		int len1 = s1.length;
		int len2 = s2.length;
		if ( len1 != len2 ) return false;
		for (int i=0; i < len1; i++) // are same length
		{
			if ( s1[i] != s2[i] ) return false;
		}
		return true;
	}
	
	public boolean sameShape( int[] s2 ) {
		int[] s1 = shape();
		int len1 = s1.length;
		int len2 = s2.length;
		if ( len1 != len2 ) return false;
		for (int i=0; i < len1; i++) // are same length
		{
			if ( s1[i] != s2[i] ) return false;
		}
		return true;
	}
		
	public TA reShape( int[] shape ) {
		return (sameShape(shape)) ? new TA(data(),shape) : this;
	}
	
	// get index for a given shape-coordinate 
	// i.e., if have shape={2,3} and want row-1,col-2, then coords={1,2}
	// We expect coords to be the same length as the shape. If not, we return an index value of data.length+1 indicating this invalid condition
	public int idx( int[] coords ) {
		int len = coords.length;
		if ( len != shape().length ) return (length() + 1);
		
		int v = 0;
		int i=0;
		for (; i < len; i++) v += (coords[i] * shape[i]);
		v += coords[i];
		return v;
	}
	
	//public TA subset(int[] indices) {
		
	//}
}
