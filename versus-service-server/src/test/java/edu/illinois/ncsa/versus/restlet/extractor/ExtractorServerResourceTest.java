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
package edu.illinois.ncsa.versus.restlet.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import edu.illinois.ncsa.versus.adapter.Adapter;
import edu.illinois.ncsa.versus.core.extractor.ExtractorDescriptor;
import edu.illinois.ncsa.versus.core.extractor.ExtractorsClient;
import edu.illinois.ncsa.versus.extract.impl.DummyExtractor;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class ExtractorServerResourceTest {

    private static Component component;

    private static String url;

    public ExtractorServerResourceTest() {
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
        DummyExtractor dummyExtractor = new DummyExtractor();
        CompareRegistry compareRegistry = new CompareRegistry();
        Collection<Adapter> availableAdapters = compareRegistry.getAvailableAdapters(dummyExtractor);
        ArrayList<String> supportedAdaptersId = new ArrayList<String>(availableAdapters.size());
        for (Adapter ad : availableAdapters) {
            supportedAdaptersId.add(ad.getClass().getName());
        }
        ExtractorDescriptor expected = new ExtractorDescriptor(
                dummyExtractor.getName(), dummyExtractor.getClass().getName(),
                dummyExtractor.getCategory(), supportedAdaptersId,
                dummyExtractor.getFeatureType().getName(), true);

        ClientResource clientResource = new ClientResource(url + ExtractorServerResource.URL + DummyExtractor.class.getName());
        ExtractorDescriptor extractor = clientResource.get(ExtractorDescriptor.class);
        assertNotNull(extractor);
        assertEquals(expected, extractor);
        assertExtractorsDescriptorEquals(expected, extractor);

        ExtractorsClient extractorsClient = new ExtractorsClient(url);
        ExtractorDescriptor extractor2 = extractorsClient.getExtractorDescriptor(DummyExtractor.class.getName());
        assertNotNull(extractor2);
        assertEquals(expected, extractor2);
        assertExtractorsDescriptorEquals(expected, extractor2);

        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(ExtractorDescriptor.class);
        ExtractorDescriptor extractorFromXML = (ExtractorDescriptor) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(extractorFromXML);
        assertEquals(expected, extractorFromXML);
        assertExtractorsDescriptorEquals(expected, extractorFromXML);

        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.processAnnotations(ExtractorDescriptor.class);
        ExtractorDescriptor extractorFromJSON = (ExtractorDescriptor) xstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(extractorFromJSON);
        assertEquals(expected, extractorFromJSON);
        assertExtractorsDescriptorEquals(expected, extractorFromJSON);

        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private static void assertExtractorsDescriptorEquals(ExtractorDescriptor extractor1, ExtractorDescriptor extractor2) {
        assertEquals(extractor1.getName(), extractor2.getName());
        assertEquals(extractor1.getType(), extractor2.getType());
        assertEquals(extractor1.getCategory(), extractor2.getCategory());
        String[] adapters1 = extractor1.getSupportedAdapters().toArray(new String[] {});
        String[] adapters2 = extractor2.getSupportedAdapters().toArray(new String[] {});
        Arrays.sort(adapters1);
        Arrays.sort(adapters2);
        assertArrayEquals(adapters1, adapters2);
        assertEquals(extractor1.getSupportedFeature(), extractor2.getSupportedFeature());
        assertEquals(extractor1.hasHelp(), extractor2.hasHelp());
    }
    
    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
