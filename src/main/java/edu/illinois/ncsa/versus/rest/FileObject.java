package edu.illinois.ncsa.versus.rest;

public class FileObject {
	String name="";
	String path="";
	String vid=""; //versus filename
	
  FileObject(){}
  
  String getName(){
	  return name;
  }
  String getPath(){
	  return path;
  }
  String getVid(){
	  return vid;
  }
  void setName(String name){
	  this.name=name;
  }
  void setPath(String path){
	  this.path=path;
  }
  void setVid(String vid){
	  this.vid=vid;
  }
}
