package edu.illinois.ncsa.versus.store;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import edu.illinois.ncsa.versus.restlet.PropertiesUtil;

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
			// comparison results
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
			// files
			String files = properties.getProperty("files", "disk");
			if ("disk".equals(files)) {
				bind(FileProcessor.class).to(DiskFileProcessor.class).in(
						Singleton.class);
			} else if ("mongo".equals(files)) {
				bind(FileProcessor.class).to(MongoFileProcessor.class).in(
						Singleton.class);
			}
			// distribution
			bind(DistributionService.class).to(DistributionServiceImpl.class).in(Singleton.class);
			//decision support
			bind(DecisionSupportService.class).to(DecisionSupportServiceImpl.class).in(Singleton.class);
			//mlds
			bind(MultiLabelDecisionSupportService.class).to(MultiLabelDecisionSupportServiceImpl.class).in(Singleton.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
