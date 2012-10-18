/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An executor provider used to submit queries either to slaves or to this node
 * It should not be used to do intensive computations like pairwise comparisons
 *
 * @author antoinev
 */
public class QueryExecutorProvider {

    private static final ExecutorService executor =
            Executors.newCachedThreadPool();

    private QueryExecutorProvider() {
    }
    
    public static ExecutorService getExecutor() {
        return executor;
    }
}
