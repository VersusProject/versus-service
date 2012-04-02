/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import edu.illinois.ncsa.versus.restlet.adapter.AdapterClient;
import edu.illinois.ncsa.versus.restlet.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.restlet.adapter.AdaptersClient;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorClient;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.restlet.extractor.ExtractorsClient;
import edu.illinois.ncsa.versus.restlet.measure.MeasureClient;
import edu.illinois.ncsa.versus.restlet.measure.MeasureDescriptor;
import edu.illinois.ncsa.versus.restlet.measure.MeasuresClient;

/**
 * @author lmarini
 *
 */
@XStreamAlias("slave")
public class Slave {

    @XStreamAlias("url")
    private String url;

    public Slave(String hostRef) {
        url = hostRef;
    }

    public String getUrl() {
        return url;
    }

    AdapterDescriptor getAdapter(String id) {
        return new AdapterClient(url).getAdapterDescriptor(id);
    }

    Set<AdapterDescriptor> getAdapters() {
        return new AdaptersClient(url).getAdapters();
    }

    ExtractorDescriptor getExtractor(String id) {
        return new ExtractorClient(url).getExtractorDescriptor(id);
    }

    Set<ExtractorDescriptor> getExtractors() {
        return new ExtractorsClient(url).getExtractors();
    }

    MeasureDescriptor getMeasure(String id) {
        return new MeasureClient(url).getMeasureDescriptor(id);
    }

    Set<MeasureDescriptor> getMeasures() {
        return new MeasuresClient(url).getMeasures();
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
