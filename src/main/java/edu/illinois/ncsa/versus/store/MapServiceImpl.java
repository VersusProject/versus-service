package edu.illinois.ncsa.versus.store;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.rest.IdMap;

public class MapServiceImpl implements MapService {
	
	private final MapProcessor mp;

	@Inject
	public MapServiceImpl(MapProcessor mapProcessor){
      this.mp=mapProcessor;	
    
	}
		
	@Override
	public void addMap(IdMap m) {
		mp.addMap(m);

	}

	@Override
	public IdMap getMap() {
		
		return mp.getMap();
	}

}
