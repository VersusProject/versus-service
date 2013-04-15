package edu.illinois.ncsa.versus.store;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public class DiskIndexerProcessor implements Serializable,IndexerProcessor{
	String indexerfolder;
	
	public DiskIndexerProcessor(){
		Properties properties;
		
		//this.indexer=indexer;
		
		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.indexerfolder");
			if (location != null) {
				indexerfolder = location;
				//log.debug("directory="+directory);
				System.out.println("folder="+indexerfolder);
			} else {
				indexerfolder = System.getProperty("java.io.tmpdir");
				System.out.println("folder="+indexerfolder);
			}
			// System.out.println("Storing file in " + directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addDescriptor(String indexerId, Descriptor document, String id) {
		File indexerfile = new File(indexerfolder);
        indexerfile.mkdir();
        File indexerIdfile=new File(indexerfolder+"/"+indexerId);
		
        try {
			
			write(indexerIdfile, document);
			write(indexerIdfile,id);
			System.out.println("Index written to "
					+ indexerIdfile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void build() {
	}

	
	@Override
	public List<SearchResult> query(String indexerId,Descriptor query, Measure measure) {
		/*List<SearchResult> sArray=new ArrayList<SearchResult>();
	  	SearchResult result=new SearchResult();
	  	List<Descriptor> des=getDescriptors();
		List<String> ids=getIdentifiers();
		for(int i=0;i<des.size();i++){
			   try {
				 result.setProximity(measure.compare(des.get(i), query));
				 result.setDocId(ids.get(i));
				sArray.add(result);
				result=new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			result.setProximity(measure.compare(query, query));
			 result.setDocId("0");
			sArray.add(result);
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		return sArray;*/

		return null;
	}

	@Override
	public List<SearchResult> query(Descriptor query, int n) {
		
		return null;
	}
	@Override
	public void addDescriptor(String indexerId, Descriptor document) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public List<SearchResult> query(String indexerId, Descriptor query) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void addIndexer(Indexer in) {
		
		
	}

	@Override
	public Indexer getIndexer(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existIndexer(String id) {
		// TODO Auto-generated method stub
		return false;
	}


	/*@Override
	public void setMeasure(String id,Measure measure) {
	}*/

	@Override
	public List<String> getIdentifiers(String indexerId) {
		
		ArrayList<Descriptor> des=new ArrayList<Descriptor>();
		ArrayList<String> ids=new ArrayList<String>();
		
		Descriptor d;
		String id;
		
		FileInputStream in;
		Index index;
        
		File indexerfile = new File(indexerfolder);

		FileInputStream fis;
		try {
			fis = new FileInputStream(indexerfile);

		while (true) {
				try {
					d= (Descriptor)readD(fis);
					id=(String)readId(fis);
					des.add(d);
					ids.add(id);
									
				} catch (EOFException e) {
					fis.close();
					return ids;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	
	
public List<Descriptor> getDescriptors(String indexerId) {
		
		ArrayList<Descriptor> des=new ArrayList<Descriptor>();
		ArrayList<String> ids=new ArrayList<String>();
		
		Descriptor d;
		String id;
		
		FileInputStream in;
		Index index;
        
		File indexerfile = new File(indexerfolder);

		FileInputStream fis;
		try {
			fis = new FileInputStream(indexerfile);

		while (true) {
				try {
					d= (Descriptor)readD(fis);
					id=(String)readId(fis);
					des.add(d);
					ids.add(id);
									
				} catch (EOFException e) {
					fis.close();
					return des;
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	private static void write(File indexFile, Descriptor d ) throws IOException {
		ObjectOutputStream oos = getOOS(indexFile);
		oos.writeObject(d);
		oos.flush();
		oos.close();
	}
	
	private static void write(File indexFile, String id ) throws IOException {
		ObjectOutputStream oos = getOOS(indexFile);
		oos.writeObject(id);
		oos.flush();
		oos.close();
	}

	private static Descriptor readD(FileInputStream fis)
			throws ClassNotFoundException, IOException {
		Descriptor d=  (Descriptor) getOIS(fis).readObject();
		return d;
	}
	private static String readId(FileInputStream fis)
			throws ClassNotFoundException, IOException {
		String d=  (String) getOIS(fis).readObject();
		return d;
	}
	
    
	
	private static ObjectOutputStream getOOS(File indexFile) throws IOException {
		if (indexFile.exists()) {
			// this is a workaround so that we can append objects to an existing
			// file
			return new AppendableObjectOutputStream(new FileOutputStream(
					indexFile, true));
		} else {
			return new ObjectOutputStream(new FileOutputStream(indexFile));
		}
	}

	private static ObjectInputStream getOIS(FileInputStream fis)
			throws IOException {
		long pos = fis.getChannel().position();
		return pos == 0 ? new ObjectInputStream(fis)
				: new AppendableObjectInputStream(fis);
	}

	private static class AppendableObjectOutputStream extends
			ObjectOutputStream {

		public AppendableObjectOutputStream(OutputStream out)
				throws IOException {
			super(out);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// do not write a header
		}
	}

	private static class AppendableObjectInputStream extends ObjectInputStream {

		public AppendableObjectInputStream(InputStream in) throws IOException {
			super(in);
		}

		@Override
		protected void readStreamHeader() throws IOException {
			// do not read a header
		}
	}

	

	
	
}
