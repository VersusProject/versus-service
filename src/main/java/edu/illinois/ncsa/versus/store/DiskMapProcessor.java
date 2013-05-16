package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
//import org.eclipse.jetty.util.log.Log;

import edu.illinois.ncsa.versus.rest.IdMap;
import edu.illinois.ncsa.versus.rest.IndexResource;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;

public class DiskMapProcessor implements MapProcessor {
	
	private static Log log = LogFactory.getLog(DiskMapProcessor.class);

	String mapfile;

	DiskMapProcessor() {
		Properties properties;
		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.map");
			if (location != null) {
				mapfile = location;
				// log.debug("directory="+directory);
				System.out.println("mapfile=" + mapfile);
			} else {
				mapfile = System.getProperty("java.io.tmpdir");
				System.out.println("mapfile=" + mapfile);
			}
			// System.out.println("Storing file in " + directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void addMap(IdMap m) {
		File mfile=new File(mapfile);
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(mfile));
			oos.writeObject(m);
			log.debug("writing to file"+mapfile);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public IdMap getMap() {
		
		File mfile= new File(mapfile);
		IdMap imap;
		
		try {
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(mfile));
		   	imap=(IdMap) ois.readObject();
		   	log.debug("reading a file"+mapfile);
		   	if(imap==null){
		   		log.debug("Object read is null");
		   	}
		   	ois.close();
		   	return imap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
