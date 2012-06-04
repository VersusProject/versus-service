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
package edu.illinois.ncsa.versus.core.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 *
 * @author antoinev
 */
@XStreamAlias("extractor")
public class ExtractorDescriptor implements Serializable {

    @XStreamAlias("name")
    private final String name;

    @XStreamAlias("id")
    private final String type;

    @XStreamAlias("category")
    private final String category;

    @XStreamAlias("supportedAdapters")
    @XStreamConverter(SupportedAdaptersConverter.class)
    private final List<String> supportedAdapters;

    @XStreamAlias("supportedFeature")
    private final String supportedFeature;

    @XStreamAlias("hasHelp")
    private final boolean hasHelp;

    public ExtractorDescriptor(String name, String id, String category,
            Collection<String> supportedAdapters, String supportedFeature,
            boolean hasHelp) {
        this.name = name;
        this.type = id;
        this.category = category;
        this.supportedAdapters = new ArrayList<String>(supportedAdapters);
        this.supportedFeature = supportedFeature;
        this.hasHelp = hasHelp;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getSupportedAdapters() {
        return Collections.unmodifiableList(supportedAdapters);
    }

    public String getSupportedFeature() {
        return supportedFeature;
    }

    public boolean hasHelp() {
        return hasHelp;
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
