/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.io.InputStream;

/**
 * Files storage.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public interface FileProcessor {

	/**
	 * Store file stream.
	 * 
	 * @param inputStream
	 * @return unique identifier for file
	 * @throws Exception
	 */
	String addFile(InputStream inputStream, String filename) throws Exception;

	InputStream getFile(String id) throws Exception;

}
