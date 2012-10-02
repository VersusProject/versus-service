package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import edu.illinois.ncsa.versus.restlet.MultiLabelDecisionSupport;


public class MultiLabelDecisionSupportServiceImpl implements MultiLabelDecisionSupportService {

	private final static ConcurrentMap<String, MultiLabelDecisionSupport> multiLabelDS = new ConcurrentHashMap<String, MultiLabelDecisionSupport>();
	
	@Override
	public void addMultiLabelDecisionSupport(MultiLabelDecisionSupport mlds) {
		multiLabelDS.put(mlds.getId(), mlds);
	}

	@Override
	public MultiLabelDecisionSupport getMultiLabelDecisionSupport(String id) {
		return multiLabelDS.get(id);
	}

	@Override
	public Collection<MultiLabelDecisionSupport> listAll() {
		return multiLabelDS.values();
	}
}