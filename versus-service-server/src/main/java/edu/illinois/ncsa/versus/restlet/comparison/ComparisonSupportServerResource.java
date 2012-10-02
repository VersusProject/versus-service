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
package edu.illinois.ncsa.versus.restlet.comparison;

import java.util.Map;

import org.restlet.resource.Get;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.Slave;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.restlet.VersusServerResource;

/**
 *
 * @author antoinev
 */
public class ComparisonSupportServerResource extends VersusServerResource {

    public static final String URL = "/comparisons/";

    public static final String ADAPTER_PARAMETER = "adapter";

    public static final String EXTRACTOR_PARAMETER = "extractor";

    public static final String MEASURE_PARAMETER = "measure";

    public static final String PATH_TEMPLATE = URL
            + '{' + ADAPTER_PARAMETER + '}' + '/'
            + '{' + EXTRACTOR_PARAMETER + '}' + '/'
            + '{' + MEASURE_PARAMETER + '}';

    @Get
    public Boolean retrieve() {
        Map<String, Object> attributes = getRequest().getAttributes();
        final String adapterId = (String) attributes.get(ADAPTER_PARAMETER);
        final String extractorId = (String) attributes.get(EXTRACTOR_PARAMETER);
        final String measureId = (String) attributes.get(MEASURE_PARAMETER);

        ServerApplication server = (ServerApplication) getApplication();
        CompareRegistry registry = server.getRegistry();

        if (registry.supportComparison(adapterId, extractorId, measureId)) {
            return true;
        }

        return server.getSlavesManager().querySlavesAnyTrue(
                new SlavesManager.SlaveQuery<Boolean>() {

                    @Override
                    public Boolean executeQuery(Slave slave) {
                        return slave.supportComparison(adapterId, extractorId,
                                measureId);
                    }
                });
    }

    @Get("xml")
    public String asXml() {
        XStream xstream = new XStream();
        return fillAndConvert(xstream);
    }

    @Get("json")
    public String asJson() {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        return fillAndConvert(xstream);
    }

    private String fillAndConvert(XStream xstream) {
        xstream.alias("supported", Boolean.class);
        xstream.alias("supported", boolean.class);
        return xstream.toXML(retrieve());
    }
}
