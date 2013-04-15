package edu.illinois.ncsa.versus.store;

import edu.illinois.ncsa.versus.search.Indexer;

public interface IndexerService {
	 /*Indexer*/
	    public void addIndexer(Indexer in);
	    public Indexer getIndexer(String id);
       // public IndexerProcessor getIndexerProcessor();
        public boolean existIndexer(String id);
}
