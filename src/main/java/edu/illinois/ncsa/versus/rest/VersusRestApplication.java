/**
 * 
 */
package edu.illinois.ncsa.versus.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupportServerResource;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupportsServerResource;

/**
 * Versus REST service application.
 * 
 * @author Luigi Marini
 * 
 */
public class VersusRestApplication extends Application {

	private final Set<Object> endpoints = new HashSet<Object>();

	public VersusRestApplication() {
		endpoints.add(new IndexResource());
		endpoints.add(new AdapterResource());
		endpoints.add(new ComparisonResource1());
		endpoints.add(new DecisionSupportResource());
		endpoints.add(new DistributionResource());
		endpoints.add(new ExtractorResource());
		endpoints.add(new FileResource());
		endpoints.add(new MeasureResource());
		endpoints.add(new ServiceResource());
		endpoints.add(new StatusResource());
		endpoints.add(new SlavesResource());
		endpoints.add(new MultiLabelDecisionSupportResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return endpoints;
	}
}
