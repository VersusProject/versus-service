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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import edu.illinois.ncsa.versus.restlet.NotFoundException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class AdapterHelpSha1ServerResource extends ServerResource {

    public static final String ID_PARAMETER = "id";

    public static final String PATH_TEMPLATE = "/adapters/" + '{' + ID_PARAMETER + '}' + "/helpsha1";

    @Get
    public String retrieve() {
        String id = (String) getRequest().getAttributes().get(ID_PARAMETER);
        try {
            return ((ServerApplication) getApplication()).getAdapterHelpSha1(id);
        } catch (NotFoundException ex) {
            Logger.getLogger(AdapterHelpSha1ServerResource.class.getName()).log(Level.SEVERE, null, ex);
            setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return "Adapter help " + id + " not found";
        }
    }
}
