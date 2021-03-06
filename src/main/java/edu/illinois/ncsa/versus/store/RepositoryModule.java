package edu.illinois.ncsa.versus.store;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import edu.illinois.ncsa.versus.rest.LinearIndexer;
import edu.illinois.ncsa.versus.restlet.PropertiesUtil;
import edu.illinois.ncsa.versus.search.Indexer;

/**
 * Guice wiring for repository.
 * 
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 * 
 */
public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		try {

			bind(ComparisonService.class).to(ComparisonServiceImpl.class).in(
					Singleton.class);

			Properties properties = PropertiesUtil.load();

			// comparisons
			String repository = properties.getProperty("repository", "mem");

			if ("mem".equals(repository)) {
				bind(ComparisonProcessor.class).to(
						InMemoryComparisonProcessor.class).in(Singleton.class);

			} else if ("mysql".equals(repository)) {
				bind(ComparisonProcessor.class).to(
						JDBCComparisonProcessor.class).in(Singleton.class);
			} else if ("mongo".equals(repository)) {
				bind(ComparisonProcessor.class).to(
						MongoComparisonProcessor.class).in(Singleton.class);
			}
			
			// Storing files in disk or Mongo
			String files = properties.getProperty("files", "disk");

			if ("disk".equals(files)) {
				bind(FileProcessor.class).to(DiskFileProcessor.class).in(
						Singleton.class);
			} else if ("mongo".equals(files)) {
				bind(FileProcessor.class).to(MongoFileProcessor.class).in(
						Singleton.class);
			}

			// storing Index in  memory or disk
			bind(IndexService.class).to(IndexServiceImpl.class).in(
					Singleton.class);

			String index = properties.getProperty("index", "memIndex");

			if ("memIndex".equals(index)) {
				bind(IndexProcessor.class).to(InMemoryIndex.class).in(
						Singleton.class);
			} else if ("diskIndex".equals(index)) {
				bind(IndexProcessor.class).to(DiskIndexProcessor.class).in(
						Singleton.class);
			}

			//storing map object in mysql
			
			bind(MapObjectService.class).to(MapObjectServiceImpl.class).in(
					Singleton.class);
			String maps=properties.getProperty("maps","mysql");
			if("mysql".equals(maps)){
				bind(MapObjectProcessor.class).to(JDBCMapObjectProcessor.class).in(Singleton.class);
			}
			
			/*bind(MapService.class).to(MapServiceImpl.class).in(Singleton.class);
			String map=properties.getProperty("map", "diskMap");
			bind(MapProcessor.class).to(DiskMapProcessor.class).in(
					Singleton.class);*/
			
			
			
			
			// indexer
			bind(IndexerService.class).to(IndexerServiceImpl.class).in(
					Singleton.class); // how to bind it to census Indexer also
			
			/* bind(IndexerProcessor.class).to(LinearIndexer.class).in(Singleton.class);
 			 String indexer=properties.getProperty("indexer","memLinear");
            */
			
			// Needs to be changed : distribution
			bind(DistributionService.class).to(DistributionServiceImpl.class)
					.in(Singleton.class);
			// Needs to be changed: decision support
			bind(DecisionSupportService.class).to(
					DecisionSupportServiceImpl.class).in(Singleton.class);
			//Needs to be changed: mlds
			bind(MultiLabelDecisionSupportService.class).to(
					MultiLabelDecisionSupportServiceImpl.class).in(
					Singleton.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
