package gov.nist.itl.ssd.similarity.test.input.source;

import java.io.BufferedReader;
import java.io.PrintWriter;

import gov.nist.itl.ssd.similarity.test.TA;
import gov.nist.itl.ssd.similarity.test.TCLFile;

public class ImageFileNameList extends DataSource<String>
{
	protected String dir;
	protected TA fileNames;
	
	public ImageFileNameList(){super();}	
	public void dir(String v){this.dir=v;}
	public String dir(){ return dir; }
	public void fileNames(TA v){this.fileNames=v;}
	public TA fileNames(){ return fileNames; }
	public String fileName(int idx) { return (fileNames.bounds(idx)) ? (String)fileNames.get(idx) : ""; }
	public int length(){ return fileNames.length(); }
	
	public String[] load( String fileName )
	{
		BufferedReader rdr = null;
		TCLFile file = new TCLFile(fileName);
		String[] files = null;
		try {
			rdr = file.open(fileName);
			files = file.readAllLines(rdr);
			file.close(rdr);
		}
		catch( Exception e ) {
			System.out.println( "Error: cannot read fileNames file." );
		}
		//fileNames.removeAll();
		//int len = files.length;
		//for (int i=0; i < len; i++) System.out.println( files[i] ); 
			//fileNames.add( files[i] );
		return files;
	}

	public void save( String fileName )
	{
		PrintWriter pw = null;
		TCLFile file = new TCLFile(fileName);
		try {
			pw = file.open_overwrite(fileName);
			int len = fileNames.length();
			for (int i=0; i < len; i++) pw.println( fileNames.get(i) );
			file.close(pw);
		}
		catch( Exception e ) {
			System.out.println( "Error: cannot write fileNames file." );
		}
	}

}
