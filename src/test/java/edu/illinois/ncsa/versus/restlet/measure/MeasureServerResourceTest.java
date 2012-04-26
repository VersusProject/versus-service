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

import edu.illinois.ncsa.versus.measure.impl.DummyMeasure;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class MeasureServerResourceTest {

    private static Component component;
    
    private static String url;
    
    public MeasureServerResourceTest() {
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
        MeasureDescriptor expected = new MeasureDescriptor(new DummyMeasure());
        
        ClientResource clientResource = new ClientResource(url + MeasureServerResource.URL + DummyMeasure.class.getName());
        MeasureDescriptor measure = clientResource.get(MeasureDescriptor.class);
        assertNotNull(measure);
        assertEquals(expected, measure);
        
        MeasuresClient measuresClient = new MeasuresClient(url);
        MeasureDescriptor measure2 = measuresClient.getMeasureDescriptor(DummyMeasure.class.getName());
        assertNotNull(measure2);
        assertEquals(expected, measure2);
        
        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(MeasureDescriptor.class);
        MeasureDescriptor measureFromXML = (MeasureDescriptor) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(measureFromXML);
        assertEquals(expected, measureFromXML);
        
        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.processAnnotations(MeasureDescriptor.class);
        MeasureDescriptor measureFromJSON = (MeasureDescriptor) xstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(measureFromJSON);
        assertEquals(expected, measureFromJSON);
        
        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }
    
    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
