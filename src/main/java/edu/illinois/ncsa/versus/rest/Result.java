package edu.illinois.ncsa.versus.rest;

public class Result {
	String docID;
	Double proximity;
Result(){
	
}	
public void set(String docID,Double proximity){
	this.docID=docID;
	this.proximity=proximity;
}

}
