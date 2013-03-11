package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.ncsa.versus.restlet.*;

public class SlavesList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static List<Slave> slaves;
	
	public SlavesList(){
		slaves=new ArrayList<Slave>();
	}
	
	public  static List<Slave> getSlaves(){
		return slaves;
	}
    public void addSlave(String url){
    	if(!slaves.contains(url)){
    		slaves.add(new Slave(url));
    	}
    }
}
