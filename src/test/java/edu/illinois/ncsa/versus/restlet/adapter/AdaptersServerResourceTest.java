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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;

import org.apache.mina.util.AvailablePortFinder;
import org.restlet.Component;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class AdaptersServerResourceTest {

    private static Component component;

    private static int port;

    public AdaptersServerResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        port = AvailablePortFinder.getNextAvailable(8080);

        // Create a new Component.  
        component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        component.getDefaultHost().attach("/versus",
                new ServerApplication(port, "/versus"));
        component.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        component.stop();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() throws IOException {
        AdaptersClient adaptersClient = new AdaptersClient("http://127.0.0.1:" + port + "/versus");
        HashSet<AdapterDescriptor> adapters = adaptersClient.getAdapters();
        assertNotNull(adapters);
        assertFalse(adapters.isEmpty());

        ClientResource clientResource = new ClientResource("http://127.0.0.1:" + port + "/versus/adapters");

        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(AdapterDescriptor.class);
        HashSet<AdapterDescriptor> adaptersFromXML = (HashSet<AdapterDescriptor>) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(adaptersFromXML);
        assertFalse(adaptersFromXML.isEmpty());

        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        XStream jsonXstream = new XStream(new JettisonMappedXmlDriver());
        jsonXstream.processAnnotations(AdapterDescriptor.class);
        HashSet<AdapterDescriptor> adaptersFromJSON = (HashSet<AdapterDescriptor>) jsonXstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(adaptersFromJSON);
        assertFalse(adaptersFromJSON.isEmpty());

        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        htmlRepresentation.write(baos);
        String html = baos.toString();
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }
}
