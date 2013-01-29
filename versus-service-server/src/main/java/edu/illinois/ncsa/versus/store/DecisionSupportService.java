package edu.illinois.ncsa.versus.store;

import java.io.InputStream;
import java.util.Collection;

import edu.illinois.ncsa.versus.restlet.DecisionSupport;

/**
 * Handles the manipulation of distributions in the repository.
 * 
 * @author Devin Bonnie
 * 
 */
public interface DecisionSupportService {

	void addDecisionSupport(DecisionSupport ds);

	DecisionSupport getDecisionSupport(String id);
	
	Collection<DecisionSupport> listAll();
}
