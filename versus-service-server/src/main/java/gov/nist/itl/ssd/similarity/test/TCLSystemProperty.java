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

import static gov.nist.itl.ssd.similarity.test.TConstants.SEP;
import static gov.nist.itl.ssd.similarity.test.TConstants.EOL;

public class TCLSystemProperty 
{
	private String name;
	private String value;
	private String category;	// category of property information; e.g., hardware, sw, os, user, jvm, cfg, etc.
	
	public TCLSystemProperty(String name, String value) { this.name=name; this.value=value;}
	public TCLSystemProperty(String name, String value, String category) { this(name,value); this.category=category;}
	public void name(String v){this.name=v;}
	public String name(){ return name; }
	public void value(String v){ this.value=v; }
	public String value(){ return value; }	
	public void category(String v){ this.category=v;}
	public String category(){ return category; }
	public String toString() {
		return name() + SEP + value() + SEP + category();
	}
	
	public Boolean equal( TCLSystemProperty p ) {
		if ( name != null && p.name() != null && name.compareTo(p.name())!=0 ) return false;
		else if ( value != null && p.value() != null && value.compareTo(p.value())!=0 ) return false;
		else if ( category != null && p.category() != null && category.compareTo(p.category())!=0 ) return false;
		return true;
	}
	
	public static TCLSystemProperty read( String csvString ) {
		int len = csvString.length();
		String str = "";
		TA<String> vals = new TA();
		TCLSystemProperty prop = null;
		for (int i=0; i < len; i++) {
			if ( csvString.charAt(i) == '\t' ) {
				if ( str.length() != 0 )
					vals.add(str);
				str = "";
			}
			else {
				str += csvString.charAt(i);
			}
		}
		// get last
		if ( str.length() != 0 )
			vals.add(str);
		
		try {
			prop = new TCLSystemProperty( vals.get(0), vals.get(1), vals.get(2) );
		}
		catch( Exception e ) {
			System.out.println("Error: could not read system property from delimited string");
		}
		return prop;
	}
}
