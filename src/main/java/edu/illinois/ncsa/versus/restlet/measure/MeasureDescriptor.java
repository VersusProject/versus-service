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
package edu.illinois.ncsa.versus.restlet.measure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import edu.illinois.ncsa.versus.descriptor.Descriptor;
import edu.illinois.ncsa.versus.measure.Measure;

/**
 *
 * @author antoinev
 */
@XStreamAlias("measure")
public class MeasureDescriptor implements Serializable {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("id")
    private String type;

    @XStreamAlias("supportedFeatures")
    @XStreamConverter(SupportedFeaturesConverter.class)
    private List<String> supportedFeatures;

    public MeasureDescriptor(Measure measure) {
        name = measure.getName();
        type = measure.getClass().getName();
        Set<Class<? extends Descriptor>> supportedFeaturesTypes =
                measure.supportedFeaturesTypes();
        supportedFeatures = new ArrayList<String>(supportedFeaturesTypes.size());
        for (Class<? extends Descriptor> feature : supportedFeaturesTypes) {
            supportedFeatures.add(feature.getName());
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getSupportedFeatures() {
        return Collections.unmodifiableList(supportedFeatures);
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (!(anObject instanceof MeasureDescriptor)) {
            return false;
        }
        MeasureDescriptor other = (MeasureDescriptor) anObject;
        return type.equals(other.type);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }
}
