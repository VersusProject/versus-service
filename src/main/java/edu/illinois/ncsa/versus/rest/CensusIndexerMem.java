package edu.illinois.ncsa.versus.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kgm.utility.Pair;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.clustering.ClusterTreeExpanded;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.impl.EuclideanDistanceMeasure;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

public class CensusIndexerMem implements Serializable, Indexer {
	private static Log log = LogFactory.getLog(CensusIndexer.class);

	private String id;
	private ClusterTreeExpanded cluster;
	private final List<Descriptor> descriptors;
	private final List<String> identifiers;
	// private ArrayList<List<Double>> sigs;
	ArrayList<List<Double>> signatures = new ArrayList<List<Double>>();
	ArrayList<String> ids = new ArrayList<String>();
	private final double returnPercentage = 200.0;
	private Measure measure;
	String indexerfolder;

	public CensusIndexerMem() {
		cluster = new ClusterTreeExpanded();
		descriptors = new ArrayList<Descriptor>();
		identifiers = new ArrayList<String>();

		Properties properties;

		try {
			properties = PropertiesUtil.load();
			String location = properties.getProperty("file.indexerfolder");
			if (location != null) {
				indexerfolder = location;
				// log.debug("directory="+directory);
				File indexerfile = new File(indexerfolder);
				if (!indexerfile.exists())
					indexerfile.mkdir();

				System.out.println("Census indexer folder=" + indexerfolder);
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
		System.out.println("Setting id for Census Indexer");
		File indexerIdfile = new File(indexerfolder + "/" + id + ".txt");
		FileInputStream fis = null;
		if (indexerIdfile.exists()) {
			try {
				fis = new FileInputStream(indexerIdfile);
				ObjectInputStream ois = new ObjectInputStream(fis);

				ClusterTreeExpanded c = (ClusterTreeExpanded) ois.readObject();

				this.cluster = c;
				System.out.println("Reading Cluster");

				String[] idss = cluster.identifiers;

				for (String i : idss) {
					this.ids.add(i);
				}

				// fis.close();

			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("I am inside iOException");
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public String getId() {
		return id;
	}

	public void addDescriptor(Descriptor document, String id) {
		descriptors.add(document);
		identifiers.add(id);
		System.out.println("Adding descriptor and identifier...");
	}

	public void addDescriptor(Descriptor document) {
		descriptors.add(document);
	}

	public void build() {

		int i = 0;
		ids.clear();
		for (Descriptor d : descriptors) {
			WordspottingFeature descriptor = new WordspottingFeature(d);
			signatures.add(descriptor.asListDoubles());
			ids.add(identifiers.get(i));
			System.out.println("i=" + i + " ids=" + ids.get(i));
			i++;
		}
		// index
		cluster.build(signatures, ids);

		File indexerIdfile = new File(indexerfolder + "/" + getId() + ".txt");

		try {

			write(indexerIdfile, cluster);
			System.out.println("Index written to "
					+ indexerIdfile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<SearchResult> query(Descriptor query) {
		log.debug("CensusIndexer:query");
		measure = new EuclideanDistanceMeasure();
		WordspottingFeature d = new WordspottingFeature(query);

		List<Double> signature = d.asListDoubles();

		log.debug("Signature of Query:" + signature);
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		// log.debug("Before query to cluster");

		ArrayList<Pair<String, double[]>> results = cluster.getSimilarCluster(
				signature, returnPercentage);

		log.debug("After query to cluster: Results.size=" + results.size());

		SearchResult Result = new SearchResult();

		for (int i = 0; i < results.size(); i++) {
			try {
				Result.setProximity(getMeasure().compare(
						new DoubleArrayFeature(results.get(i).second), query));
				Result.setDocId(results.get(i).first);
				log.debug(results.get(i).first);
				searchResults.add(Result);
				Result = new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			Result.setProximity(getMeasure().compare(query, query));
			Result.setDocId("0");
			searchResults.add(Result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("Returning results to IndexResource");
		return searchResults;

	}

	public void setMeasure(Measure measure) {
		this.measure = measure;

	}

	public Measure getMeasure() {
		return measure;
	}

	public List<SearchResult> query(Descriptor query, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getIdentifiers() {
		// return identifiers;
		return ids;
	}

	@Override
	public List<SearchResult> query(Descriptor query, Measure measure) {

		log.debug("CensusIndexer:query");
		measure = new EuclideanDistanceMeasure();
		WordspottingFeature d = new WordspottingFeature(query);

		List<Double> signature = d.asListDoubles();

		// log.debug("Signature of Query:"+signature);
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		// log.debug("Before query to cluster");

		ArrayList<Pair<String, double[]>> results = cluster.getSimilarCluster(
				signature, returnPercentage);

		log.debug("After Query: Results.size=" + results.size());

		SearchResult Result = new SearchResult();

		for (int i = 0; i < results.size(); i++) {
			try {
				Result.setProximity(measure.compare(new DoubleArrayFeature(
						results.get(i).second), query));
				Result.setDocId(results.get(i).first);
				log.debug(results.get(i).first);
				searchResults.add(Result);
				Result = new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			Result.setProximity(measure.compare(query, query));
			Result.setDocId("0");
			searchResults.add(Result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("Returning results to IndexResource");
		return searchResults;
	}

	@Override
	public List<Descriptor> getDescriptors() {
		return descriptors;
	}

	private static void write(File indexerFile, ClusterTreeExpanded t)
			throws IOException {
		ObjectOutputStream oos = getOOS(indexerFile);
		oos.writeObject(t);
		oos.flush();
		oos.close();
	}

	private static ObjectOutputStream getOOS(File indexFile) throws IOException {
		return new ObjectOutputStream(new FileOutputStream(indexFile));

	}

}
