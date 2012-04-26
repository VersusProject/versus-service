/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Set;

import edu.illinois.ncsa.versus.restlet.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.restlet.adapter.AdaptersClient;
import edu.illinois.ncsa.versus.restlet.comparison.Comparison;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonClient;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorsClient;
import edu.illinois.ncsa.versus.restlet.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.restlet.measure.MeasuresClient;

/**
 * @author lmarini
 *
 */
public class Slave {

    private String url;

    public Slave(String hostRef) {
        url = hostRef;
    }

    public String getUrl() {
        return url;
    }

    public AdapterDescriptor getAdapter(String id) {
        return new AdaptersClient(url).getAdapterDescriptor(id);
    }

    public Set<String> getAdaptersId() {
        return new AdaptersClient(url).getAdapters();
    }

    public ExtractorDescriptor getExtractor(String id) {
        return new ExtractorsClient(url).getExtractorDescriptor(id);
    }

    public Set<String> getExtractorsId() {
        return new ExtractorsClient(url).getExtractors();
    }

    public MeasureDescriptor getMeasure(String id) {
        return new MeasuresClient(url).getMeasureDescriptor(id);
    }

    public Set<String> getMeasuresId() {
        return new MeasuresClient(url).getMeasures();
    }

    public Comparison getComparison(Comparison comparison) {
        comparison = new ComparisonClient(url).getComparison(comparison.getId());
        comparison.setSlave(url);
        return comparison;
    }

    public Comparison submit(Comparison comparison) {
        comparison = new ComparisonClient(url).submit(comparison);
        comparison.setSlave(url);
        return comparison;
    }

    public boolean supportComparison(Comparison comparison) {
        return supportComparison(comparison.getAdapterId(),
                comparison.getExtractorId(), comparison.getMeasureId());
    }

    public boolean supportComparison(String adapterId, String extractorId, String measureId) {
        return new ComparisonClient(url).supportComparison(adapterId, extractorId, measureId);
    }

    @Override
    public String toString() {
        return url;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Slave)) {
            return false;
        }
        final Slave other = (Slave) obj;
        return this.url.equals(other.url);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }
}
