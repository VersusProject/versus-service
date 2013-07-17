package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;

public class MapObject implements Serializable{
	private String versusId;
	private String mediciId;
	private String indexId;
	private String buildStatus="false";
	
public MapObject(){
	
}

MapObject(String versusId,String mediciId,String indexId,String buildStatus){
	this.versusId=versusId;
	this.mediciId=mediciId;
	this.indexId=indexId;
	this.buildStatus=buildStatus;
  }

public String getversusId(){
	return versusId;
}

public void setversusId(String versusId){
	this.versusId=versusId;
}

public String getmediciId(){
	return mediciId;
}

public void setmediciId(String mediciId){
	this.mediciId=mediciId;
}

public String getindexId(){
	return indexId;
}

public void setindexId(String indexId ){
  this.indexId=indexId;
}

public String getbuildStatus(){
	return buildStatus;
}

public void setbuildStatus(String buildStatus){
	this.buildStatus=buildStatus;
}

}
