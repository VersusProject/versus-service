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
package edu.illinois.ncsa.versus.restlet.adapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import edu.illinois.ncsa.versus.adapter.Adapter;

/**
 *
 * @author antoinev
 */
@XStreamAlias("adapter")
public class AdapterDescriptor implements Serializable {

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("id")
    private String id;

    @XStreamAlias("supportedTypes")
    @XStreamConverter(SupportedMediaConverter.class)
    private List<String> supportedMedia;

    public AdapterDescriptor(Adapter adapter) {
        name = adapter.getName();
        id = adapter.getClass().getName();
        supportedMedia = adapter.getSupportedMediaTypes();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getSupportedMediaTypes() {
        return Collections.unmodifiableList(supportedMedia);
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (!(anObject instanceof AdapterDescriptor)) {
            return false;
        }
        AdapterDescriptor other = (AdapterDescriptor) anObject;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
