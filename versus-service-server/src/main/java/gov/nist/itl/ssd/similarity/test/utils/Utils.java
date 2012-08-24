package gov.nist.itl.ssd.similarity.test.utils;

import gov.nist.itl.versus.similarity.comparisons.ImageData;
import java.io.File;


public class Utils 
{
	public static Integer 	_Integer_epsilon= null;
	public static Integer[] _Integer_Array_epsilon= null;
	public static int 		_int_epsilon	= -1;
	public static int[] 	_int_Array_epsilon	= null;
	public static double	_double_epsilon = Double.NaN;
	public static double[]  _double_Array_epsilon = null;
	public static Double	_Double_epsilon = null;
	public static Double[]	_Double_Array_epsilon = null;
	public static Boolean   _Boolean_epsilon = null;
	public static boolean   _boolean_epsilon = false;	// NOTE: This one could be problematic.
	public static String 	_str_epsilon	= "_E_";
	public static String[] 	_str_Array_epsilon	= null;
	public static String 	_EMPTY_STR_ 	= "";
	
	public static String SEP="\t";	
	public static boolean _valid( String v ) { return (!v.equals(_str_epsilon) && v.compareTo(_EMPTY_STR_)!=0 ) ? true : false; }
	public static boolean _valid( int v ) { return (v != _int_epsilon) ? true : false; }
	
	public static boolean _valid( Integer v ) { return (!v.equals(_Integer_epsilon)) ? true : false; }
	public static boolean _valid( Double v ) { return  (!v.equals(_Double_epsilon)) ? true : false; }
	public static boolean _valid( Boolean v ) { return (!v.equals(_Boolean_epsilon)) ? true : false; }
	public static boolean _valid( boolean v ) { return (v!=_boolean_epsilon) ? true : false; }
	public static boolean _valid( double v ) { return  (v!= _double_epsilon) ? true : false; }
	
	public static boolean _cmp( String s1, String s2 ) { return (s1.compareTo(s2)==0) ? true : false; }
	
	public static int 		_s2i( String s ) { return Integer.parseInt(s); }
	public static double 	_s2d( String s ) { return Double.parseDouble(s); }
	public static Integer 	_s2I( String s ) { return Integer.parseInt(s); }
	public static Double  	_s2D( String s ) { return Double.parseDouble(s); }
	
	public static Boolean   _s2B( String s ) { return Boolean.parseBoolean(s); }
	public static boolean   _s2b( String s ) { return Boolean.parseBoolean(s); }	

	public static String href( String s ) 
	{
		String HREF1 = "<a href=\"";
		String HREF2= "\">";
		String HREF3= "</a>\n";
		return HREF1 + s + HREF2 + s + HREF3;
	}
	
	public static String href( String name, String link ) 
	{
		String HREF1 = "<a href=\"";
		String HREF2= "\">";
		String HREF3= "</a>\n";
		return HREF1 + link + HREF2 + name + HREF3;
	}
	
	public static String image( String s ) 
	{
		String IMG1= "<img src=\"";
		String IMG2= "\" height=200 width=200 />\n";
		return IMG1 + s + IMG2;
	}
	
	public static String rname( String fileName )
	{
		File f = new File( fileName );
		String name = f.getName();
		int idx = name.lastIndexOf('.');
		return name.substring(0,idx);
	}
	
	public static String im2s( ImageData im ) {
		String str = "new ImageData( new double[][][]";
		double[][][] values = im.getValues();
		int len1 = values.length;
		int len2 = values[0].length;
		int len3 = values[0][0].length;
		str += "{";
		for (int i=0; i < len1; i++) {
			if ( i!=0 ) str += ",";
			str += "{";
			for (int j=0; j < len2; j++) {
				if (j!=0) str += ",";
				str += "{";
				for (int k=0; k < len3; k++) {
					if ( k!=0 ) str += ",";
					str += values[i][j][k] + "d";
				}
				str += "}";
			}
			str += "}";
		}
		str += "}";
		str += ")";
		return str;
	}
	

	public static void out( String s ) {
		System.out.println(s);
	}
}
