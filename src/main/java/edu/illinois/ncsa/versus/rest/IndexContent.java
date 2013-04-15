package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;

import edu.illinois.ncsa.versus.descriptor.Descriptor;

public class IndexContent implements Serializable{
	ArrayList<String> ids;
	ArrayList<Descriptor> descriptors;
	
	public IndexContent(){
		ids=new ArrayList<String>();
		descriptors=new ArrayList<Descriptor>();
	}
   public void setIds(ArrayList<String>ids){
	   this.ids=ids;
   }
   public ArrayList<String> getIds(){
	   return ids;
   }
   public void setDescriptors(ArrayList<Descriptor> descriptors){
	  this.descriptors=descriptors; 
   }
   public ArrayList<Descriptor> getDescriptors(){
	   return descriptors;
   }
}
