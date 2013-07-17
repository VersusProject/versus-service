package edu.illinois.ncsa.versus.store;

import edu.illinois.ncsa.versus.rest.MapObject;

public interface MapObjectService {
	void addMapObject(MapObject map);
	MapObject getMapObject(String versusId);
	void updateBuildStatus(String indexId,String buildStatus);
	String getBuildStatus(String versusId);
	void setBuildStatus(String versusId,String buildStatus);

}
