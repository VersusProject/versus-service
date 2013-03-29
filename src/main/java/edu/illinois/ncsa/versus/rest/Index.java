package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.HashMap;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.measure.Measure;
import edu.illinois.ncsa.versus.search.Indexer;

public class Index implements Serializable {
	
	private String id;
	

	
	private String adapterId;
	private String extractorId;
	private String measureId;
	private String indexerId;
	
	/*private Adapter adapter;
	private Extractor extractor;
	private Measure measure;
	private Indexer indexer;*/
	
	//private HashMap<String,Descriptor> indexHash;
	
	public Index(){
	//indexHash=new HashMap<String,Descriptor>();
	}
	
	/*public HashMap<String,Descriptor> getindexHash(){
		return indexHash;
		
	}*/
	
	/*public void addToIndex(String id,Descriptor d){
		indexHash.put(id, d);
		
	}*/
	
	/*public Descriptor getDescriptor(String id){
		return indexHash.get(id);
	}*/
	
	public String getId(){
		return id;
		
	}
	
	public String getAdapterId(){
		return adapterId;
	}
	public String getMeasureId(){
		return measureId;
	}
	public String getExtractorId(){
		return extractorId;
	}
	
	public String getIndexerId(){
		return indexerId;
	}
	
	/*public Adapter getAdapter(){
		return adapter;
	}
	
	
	
	public Extractor getExtractor(){
		return extractor;
	}
	
	
	
	public Measure getMeasure(){
		return measure;
	}
	
	public Indexer getIndexer(){
		return indexer;
	}*/
	
	
	
	public void setId(String id){
		this.id=id;
		
	}
	
    public void setAdapterId(String adapterID){
		this.adapterId=adapterID;
	}
 public void setExtractorId(String extractorID){
		
		this.extractorId=extractorID;
		
	}
 public void setMeasureId(String measureID){
		this.measureId=measureID;
		
}
 public void setIndexerId(String indexerID){
		this.indexerId=indexerID;
}
	
    
   /* public void setAdapter(String adapterID){
    	
    	try {
			this.adapter = (Adapter) Class.forName(adapterID).newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    }
	
  
   
   public void setExtractor(String extractorID){
	   try {
			this.extractor = (Extractor) Class.forName(extractorId).newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	   
   }
	
	
	public void setMeasure(String measureID){
		
		try {
			this.measure = (Measure) Class.forName(measureId).newInstance();
		} catch (InstantiationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalAccessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (ClassNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
	}
	


public void setIndexer(String indexerID){
	try {
		this.indexer = (Indexer) Class.forName(indexerID).newInstance();
	} catch (InstantiationException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	} catch (IllegalAccessException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	} catch (ClassNotFoundException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
	
}
public void setIndexer(Indexer indexer){
	this.indexer=indexer;
}*/
	
}