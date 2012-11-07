package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.illinois.ncsa.versus.restlet.Slave;

public class RankSlaves extends SlavesList implements Serializable{
	
	static int nextIndex=-1;
	
	public int getNextIndex(){
		return nextIndex;
	}
	public void setNextIndex(int in){
		nextIndex=in;
	}
	/*public static List<Slave> rank(Collection<Slave> list) {
		ArrayList<Slave> arrayList = new ArrayList<Slave>(list);
		return arrayList;
	}*/

}
