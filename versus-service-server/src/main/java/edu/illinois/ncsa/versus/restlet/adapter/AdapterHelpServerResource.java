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

import java.io.InputStream;

import edu.illinois.ncsa.versus.restlet.HelpServerResource;
import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class AdapterHelpServerResource extends HelpServerResource {

    public static final String PATH_TEMPLATE = "/adapters/" + '{' + ID_PARAMETER + '}' + "/help";

    @Override
    protected InputStream getZippedHelpStream(String id) throws NotFoundException {
        return ((ServerApplication) getApplication()).getAdapterZippedHelp(id);
    }

    @Override
    protected String getType() {
        return "Adapter";
    }
}
