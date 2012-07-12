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
package gov.nist.itl.ssd.sampling;

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
@XStreamAlias("sampler")
public class SamplerDescriptor implements Serializable {

    @XStreamAlias("name")
    private final String name;

    @XStreamAlias("id")
    private final String id;

    @XStreamAlias("category")
    private final String category;

    @XStreamAlias("supportedIndividuals")
    @XStreamConverter(SupportedIndividualsConverter.class)
    private final List<String> supportedIndividuals;

    @XStreamAlias("hasHelp")
    private final boolean hasHelp;

    public SamplerDescriptor(String name, String id, String category,
            Collection<String> supportedIndividuals, boolean hasHelp) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.supportedIndividuals = new ArrayList<String>(supportedIndividuals);
        this.hasHelp = hasHelp;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getSupportedIndividuals() {
        return Collections.unmodifiableList(supportedIndividuals);
    }

    public boolean hasHelp() {
        return hasHelp;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (!(anObject instanceof SamplerDescriptor)) {
            return false;
        }
        SamplerDescriptor other = (SamplerDescriptor) anObject;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
