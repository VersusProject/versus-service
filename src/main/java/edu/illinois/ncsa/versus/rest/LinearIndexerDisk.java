package edu.illinois.ncsa.versus.rest;

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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import edu.illinois.ncsa.clustering.ClusterTreeExpanded;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public class LinearIndexerDisk implements Serializable, Indexer {
	String id;
	Measure measure;
	String indexerfolder;
	ArrayList<String> identifiers;
	ArrayList<Descriptor> descriptors;
	IndexContent inc;

	public LinearIndexerDisk() {
		Properties properties;
		inc= new IndexContent();
		identifiers = new ArrayList<String>();
		descriptors = new ArrayList<Descriptor>();
		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.indexerfolder");
			if (location != null) {
				indexerfolder = location;
				// log.debug("directory="+directory);
				File indexerfile = new File(indexerfolder);
				if (!indexerfile.exists())
					indexerfile.mkdir();

				System.out.println("folder=" + indexerfolder);
			} else {
				indexerfolder = System.getProperty("java.io.tmpdir");
				System.out.println("folder=" + indexerfolder);
			}
			// System.out.println("Storing file in " + directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setId(String id) {

		this.id = id;

		System.out.println("Setting id for Linear Indexer");

		File indexerfile = new File(indexerfolder + "/" + id + ".txt");

		FileInputStream fis = null;

		if (indexerfile.exists()) {
			System.out.println("indexer file exist");
			try {
				fis = new FileInputStream(indexerfile);

				try {
					inc = (IndexContent) read(fis);

					descriptors.addAll(inc.getDescriptors());
					identifiers.addAll(inc.getIds());

				} catch (EOFException e) {

				} finally {
					fis.close();
				}

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Measure getMeasure() {
		return measure;
	}

	@Override
	public void setMeasure(Measure measure) {
		this.measure = measure;

	}

	@Override
	public List<String> getIdentifiers() {
		if (identifiers.isEmpty())
			return null;
		else
			return identifiers;
	}

	@Override
	public List<Descriptor> getDescriptors() {
		return descriptors;
	}

	@Override
	public void addDescriptor(Descriptor document) {

	}

	@Override
	public void addDescriptor(Descriptor document, String id) {

		File indexerIdfile = new File(indexerfolder + "/" + getId() + ".txt");

		descriptors.add(document);
		identifiers.add(id);
	}

	@Override
	public void build() {

		File indexerIdfile = new File(indexerfolder + "/" + getId() + ".txt");
		
		//inc.getDescriptors().clear();
		//inc.getIds().clear();
		
		
		
		inc.setDescriptors(descriptors);
		inc.setIds(identifiers);

		try {
			write(indexerIdfile, inc);
			System.out.println("Index written to "
					+ indexerIdfile.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<SearchResult> query(Descriptor query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SearchResult> query(Descriptor query, Measure measure) {

		List<SearchResult> sArray = new ArrayList<SearchResult>();
		SearchResult result = new SearchResult();

		List<Descriptor> des = getDescriptors();
		List<String> ids = getIdentifiers();

		for (int i = 0; i < des.size(); i++) {
			try {
				result.setProximity(measure.compare(des.get(i), query));
				result.setDocId(ids.get(i));
				sArray.add(result);
				result = new SearchResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			result.setProximity(measure.compare(query, query));
			result.setDocId("0");
			sArray.add(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sArray;

	}

	@Override
	public List<SearchResult> query(Descriptor query, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void write(File indexerFile, IndexContent hd)
			throws IOException {
		ObjectOutputStream oos = getOOS(indexerFile);
		oos.writeObject(hd);
		oos.flush();
		oos.close();
	}

	private static IndexContent read(FileInputStream fis)
			throws ClassNotFoundException, IOException {
		IndexContent d = (IndexContent) getOIS(fis).readObject();
		return d;
	}

	private static ObjectOutputStream getOOS(File indexFile) throws IOException {
		return new ObjectOutputStream(new FileOutputStream(indexFile));

	}

	private static ObjectInputStream getOIS(FileInputStream fis)
			throws IOException {
		long pos = fis.getChannel().position();
		// return pos == 0 ? new ObjectInputStream(fis): new
		// AppendableObjectInputStream(fis);
		return new ObjectInputStream(fis);

	}

	/*
	 * private static class AppendableObjectOutputStream extends
	 * ObjectOutputStream {
	 * 
	 * public AppendableObjectOutputStream(OutputStream out) throws IOException
	 * { super(out); }
	 * 
	 * @Override protected void writeStreamHeader() throws IOException { // do
	 * not write a header } }
	 * 
	 * private static class AppendableObjectInputStream extends
	 * ObjectInputStream {
	 * 
	 * public AppendableObjectInputStream(InputStream in) throws IOException {
	 * super(in); }
	 * 
	 * @Override protected void readStreamHeader() throws IOException { // do
	 * not read a header } }
	 */

}
