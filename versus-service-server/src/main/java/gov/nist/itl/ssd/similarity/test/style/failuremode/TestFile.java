package gov.nist.itl.ssd.similarity.test.style.failuremode;

import java.io.File;

import gov.nist.itl.ssd.similarity.test.TCLFile;

public class TestFile<A> {

	public TestFile(){super();}

	protected TCLFile file;
	
	public boolean testFileExists( String dirName ) throws Exception
	{
		boolean status = true;
		file = null;
		try {
			file = new TCLFile( dirName );
			if ( file.exists() ) {
				status = true;
			}
			else {
				status = false;
			}
		}
		catch( Exception e ) {
			throw e;
		}
		return status;
	}

	public boolean testFileIsReadable( String fileName ) throws Exception
	{
		boolean status = true;
		file = null;
		try {
			file = new TCLFile( fileName );
			if ( file.exists() && file.r() ) {
				status = true;
			}
			else {
				status = false;
			}
		}
		catch( Exception e ) {
			throw e;
		}
		return status;
	}

	public boolean testFileIsValidSize( String fileName ) throws Exception
	{
		// Assume a 4GB file is too large for FS and likely current memory as well (especially 2 of them)
		// 1 GB = 1073741824 // per http://en.wikipedia.org/wiki/Gigabyte
		//   Thus:
		// 4 GB = 1073741824 * 4;
		//	
		// NOTE: Also don't want a 0-byte (e.g., or 0-pixel) file.
	
		long maxSize = 1073741824 * 4;
		long minSize = 1 ;
		long fileSize = 0;
		boolean status = true;
		file = null;
		try {
			file = new TCLFile( fileName );
			if ( file.exists() && file.r() ) {
				fileSize = file.size();
				if ( fileSize > minSize && fileSize < maxSize ) {
					status = true;
				}
				else {
					status = false;
				}
			}
			else {
				status = false;
			}
		}
		catch( Exception e ) {
			throw e;
		}		
		return status;
	}

	public boolean testFileIsNotNull( String fileName ) throws Exception
	{
		boolean status = true;
	   	try {
			if ( fileName==null ) status = true;
			
			File _file = new File(fileName);
		
			if ( _file == null ) {
				status = false;
			}
			else {
				status = true;
			}
		}
		catch( Exception e ) {
			throw e;
		}
		return status;
	}
	
	public boolean testFileIsInExpectedDirectory( TCLFile file, String expectedDir ) throws Exception
	{
		boolean status = true;
	   	try {
			if ( expectedDir.compareToIgnoreCase(file.path_abs())!=0 &&  expectedDir.compareToIgnoreCase(file.path_rel())!=0 ) {
				status = false;
			}
			else {
				status = true;
			}
		}
		catch( Exception e ) {
			throw e;
		}
		return status;
	}	
}
