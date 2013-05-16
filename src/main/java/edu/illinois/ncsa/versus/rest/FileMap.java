package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.HashMap;

public class FileMap implements Serializable {
HashMap<String,FileObject> maps;
	
	public FileMap(){
		maps=new HashMap<String,FileObject>();
	}
	public void setMap(HashMap<String,FileObject> m){
	      this.maps=m;
	}
	public HashMap<String,FileObject> getMap(){
		return maps;
	}
	public FileObject getFileObject(String  id){
		return maps.get(id);
	}
	public void addMap(String id, FileObject fo ){
		maps.put(id,fo);
	}
}
