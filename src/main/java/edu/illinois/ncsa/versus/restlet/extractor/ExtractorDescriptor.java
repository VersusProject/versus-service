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
package edu.illinois.ncsa.versus.restlet.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import edu.illinois.ncsa.versus.extract.Extractor;
import edu.illinois.ncsa.versus.registry.CompareRegistry;

/**
 *
 * @author antoinev
 */
@XStreamAlias("extractor")
public class ExtractorDescriptor implements Serializable {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("id")
    private String type;

    @XStreamAlias("supportedAdapters")
    @XStreamConverter(SupportedAdaptersConverter.class)
    private List<String> supportedAdapters;

    @XStreamAlias("supportedFeature")
    private String supportedFeature;

    public ExtractorDescriptor(Extractor extractor, CompareRegistry registry) {
        name = extractor.getName();
        type = extractor.getClass().getName();
        Collection<String> adapters = registry.getAvailableAdaptersIds(extractor);
        supportedAdapters = new ArrayList<String>(adapters);
        supportedFeature = extractor.getFeatureType().getName();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getSupportedAdapters() {
        return Collections.unmodifiableList(supportedAdapters);
    }

    public String getSupportedFeature() {
        return supportedFeature;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (!(anObject instanceof ExtractorDescriptor)) {
            return false;
        }
        ExtractorDescriptor other = (ExtractorDescriptor) anObject;
        return type.equals(other.type);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}
