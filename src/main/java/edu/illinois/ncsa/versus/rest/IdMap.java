package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.HashMap;

public class IdMap implements Serializable{
	HashMap<String,String> maps;
	
	public IdMap(){
		maps=new HashMap<String,String>();
	}
	public void setMap(HashMap<String,String> m){
	      this.maps=m;
	}
	public HashMap<String,String> getMap(){
		return maps;
	}
	public String getMId(String vid ){
		return maps.get(vid);
	}
	public void addMap(String vid, String mid ){
		maps.put(vid,mid);
	}
	

}
