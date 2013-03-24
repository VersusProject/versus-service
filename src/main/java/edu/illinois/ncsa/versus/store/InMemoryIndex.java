package edu.illinois.ncsa.versus.store;

import java.util.Collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.illinois.ncsa.versus.rest.Index;

/* In memory storage of index
 * 
 *
 * */

public class InMemoryIndex implements IndexProcessor {
	
	
	private final static ConcurrentMap<String, Index> indexList = new ConcurrentHashMap<String, Index>();

	@Override
	public void addIndex(Index in) {
		indexList.put(in.getId(), in);
	}


	@Override
	public Index getIndex(String id) {
			return indexList.get(id);
	}


	@Override
	public Collection<Index> listAll() {
		return indexList.values();
	}


}
