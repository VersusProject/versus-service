package edu.illinois.ncsa.versus.store;

import java.io.File;
import java.util.Collection;

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.rest.Index;
import edu.illinois.ncsa.versus.restlet.DecisionSupport;
import edu.illinois.ncsa.versus.search.Indexer;

public interface IndexService {
	
	public void addIndex(Index in) ;
	public Index getIndex(String id);

    public Collection<Index> listAll(); 

	
	
    
}
