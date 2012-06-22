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
package gov.nist.itl.ssd.sampling.restlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

import edu.illinois.ncsa.versus.restlet.ServerApplication;
import gov.nist.itl.ssd.sampling.Individual;
import gov.nist.itl.ssd.sampling.SamplerDescriptor;
import gov.nist.itl.ssd.sampling.SamplersClient;
import gov.nist.itl.ssd.sampling.SamplingRegistry;
import gov.nist.itl.ssd.sampling.impl.BasicIndividual;
import gov.nist.itl.ssd.sampling.impl.RandomSampler;

/**
 *
 * @author antoinev
 */
public class SamplerServerResourceTest {
    
    private static Component component;
    
    private static String url;
    
    public SamplerServerResourceTest() {
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
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void test() throws IOException {
        RandomSampler randomSampler = new RandomSampler();
        SamplingRegistry registry = new SamplingRegistry();
        Collection<String> availableIndividualsIds = registry.getAvailableIndividualsIds(randomSampler);
        SamplerDescriptor expected = new SamplerDescriptor(
                randomSampler.getName(), randomSampler.getClass().getName(),
                "", availableIndividualsIds, false);
        
        ClientResource clientResource = new ClientResource(
                url + SamplerServerResource.URL + RandomSampler.class.getName());
//        SamplerDescriptor sampler = clientResource.get(SamplerDescriptor.class);
//        assertNotNull(sampler);
//        assertEquals(expected, sampler);
//        assertSamplersDescriptorEquals(expected, sampler);
        
//        SamplersClient client = new SamplersClient(url);
//        SamplerDescriptor samplerDesc = client.getSamplerDescriptor(RandomSampler.class.getName());
//        assertNotNull(samplerDesc);
//        assertEquals(expected, samplerDesc);
//        assertSamplersDescriptorEquals(expected, samplerDesc);
        
        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(SamplerDescriptor.class);
        SamplerDescriptor samplerFromXml = (SamplerDescriptor) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(samplerFromXml);
        assertEquals(expected, samplerFromXml);
        assertSamplersDescriptorEquals(expected, samplerFromXml);
        
        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.processAnnotations(SamplerDescriptor.class);
        SamplerDescriptor samplerFromJson = (SamplerDescriptor) xstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(samplerFromJson);
        assertEquals(expected, samplerFromJson);
        assertSamplersDescriptorEquals(expected, samplerFromJson);
        
        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }
    
    private static void assertSamplersDescriptorEquals(
            SamplerDescriptor expected, SamplerDescriptor actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertArrayEquals(expected.getSupportedIndividuals().toArray(), actual.getSupportedIndividuals().toArray());
        assertEquals(expected.hasHelp(), actual.hasHelp());
    }
    
    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
