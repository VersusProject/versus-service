package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
//import org.eclipse.jetty.util.log.Log;

import org.apache.commons.logging.Log;

import com.google.inject.Inject;
//import org.apache.commons.logging.LogFactory;


import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;
//import edu.illinois.ncsa.versus.store.IndexProcessor;
import edu.illinois.ncsa.versus.store.IndexerProcessor;


public class LinearIndexer implements Serializable,Indexer{
private static Log log = LogFactory.getLog(LinearIndexer.class);
	
private final List<Descriptor> descriptors;
private final List<String> identifiers;

private String id=null;

private Measure measure;

IndexerProcessor in;

@Inject
public LinearIndexer(IndexerProcessor in) {
	
		descriptors = new ArrayList<Descriptor>();
		identifiers = new ArrayList<String>();
		this.in=in;
	}
/*public LinearIndexer() {
	
	descriptors = new ArrayList<Descriptor>();
	identifiers = new ArrayList<String>();
	
}*/

   public String getId(){
	return id;
}

public void setId(String id){
	this.id=id;
}

public List<String> getIdentifiers(){
		return identifiers;
		
	}

@Override
public List<Descriptor> getDescriptors() {
	return descriptors;
}

	
	
	public void addDescriptor(Descriptor document, String id) {
		//descriptors.add(document);
		//identifiers.add(id);
		in.addDescriptor(getId(), document, id);
		
	}

	public void addDescriptor(Descriptor document) {
		//descriptors.add(document);
		in.addDescriptor(getId(), document);
		}

		
public List<SearchResult> query(Descriptor query,Measure measure) {
		
	    return in.query(getId(), query, measure);
		/*log.debug("query: Linear Indexer ");
		   	    
	  	
	  	//return indexerProcessor.query(query, measure);
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
		return sArray;*/
	}


	public void setMeasure(Measure measure) {
		this.measure=measure;

	}
	public Measure getMeasure(){
		return measure;
		//return null;
	}

	public List<SearchResult> query(Descriptor query, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void build() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SearchResult> query(Descriptor query) {
		return in.query(getId(),query);
		
		//return indexerProcessor.query(query);
       /*
		log.debug("query: Linear Indexer ");
		   	    
	  	List<SearchResult> sArray=new ArrayList<SearchResult>();
	  	SearchResult result=new SearchResult();
		
		for(int i=0;i<descriptors.size();i++){
			   try {
				 result.setProximity(getMeasure().compare(descriptors.get(i), query));
				 result.setDocId(identifiers.get(i));
				sArray.add(result);
				result=new SearchResult();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			result.setProximity(getMeasure().compare(query, query));
			 result.setDocId("0");
			sArray.add(result);
		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		return sArray;*/
		
	}

	

	
		
}
