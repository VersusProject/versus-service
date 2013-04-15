package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;
import edu.illinois.ncsa.versus.store.IndexerProcessor;

@SuppressWarnings("serial")
public class LinearIndexerMem implements Indexer, Serializable {

	private static Log log = LogFactory.getLog(LinearIndexerMem.class);
	
	private final List<Descriptor> descriptors;
	private final List<String> identifiers;

	private String id=null;

	private Measure measure;

	public LinearIndexerMem(){
		descriptors = new ArrayList<Descriptor>();
		identifiers = new ArrayList<String>();
	}
	
	@Override
	public void setId(String id) {
		this.id=id;

	}

	@Override
	public String getId() {
		return id;
	}
	public List<String> getIdentifiers(){
		return identifiers;
		
	}

 @Override
 public List<Descriptor> getDescriptors() {
	return descriptors;
}
 
 @Override
	public Measure getMeasure() {
		
		return measure;
	}
 @Override
	public void setMeasure(Measure measure) {
		this.measure=measure;
	}

	
	@Override
	public void addDescriptor(Descriptor document) {
		

	}

	@Override
	public void addDescriptor(Descriptor document, String id) {
		descriptors.add(document);
		identifiers.add(id);

	}

	@Override
	public void build() {
		
	}

	@Override
	public List<SearchResult> query(Descriptor query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SearchResult> query(Descriptor query, Measure measure) {
		
		log.debug("query: Linear Indexer ");
		 	
	  List<SearchResult> sArray=new ArrayList<SearchResult>();
	  	SearchResult result=new SearchResult();
		for(int i=0;i<descriptors.size();i++){
			   try {
				 result.setProximity(measure.compare(descriptors.get(i), query));
				 result.setDocId(identifiers.get(i));
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
		return sArray;
	}

	@Override
	public List<SearchResult> query(Descriptor query, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	

	
}
