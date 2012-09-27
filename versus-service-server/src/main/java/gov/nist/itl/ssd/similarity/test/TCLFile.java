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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import gov.nist.itl.ssd.similarity.test.TA;

public class TCLFile 
{
	private File file;
	private String name;
	private boolean isDirectory = false;
	private boolean exists = false;
	
	public TCLFile() { this("."); file = new File(".");}
	public TCLFile( String name ){ name(name); file = new File(name); }
	
	public File file(){return file;}
	public void file(File f){this.file=f;}
	public String name(){ return name; }
	public void name(String s){this.name=s;}
	public void exists(boolean x){ this.exists=x; }
	
	// exists
	public boolean exists() throws Exception {
		boolean x = false;
		try {
			x = file.exists();
		}
		catch( Exception e ) {
			throw e;
		}
		return x;
	}
	
	// name
	public String file_name() throws Exception {
		String s = "";
		try {
			s = file.getName();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;
	}
	
	// path
	public String path_rel() throws Exception {
		String s = "";
		try {
			s = file.getCanonicalPath();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;		
	}
	
	public String path_abs() throws Exception {
		String s = "";
		try {
			s = file.getAbsolutePath();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;		
	}	
	
	// dir
	public boolean isDir() { 
		isDirectory=file.isDirectory(); 
		return isDirectory;
	}
	
	// rename
	public void rename( String newName ) throws Exception {
		try {
			file.renameTo(new File(newName));
		}
		catch( Exception e ) {
			throw e;
		}
		name(newName);
	}
	
	// rm file/dir
	public boolean delete() throws Exception {
		boolean ok = false;
		try {
			ok = file.delete();
		}
		catch( Exception e ) {
			throw e;
		}
		return ok;
	}
	
	// create
	public File dir_create( String name ) throws Exception {
		file = new File(name);
		boolean ok = false;
		
		try {
			ok = file.mkdir();
		}
		catch( Exception e ) {
			throw e;
		}
		return file;
	}
	
	public File dirs_create( String name ) throws Exception {
		file = new File(name);
		boolean ok = false;
		
		try {
			ok = file.mkdirs();
		}
		catch( Exception e ) {
			throw e;
		}
		return file;
	}	
	// file
	public File file_create( String name ) throws Exception {
		file = new File(name);
		boolean ok = false;
		
		try {
			ok = file.createNewFile();
		}
		catch( Exception e ) {
			throw e;
		}
		return file;
	}
	
	// r/w/append
	public void out( PrintWriter pw, int c) throws Exception {
		try {
			pw.print(c);
		}
		catch ( Exception e ) {
			throw e;
		}
	}
	
	public void out( PrintWriter pw, String s ) throws Exception {
		try {
			pw.print(s);
		}
		catch ( Exception e ) {
			throw e;
		}
	}
	
	public void outln( PrintWriter pw, String s ) throws Exception {
		try {
			pw.println(s);
		}
		catch ( Exception e ) {
			throw e;
		}
	}
	
	public int in( BufferedReader rdr ) throws Exception {
		int c = -1;
		try {
			c = rdr.read();
		}
		catch( Exception e ) {
			throw e;
		}
		return c;
	}
	
	public String inln( BufferedReader rdr ) throws Exception {
		String s = "";
		try {
			s = rdr.readLine();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;		
	}

	public String[] readAllLines( BufferedReader rdr ) throws Exception {
		TA lines = new TA();
		String line = "";
		try {
			line = inln(rdr);
			while (line != null) {
				lines.add( line );
				line = inln(rdr);
			}
			close(rdr);
		}
		catch( Exception e ) {
			throw e;
		}
		return lines.asStringArray();
	}

	public String readAllToString( BufferedReader rdr ) throws Exception {
		String s = "";
		String line = "";
		try {
			line = inln(rdr);
			while (line != null) {
				s += line + "\n";
				line = inln(rdr);
			}
			close(rdr);
		}
		catch( Exception e ) {
			throw e;
		}
		return s;
	}	
	
	// size
	public long size(){ return file.length(); }
	
	// r/w attributes
	public boolean r(){ return file.canRead(); 		}
	public boolean w(){ return file.canWrite(); 	}
	public boolean x(){ return file.canExecute(); 	}
	public boolean r(boolean v) { return file.setReadable(v); }
	public boolean w(boolean v) { return file.setWritable(v); }
	public boolean x(boolean v) { return file.setExecutable(v); }

	// indexed write
	
	// open
	public PrintWriter open_overwrite( String fileName )
	{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(fileName, false));
		} catch (Exception e) {
			System.out.println( "Error: " + e.getMessage() );
		}
		return pw;
	}
	
	public PrintWriter open_append( String fileName )
	{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(fileName, true));	
		} catch (Exception e) {
			System.out.println( "Error: " + e.getMessage() );
		}
		return pw;
	}	
	
	public BufferedReader open( String fileName ) throws Exception {
		BufferedReader rdr = null;
		try {
			rdr = new BufferedReader(new FileReader(fileName));
		}
		catch ( Exception e ) {
			throw e;
		}
		return rdr;
	}
	
	// close
	public void close( PrintWriter pw ) throws Exception {
		try {
			pw.close();
		}
		catch (Exception e ) {
			throw e;
		}
	}

	public void close( BufferedReader rdr ) throws Exception {
		try {
			rdr.close();
		}
		catch (Exception e ) {
			throw e;
		}
	}
	
	// get current working directory
	public String cwd_rel() throws Exception {	// canonical path
		File f = new File(".");
		String s = "";
		try {
			s = f.getCanonicalPath();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;
	}
	public String cwd_abs() throws Exception {	// absolute path
		File f = new File(".");
		String s = "";
		try {
			s = f.getAbsolutePath();
		}
		catch( Exception e ) {
			throw e;
		}
		return s;
	}
	
	public File file_cwd_rel() throws Exception {	// canonical file
		File f = new File(".");
		File cf = null;
		try {
			cf = f.getCanonicalFile();
		}
		catch( Exception e ) {
			throw e;
		}
		return cf;
	}
	
	public File file_cwd_abs() throws Exception {	// absolute file
		File f = new File(".");
		File cf = null;
		try {
			cf = f.getAbsoluteFile();
		}
		catch( Exception e ) {
			throw e;
		}
		return cf;
	}

	
}
