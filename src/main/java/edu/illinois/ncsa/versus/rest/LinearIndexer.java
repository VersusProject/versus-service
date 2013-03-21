package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
//import org.eclipse.jetty.util.log.Log;

import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;


import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;
import edu.illinois.ncsa.versus.search.SearchResult;


public class LinearIndexer implements Serializable,Indexer{
private static Log log = LogFactory.getLog(LinearIndexer.class);
	
private final List<Descriptor> descriptors;
private final List<String> identifiers;

private Measure measure;
	
	

public LinearIndexer() {
		descriptors = new ArrayList<Descriptor>();
		identifiers = new ArrayList<String>();
	}
	public List<String> getIdentifiers(){
		return identifiers;
		
	}

	public void addDescriptor(Descriptor document, String id) {
		
		descriptors.add(document);
		//log.debug("Descriptor added="+document.toString());
		identifiers.add(id);
		
	}

	public void addDescriptor(Descriptor document) {
		descriptors.add(document);
		
	}

		
public List<SearchResult> query(Descriptor query,Measure measure) {
		
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


	public void setMeasure(Measure measure) {
		this.measure=measure;

	}
	public Measure getMeasure(){
		return measure;
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
		return sArray;
		
	}
	
	
	
}
