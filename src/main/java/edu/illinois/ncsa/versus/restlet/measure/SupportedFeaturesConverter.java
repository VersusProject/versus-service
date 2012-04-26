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

import java.util.ArrayList;
import java.util.List;

import edu.illinois.ncsa.versus.restlet.StringCollectionConverter;

/**
 *
 * @author antoinev
 */
public class SupportedFeaturesConverter
        extends StringCollectionConverter<List<String>> {

    @Override
    protected String getNodeName() {
        return "feature";
    }

    @Override
    protected List<String> getNewT() {
        return new ArrayList<String>();
    }

    @Override
    public boolean canConvert(Class type) {
        return List.class.isAssignableFrom(type);
    }
}
