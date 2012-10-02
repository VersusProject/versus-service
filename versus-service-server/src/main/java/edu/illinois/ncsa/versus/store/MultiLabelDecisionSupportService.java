package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport;

/**
 * Handles the manipulation of mlds in the repository.
 * 
 * @author Devin Bonnie
 * 
 */
public interface MultiLabelDecisionSupportService {

	void addMultiLabelDecisionSupport(MultiLabelDecisionSupport mlds);

	MultiLabelDecisionSupport getMultiLabelDecisionSupport(String id);
		
	Collection<MultiLabelDecisionSupport> listAll();
}