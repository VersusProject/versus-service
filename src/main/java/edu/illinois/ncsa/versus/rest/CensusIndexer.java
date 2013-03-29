package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.eclipse.jetty.util.log.Log;

import kgm.utility.Pair;

import edu.illinois.ncsa.clustering.ClusterTreeExpanded;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.measure.impl.EuclideanDistanceMeasure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;

/**
 * Index census forms.
 * 
 * @author Luigi Marini
 * 
 */
public class CensusIndexer implements Serializable, Indexer {

	private static Log log = LogFactory.getLog(CensusIndexer.class);

	private String id;
	private final ClusterTreeExpanded cluster;
	private final List<Descriptor> descriptors;
	private final List<String> identifiers;
	private final double returnPercentage = 200.0;
	private Measure measure;

	public CensusIndexer() {
		cluster = new ClusterTreeExpanded();
		descriptors = new ArrayList<Descriptor>();
		identifiers = new ArrayList<String>();

	}

	public void addDescriptor(Descriptor document, String id) {
		descriptors.add(document);
		identifiers.add(id);
	}

	public void addDescriptor(Descriptor document) {
		descriptors.add(document);
	}

	public void build() {
		ArrayList<List<Double>> signatures = new ArrayList<List<Double>>();
		ArrayList<String> ids = new ArrayList<String>();
		// build signatures and ids
		int id = 0;
		for (Descriptor d : descriptors) {
			WordspottingFeature descriptor = new WordspottingFeature(d);
			signatures.add(descriptor.asListDoubles());
			// ids.add(String.valueOf(id));
			ids.add(identifiers.get(id));
			id++;
		}
		// index
		cluster.build(signatures, ids);
	}

	public List<SearchResult> query(Descriptor query) {
		log.debug("CensusIndexer:query");
		measure = new EuclideanDistanceMeasure();
		WordspottingFeature d = new WordspottingFeature(query);

		List<Double> signature = d.asListDoubles();

		log.debug("Signature of Query:" + signature);
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		log.debug("Before query to cluster");

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

	/*
	 * public List<Double> query(Descriptor query,Measure measure){ return null;
	 * }
	 */

	@Override
	public List<String> getIdentifiers() {
		return identifiers;
	}

	@Override
	public List<SearchResult> query(Descriptor query, Measure measure) {

		log.debug("CensusIndexer:query");
		measure = new EuclideanDistanceMeasure();
		WordspottingFeature d = new WordspottingFeature(query);

		List<Double> signature = d.asListDoubles();

		log.debug("Signature of Query:" + signature);
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		log.debug("Before query to cluster");

		ArrayList<Pair<String, double[]>> results = cluster.getSimilarCluster(
				signature, returnPercentage);

		log.debug("After query to cluster: Results.size=" + results.size());

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

	@Override
	public void setId(String id) {
		this.id = id;

	}

	@Override
	public String getId() {
		return id;
	}

}
