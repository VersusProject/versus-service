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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 *
 * @author antoinev
 */
public class SlavesManager {

    public interface SlaveQuery<T> {

        T executeQuery(Slave slave);
    }
    private final HashMap<String, Slave> slaves = new HashMap<String, Slave>();

    private static final int NUM_CORES =
            Runtime.getRuntime().availableProcessors();
    
    private static final int NUM_THREADS = NUM_CORES * 10;

    private static final Logger logger =
            Logger.getLogger(SlavesManager.class.getName());

    public Slave getSlave(String url) {
        return slaves.get(url);
    }

    public void addSlave(String url) {
        if (!slaves.containsKey(url)) {
            slaves.put(url, new Slave(url));
        }
    }

    public int getSlavesNumber() {
        return slaves.size();
    }

    public Set<Slave> getSlaves() {
        return Collections.unmodifiableSet(new HashSet(slaves.values()));
    }

    private <T> HashMap<Future<T>, Slave> submitQueryToSlaves(
            ExecutorService executor, final SlaveQuery<T> query) {
        HashMap<Future<T>, Slave> futures =
                new HashMap<Future<T>, Slave>(getSlavesNumber());
        for (final Slave slave : slaves.values()) {
            Future<T> future = executor.submit(new Callable<T>() {

                @Override
                public T call() throws Exception {
                    return query.executeQuery(slave);
                }
            });
            futures.put(future, slave);
        }
        return futures;
    }

    private <T> void manageSlaveException(Exception ex, Future<T> future,
            HashMap<Future<T>, Slave> futures) {
        Slave slave = futures.get(future);
        Throwable cause = ex.getCause();
        if (cause instanceof ResourceException) {
            ResourceException re = (ResourceException) cause;
            if (Status.CONNECTOR_ERROR_COMMUNICATION.equals(
                    re.getStatus())) {
                logger.log(Level.INFO,
                        "Unregistering unresponsive slave {0}", slave);
                slaves.remove(slave.getUrl());
                return;
            }
        }
        logger.log(Level.WARNING, "Error while querying slave " + slave,
                ex);
    }

    /**
     * Query all the slaves in parallel and return the ones which verify the
     * condition
     *
     * @param condition The condition a slave must match to be selected
     * @return the list of slaves matching the condition
     */
    public Set<Slave> getSlaves(final SlaveQuery<Boolean> condition) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        HashMap<Future<Boolean>, Slave> futures =
                submitQueryToSlaves(executor, condition);

        HashSet<Slave> result = new HashSet<Slave>(getSlavesNumber());
        for (Future<Boolean> f : futures.keySet()) {
            try {
                if (f.get()) {
                    Slave slave = futures.get(f);
                    result.add(slave);
                }
            } catch (Exception ex) {
                manageSlaveException(ex, f, futures);
            }
        }
        executor.shutdown();
        return result;
    }

    /**
     * Query all the slaves in parallel and return true if any of the slave
     * reply true to the query, false otherwise
     *
     * @param query The query
     * @return true if any of the slave reply true to the query, false otherwise
     */
    public Boolean querySlavesAnyTrue(final SlaveQuery<Boolean> query) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        HashMap<Future<Boolean>, Slave> futures =
                submitQueryToSlaves(executor, query);

        Boolean result = false;
        for (Future<Boolean> f : futures.keySet()) {
            try {
                if (result == false) {
                    result = f.get();
                } else {
                    f.cancel(false);
                }
            } catch (Exception ex) {
                manageSlaveException(ex, f, futures);
            }
        }
        executor.shutdown();
        return result;
    }

    /**
     * Query all the slaves in parallel and return the first not null result or
     * null if all the results are null
     *
     * @param <T> The type returned by the query
     * @param query The query
     * @return The first not null result or null if all the results are null
     */
    public <T> T querySlavesFirstNotNull(final SlaveQuery<T> query) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        HashMap<Future<T>, Slave> futures =
                submitQueryToSlaves(executor, query);

        T result = null;
        for (Future<T> f : futures.keySet()) {
            try {
                if (result == null) {
                    result = f.get();
                } else {
                    f.cancel(false);
                }
            } catch (Exception ex) {
                manageSlaveException(ex, f, futures);
            }
        }
        executor.shutdown();
        return result;
    }

    /**
     * Query all the slaves in parallel and return a collection containing the
     * result on each slave
     *
     * @param <T> The type returned by the query
     * @param query The query
     * @return A collection containing the result on each slave
     */
    public <T> Collection<T> querySlaves(final SlaveQuery<T> query) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        HashMap<Future<T>, Slave> futures =
                submitQueryToSlaves(executor, query);

        final ArrayList<T> result = new ArrayList<T>(getSlavesNumber());
        for (Future<T> f : futures.keySet()) {
            try {
                result.add(f.get());
            } catch (Exception ex) {
                manageSlaveException(ex, f, futures);
            }
        }
        executor.shutdown();
        return result;
    }
}
