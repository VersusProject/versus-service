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

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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

import edu.illinois.ncsa.versus.adapter.impl.DummyAdapter;
import edu.illinois.ncsa.versus.core.adapter.AdapterDescriptor;
import edu.illinois.ncsa.versus.core.adapter.AdaptersClient;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class AdapterServerResourceTest {

    private static Component component;

    private static String url;

    public AdapterServerResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        int port = AvailablePortFinder.getNextAvailable(8080);

        url = "http://127.0.0.1:" + port + "/versus";

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
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException {
        DummyAdapter dummyAdapter = new DummyAdapter();
        AdapterDescriptor expected = new AdapterDescriptor(
                dummyAdapter.getName(), dummyAdapter.getClass().getName(),
                dummyAdapter.getCategory(),
                dummyAdapter.getSupportedMediaTypes(), true);

        ClientResource clientResource = new ClientResource(url + AdapterServerResource.URL + DummyAdapter.class.getName());
        AdapterDescriptor adapter = clientResource.get(AdapterDescriptor.class);
        assertNotNull(adapter);
        assertEquals(expected, adapter);
        assertAdapterDescriptorEquals(expected, adapter);

        AdaptersClient adaptersClient = new AdaptersClient(url);
        AdapterDescriptor adapter2 = adaptersClient.getAdapterDescriptor(DummyAdapter.class.getName());
        assertNotNull(adapter2);
        assertEquals(expected, adapter2);
        assertAdapterDescriptorEquals(expected, adapter2);

        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(AdapterDescriptor.class);
        AdapterDescriptor adapterFromXML = (AdapterDescriptor) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(adapterFromXML);
        assertEquals(expected, adapterFromXML);
        assertAdapterDescriptorEquals(expected, adapterFromXML);

        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.processAnnotations(AdapterDescriptor.class);
        AdapterDescriptor adapterFromJSON = (AdapterDescriptor) xstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(adapterFromJSON);
        assertEquals(expected, adapterFromJSON);
        assertAdapterDescriptorEquals(expected, adapterFromJSON);

        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private static void assertAdapterDescriptorEquals(AdapterDescriptor adapter1, AdapterDescriptor adapter2) {
        assertEquals(adapter1.getName(), adapter2.getName());
        assertEquals(adapter1.getId(), adapter2.getId());
        assertEquals(adapter1.getCategory(), adapter2.getCategory());
        assertArrayEquals(adapter1.getSupportedMediaTypes().toArray(), adapter2.getSupportedMediaTypes().toArray());
        assertEquals(adapter1.hasHelp(), adapter2.hasHelp());
    }

    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
