package gov.nist.itl.ssd.similarity.test.fault.hw_incompatibility;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import gov.nist.itl.versus.similarity.comparisons.exception.HWIndependenceException;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.TCLFile;
import gov.nist.itl.ssd.similarity.test.TCLSystemProperty;

public class HW 
{
	protected long spaceCurrentlyInUse;
	protected long spaceUsedSinceStartOfSession;
	protected long spaceNeededToExecSpecificCmd;	
	protected long totalFreeSpace;
	protected long totalSpaceUsedByNamedObject;	
	protected long totalSpaceUsedByLocale;	
	protected TA hw;
	protected TA sw;
	protected TA os;
	protected TA user;
	protected TA jvm;
	protected TA cfg;
	protected TA arch;
	protected TA java;
	protected TA other;	
	protected TA baseline;
	
	public HW()
	{
		hw = new TA();
		sw = new TA();
		os = new TA();
		user= new TA();
		jvm = new TA();
		cfg = new TA();
		arch = new TA();
		java = new TA();
		other= new TA();	
		baseline = new TA();
		
	}

	public static long  totalProcessors() { return Runtime.getRuntime().availableProcessors(); }
	public static long  freeMemory() { return Runtime.getRuntime().freeMemory(); }
	public static long  maxMemWillUse() { return  Runtime.getRuntime().maxMemory(); }
	public static long  totalJvmMemUsed() { return Runtime.getRuntime().totalMemory(); }
	
	public TA baselineInfo()
	{
		TA b = filterProperties(properties());
		b.add( new TCLSystemProperty("Total machine processors",""+totalProcessors(),"HW") );
		b.add( new TCLSystemProperty("Total memory free in JVM (bytes)",""+freeMemory(),"HW") );
		b.add( new TCLSystemProperty("Total memory used by JVM (bytes)",""+maxMemWillUse(),"HW") );
		b.add( new TCLSystemProperty("Max memory available to JVM (bytes)",""+totalJvmMemUsed(),"HW") );
		return b;
	}
	
	public void saveBaselineInfo( String fileName )
	{
		TCLFile file = new TCLFile();
		PrintWriter pw = null;
		try {
			pw = file.open_overwrite(fileName);
			TA b = baselineInfo();
			int len = b.length();
			for (int i=0; i < len; i++) {
				pw.println( (TCLSystemProperty)b.get(i) );
			}			
			file.close(pw);
		}
		catch( Exception e ) {
			System.out.println("Error: could not save baselineInfo for HW Compatibility Test" );
		}
	}
	
	public TA readBaselineInfo( String fileName )
	{
		TA b = new TA();
		TCLFile file = new TCLFile();
		BufferedReader rdr = null;
		try {
			rdr = file.open(fileName);
			String[] lines = file.readAllLines(rdr);
			file.close(rdr);
			int len = lines.length;
			TCLSystemProperty prop = null;
			for (int i=0; i < len; i++) {
				prop = TCLSystemProperty.read( lines[i] );
				b.add( prop );
			}			
		}
		catch( Exception e ) {
			System.out.println("Error: could not load baselineInfo for HW Compatibility Test" );
		}
		return b;
	}
	
	public void loadBaselineInfo( String fileName )
	{
		baseline = readBaselineInfo(fileName);
	}
	
	public Boolean compareBaselineInfo( String fileName ) throws Exception
	{
		TA bl = baselineInfo();
		TA loaded = readBaselineInfo(fileName);
		
		int len1 = bl.length();
		int len2 = loaded.length();
		
		if ( len1 != len2 ) 
			throw new HWIndependenceException( "node has different number of baseline parameters than last known baseline"); // one has a diff number of properties than the other
		
		// if get here, are same length
		int len = len1;
		TCLSystemProperty p1 = null;
		TCLSystemProperty p2 = null;
		for (int i=0; i < len; i++) {
			p1 = (TCLSystemProperty)bl.get(i);
			p2 = (TCLSystemProperty)loaded.get(i);
			if ( !p1.equals(p2) ) 
					throw new HWIndependenceException("node has different configuration for same property values");
		}
		return true;
	}
	
	public static String getSystemBaselineInfo1( String[][] properties ) {
		String str = "";
		
		for (int i=0; i < properties.length; i++) {
			str += properties[i][0] + "\t" + properties[i][1] + "\t" + prop(properties[i][2]) + "\n";
		}
		str += ( "System" + "\t" + "Total machine processors" + "\t" + totalProcessors() ) + "\n" ;
		str += ( "System" + "\t" + "Total memory free in JVM (bytes)" + "\t" + freeMemory() )+ "\n";
		str += ( "System" + "\t" + "Total memory used by JVM (bytes)" + "\t" + maxMemWillUse() )+ "\n";
		str += ( "System" + "\t" + "Max memory available to JVM (bytes)" + "\t" + totalJvmMemUsed() )+ "\n";
		
		return str;
	}
	
	public static String prop(String key) {
		String s = System.getProperty(key);
		if (s == null)
			s = "[not available]";
		return s;
	}	
	
	public static TA properties() {
		TA sp = new TA();
		Properties p = System.getProperties();
		Enumeration e = p.propertyNames();
		String n="";
		String v="";
		for (; e.hasMoreElements(); ) {
		    n = (String)e.nextElement();
		    v = (String)p.get(n);
		    sp.add( new TCLSystemProperty(n,v) );
		}
		return sp;
	}
	
	public static void viewProperties(){
		TA p = properties();
		int len = p.length();
		for (int i=0; i < len; i++)
			System.out.println( (TCLSystemProperty)p.get(i) );
	}

	
	public TA filterProperties( TA origProperties ) {
		TA p = properties();
		TA filtered = new TA();
		
		int len = origProperties.length();
		TCLSystemProperty prop = null;
		for (int i=0; i < len; i++) {
			prop = (TCLSystemProperty)origProperties.get(i);
				//System.out.println("(name,value)=(" + prop.name() + "," + prop.value() + ")" );
			if 		( prop.name().startsWith("java.vm"))	{prop.category("JVM");jvm.add(prop); filtered.add(prop);}
			else if ( prop.name().startsWith("java.")) 		{prop.category("JAVA");java.add(prop);filtered.add(prop);}
			else if ( prop.name().startsWith("os.")) 		{prop.category("OS"); os.add(prop); filtered.add(prop);}
			else if ( prop.name().startsWith("user.")) 		{prop.category("USER");user.add(prop);filtered.add(prop);}
			else if ( prop.name().indexOf(".cpu.")!= -1) 	{prop.category("HW"); hw.add(prop); filtered.add(prop);}
			else if ( prop.name().indexOf(".io.")!= -1) 	{prop.category("HW"); hw.add(prop); filtered.add(prop);}
			else if ( prop.name().startsWith("sun.")) 		{prop.category("ARCH"); arch.add(prop); filtered.add(prop);}
			else {
															 prop.category("OTHER"); other.add(prop); 
			}
		}
		return filtered;
	}	
	
	public void showPropCategories( String categoryName, TA props )
	{
		int len = props.length();
		for (int i=0; i < len; i++) {
			System.out.println(categoryName + "\t" + props.get(i) );
		}
	}
	
	public void showPropCategories( TA props )
	{
		int len = props.length();
		for (int i=0; i < len; i++) {
			System.out.println( props.get(i) );
		}
	}		
	
	public static void main( String[] args ) {
		HW hw = new HW();
		hw.saveBaselineInfo("./hwCompatData_baselineInfo.txt");
	}
}