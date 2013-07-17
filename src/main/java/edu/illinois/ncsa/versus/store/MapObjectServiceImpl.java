package edu.illinois.ncsa.versus.store;

import com.google.inject.Inject;

import edu.illinois.ncsa.versus.rest.MapObject;

public class MapObjectServiceImpl implements MapObjectService {

	private final MapObjectProcessor mp;

	@Inject
	public MapObjectServiceImpl(MapObjectProcessor mp){
      this.mp=mp;	
    
	}
	
	@Override
	public void addMapObject(MapObject map) {
		mp.addMapObject(map);

	}

	@Override
	public MapObject getMapObject(String versusId) {
		
		return mp.getMapObject(versusId);
	}

	@Override
	public void updateBuildStatus(String indexId, String buildStatus) {
		mp.updateBuildStatus(indexId, buildStatus);

	}

	@Override
	public String getBuildStatus(String versusId) {
		return mp.getBuildStatus(versusId);
	}

	@Override
	public void setBuildStatus(String versusId, String buildStatus) {
		mp.setBuildStatus(versusId, buildStatus);

	}

}
