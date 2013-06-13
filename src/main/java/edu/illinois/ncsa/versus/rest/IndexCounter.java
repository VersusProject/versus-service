package edu.illinois.ncsa.versus.rest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IndexCounter {
	ConcurrentMap<String, Integer> counter= new ConcurrentHashMap<String,Integer>(); //to keep track of files uploaded

	ConcurrentMap<String, Integer> getCounter(){
		return counter;
	}
	
	void setCount(String id){
		counter.put(id, 0);
		
	}
	void increment(String id){
		counter.replace(id,counter.get(id),counter.get(id)+1);
		//counter.get(id)
	}
	void resetCount(String id){
		counter.replace(id,counter.get(id),0);
	}
	int getCount(String id){
		return counter.get(id);
	}
}
