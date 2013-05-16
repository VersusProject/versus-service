package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.HashMap;

import edu.illinois.ncsa.versus.rest.IdMap;


public interface MapProcessor {
	public void addMap(IdMap m) ;
	public IdMap getMap();
}
