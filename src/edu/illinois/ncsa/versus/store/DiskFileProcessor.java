/**
 * 
 */
package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

import edu.illinois.ncsa.versus.restlet.PropertiesUtil;

/**
 * Disk based file storage.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class DiskFileProcessor implements FileProcessor {

	private String directory;

	public DiskFileProcessor() {
		Properties properties;
		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.directory");
			if (location != null) {
				directory = location;
			} else {
				directory = System.getProperty("java.io.tmpdir");
			}
			System.out.println("Storing file in " + directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String addFile(InputStream inputStream) {
		String id = UUID.randomUUID().toString();
		File file = new File(directory, id);
		try {
			OutputStream out = new FileOutputStream(file);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			inputStream.close();
			return id;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public InputStream getFile(String id) {
		File file = new File(directory, id);
		System.out.println("Reading file " + file);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
