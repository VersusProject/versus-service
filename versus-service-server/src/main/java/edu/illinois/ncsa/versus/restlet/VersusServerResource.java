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
package edu.illinois.ncsa.versus.restlet;

import java.util.Properties;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Message;
import org.restlet.engine.header.Header;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;

/**
 *
 * @author antoinev
 */
public class VersusServerResource extends ServerResource {

    private static final String HEADERS_KEY = "org.restlet.http.headers";

    private static final String ACAO_KEY = "Access-Control-Allow-Origin";

    private static final String ACAO_GET;

    private static final String ACAO_POST;

    private static final String ACAO_PUT;

    private static final String ACAO_DELETE;

    private static final String ACAO_HEAD;

    private static final String ACAO_OPTIONS;

    static {
        Properties properties;
        try {
            properties = PropertiesUtil.load();
        } catch (Exception ex) {
            Logger.getLogger(VersusServerResource.class.getName()).log(Level.WARNING,
                    "Cannot read property file. Using default REST properties.", ex);
            properties = new Properties();
        }
        ACAO_GET = properties.getProperty("rest.access-control-allow-origin.get", null);
        ACAO_POST = properties.getProperty("rest.access-control-allow-origin.post", null);
        ACAO_PUT = properties.getProperty("rest.access-control-allow-origin.put", null);
        ACAO_DELETE = properties.getProperty("rest.access-control-allow-origin.delete", null);
        ACAO_HEAD = properties.getProperty("rest.access-control-allow-origin.head", null);
        ACAO_OPTIONS = properties.getProperty("rest.access-control-allow-origin.options", null);
    }

    private static Series<Header> getMessageHeaders(Message message) {
        ConcurrentMap<String, Object> attrs = message.getAttributes();
        Series<Header> headers = (Series<Header>) attrs.get(HEADERS_KEY);
        if (headers == null) {
            headers = new Series<Header>(Header.class);
            Series<Header> prev = (Series<Header>) attrs.putIfAbsent(HEADERS_KEY, headers);
            if (prev != null) {
                headers = prev;
            }
        }
        return headers;
    }

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Representation get = super.get(variant);
        if (ACAO_GET != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_GET);
        }
        return get;
    }

    @Override
    protected Representation post(Representation entity, Variant variant) {
        Representation post = super.post(entity, variant);
        if (ACAO_POST != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_POST);
        }
        return post;
    }

    @Override
    protected Representation put(Representation representation, Variant variant) {
        Representation put = super.put(representation, variant);
        if (ACAO_PUT != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_PUT);
        }
        return put;
    }

    @Override
    protected Representation delete(Variant variant) throws ResourceException {
        Representation delete = super.delete(variant);
        if (ACAO_DELETE != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_DELETE);
        }
        return delete;
    }

    @Override
    protected Representation head(Variant variant) throws ResourceException {
        Representation head = super.head(variant);
        if (ACAO_HEAD != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_HEAD);
        }
        return head;
    }

    @Override
    protected Representation options(Variant variant) throws ResourceException {
        Representation options = super.options(variant);
        if (ACAO_OPTIONS != null) {
            getMessageHeaders(getResponse()).add(ACAO_KEY, ACAO_OPTIONS);
        }
        return options;
    }
}
