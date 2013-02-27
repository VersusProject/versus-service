package edu.illinois.ncsa.versus.store;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;

/**
 * In memory storage of comparisons.
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 *
 */
public class InMemoryComparisonProcessor implements ComparisonProcessor {

    private final static ConcurrentMap<String, Comparison> comparisons = new ConcurrentHashMap<String, Comparison>();

    @Override
    public void addComparison(Comparison comparison) {
        comparisons.put(comparison.getId(), comparison);
    }

    @Override
    public Comparison getComparison(String id) {
        return comparisons.get(id);
    }

    @Override
    public Collection<Comparison> listAll() {
        return comparisons.values();
    }

    @Override
    public void updateValue(String id, String value) {
        Comparison comparison = comparisons.get(id);
        if (comparison != null) {
            comparison.setValue(value);
        } else {
            throw new RuntimeException("Comparison " + id + " not found.");
        }
    }

    @Override
    public void setStatus(String id, ComparisonStatus status) {
        Comparison comparison = comparisons.get(id);
        if (comparison != null) {
            comparison.setStatus(status);
        } else {
            throw new RuntimeException("Comparison " + id + " not found.");
        }
    }

    @Override
    public ComparisonStatus getStatus(String id) {
        return comparisons.get(id).getStatus();
    }

    @Override
    public void setError(String id, String error) {
        Comparison comparison = comparisons.get(id);
        if(comparison != null) {
            comparison.setError(error);
        } else {
            throw new RuntimeException("Comparison " + id + " not found.");
        }
    }
}
