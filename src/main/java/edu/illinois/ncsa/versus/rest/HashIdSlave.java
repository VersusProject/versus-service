package edu.illinois.ncsa.versus.rest;

//import java.net.URI;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

public class HashIdSlave implements Serializable{
   HashMap<String,String> hashList;
	
 public HashIdSlave(){
	 hashList=new HashMap<String,String>();
 }
 
 public HashMap<String,String> gethashList(){
	 return this.hashList;
 }
 
 public void addTohashList(String id, String requrl){
	 this.hashList.put(id,requrl);
 }
 
}
