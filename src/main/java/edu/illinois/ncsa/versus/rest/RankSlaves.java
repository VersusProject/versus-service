package edu.illinois.ncsa.versus.rest;

import java.io.Serializable;


//import edu.illinois.ncsa.versus.restlet.Slave;


@SuppressWarnings("serial")
public class RankSlaves extends SlavesList implements Serializable{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	 int nextIndex=-1;
	
	public synchronized int getNextIndex(){
		return nextIndex;
	}
	public synchronized void setNextIndex(int size){
		if (nextIndex < 0) {
			nextIndex = 0;
		} else {
			if (nextIndex == (slaves.size() - 1)) {
				nextIndex = 0;
			} else
				nextIndex++;
		}

		//nextIndex=in;
	}
	/*public static List<Slave> rank(Collection<Slave> list) {
		ArrayList<Slave> arrayList = new ArrayList<Slave>(list);
		return arrayList;
	}*/

}
