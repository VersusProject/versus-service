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

import edu.illinois.ncsa.versus.core.adapter.SupportedMediaConverter;

/**
 *
 * @author antoinev
 */
@XStreamAlias("individual")
public class IndividualDescriptor implements Serializable {

    @XStreamAlias("name")
    private final String name;

    @XStreamAlias("id")
    private final String id;

    @XStreamAlias("category")
    private final String category;

    @XStreamAlias("supportedTypes")
    @XStreamConverter(SupportedMediaConverter.class)
    private final List<String> supportedMedia;

    @XStreamAlias("hasHelp")
    private final boolean hasHelp;

    public IndividualDescriptor(String name, String id, String category,
            Collection<String> supportedMedia, boolean hasHelp) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.supportedMedia = new ArrayList<String>(supportedMedia);
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

    public List<String> getSupportedMediaTypes() {
        return Collections.unmodifiableList(supportedMedia);
    }

    public boolean hasHelp() {
        return hasHelp;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (!(anObject instanceof IndividualDescriptor)) {
            return false;
        }
        IndividualDescriptor other = (IndividualDescriptor) anObject;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
