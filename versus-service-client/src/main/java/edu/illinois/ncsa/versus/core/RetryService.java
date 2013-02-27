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
package edu.illinois.ncsa.versus.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Workaround service for 
 * https://github.com/restlet/restlet-framework-java/issues/669
 *
 * @author antoinev
 */
public class RetryService<T> {

    private final Callable<T> callable;
    
    private final int maxRetry;
    
    private final int timeout;

    /**
     * 
     * @param callable
     * @param timeout Timeout in seconds
     * @param maxRetry 
     */
    public RetryService(Callable<T> callable, int timeout, int maxRetry) {
        this.callable = callable;
        this.timeout = timeout;
        this.maxRetry = maxRetry;
    }

    public T run() {
        int retry = 0;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try {
            do {
                Future<T> future = executor.submit(callable);
                try {
                    return future.get(timeout, TimeUnit.SECONDS);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (CancellationException e) {
                    Logger.getLogger(RetryService.class.getName()).log(
                            Level.INFO, "Computation canceled.", e);
                } catch (InterruptedException e) {
                    Logger.getLogger(RetryService.class.getName()).log(
                            Level.INFO, "Computation interrupted.", e);
                } catch (TimeoutException e) {
                    Logger.getLogger(RetryService.class.getName()).log(
                            Level.INFO, "Computation timeout.", e);
                } finally {
                    future.cancel(true);
                }

                retry++;
            } while (retry < maxRetry);
        } finally {
            executor.shutdownNow();
        }
        throw new RuntimeException("The request failed after " + retry + " retry.");
    }
}
