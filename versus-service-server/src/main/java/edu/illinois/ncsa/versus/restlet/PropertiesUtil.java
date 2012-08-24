/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties on disk in versus.properties
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 */
public class PropertiesUtil {

	public static Properties load() throws IOException {
		Properties defaultProps = new Properties();
		InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("versus.properties"); 
//		FileInputStream inputStream = new FileInputStream("versus.properties");
		defaultProps.load(inputStream);
		inputStream.close();
		return defaultProps;
	}
}
