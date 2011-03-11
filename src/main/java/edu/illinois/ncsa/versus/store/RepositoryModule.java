package edu.illinois.ncsa.versus.store;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;

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
			Properties properties = PropertiesUtil.load();
			// comparison results
			String repository = properties.getProperty("repository", "mem");
			if ("mem".equals(repository)) {
				bind(ComparisonProcessor.class).to(
						InMemoryComparisonProcessor.class);
			} else if ("mysql".equals(repository)) {
				bind(ComparisonProcessor.class).to(
						JDBCComparisonProcessor.class);
			}
			// files
			bind(FileProcessor.class).to(DiskFileProcessor.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
