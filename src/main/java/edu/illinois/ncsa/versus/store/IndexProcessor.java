package edu.illinois.ncsa.versus.store;

import java.util.Collection;

import edu.illinois.ncsa.versus.rest.Index;

public interface IndexProcessor {
	public void addIndex(Index in) ;
	public Index getIndex(String id);

    public Collection<Index> listAll(); 

	

}
