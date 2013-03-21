/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.descriptor.Feature;
import edu.illinois.ncsa.versus.descriptor.impl.DoubleArrayFeature;

/**
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class WordspottingFeature implements Feature {

	private final double[] signature;

	public WordspottingFeature(double[] signature) {
		this.signature = signature;
	}
    
	public WordspottingFeature(Descriptor d){
		double[] des= ((DoubleArrayFeature) d).getValues();
	
		this.signature=des;
	}
	public String getName() {
		return "Wordspotting";
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Double> asListDoubles() {
		List<Double> doubles = new ArrayList<Double>();
		for(int i=0;i<signature.length;i++){
		    doubles.add(new Double(signature[i]));
		}
		return doubles;
	}

	public double[] getSignature() {
		return signature;
	}

}
