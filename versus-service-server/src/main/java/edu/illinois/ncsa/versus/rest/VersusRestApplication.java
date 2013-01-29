/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Versus REST service application.
 * 
 * @author Luigi Marini
 * 
 */
public class VersusRestApplication extends Application {

	private final Set<Object> endpoints = new HashSet<Object>();

	public VersusRestApplication() {
		endpoints.add(new AdapterResource());
		endpoints.add(new ComparisonResource());
		endpoints.add(new DecisionSupportResource());
		endpoints.add(new DistributionResource());
		endpoints.add(new ExtractorResource());
		endpoints.add(new FileResource());
		endpoints.add(new MeasureResource());
		endpoints.add(new ServiceResource());
		endpoints.add(new StatusResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return endpoints;
	}
}
