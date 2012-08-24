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

import edu.illinois.ncsa.versus.restlet.ServerApplication;
import gov.nist.itl.ssd.sampling.IndividualDescriptor;
import gov.nist.itl.ssd.sampling.IndividualsClient;
import gov.nist.itl.ssd.sampling.impl.BasicIndividual;

/**
 *
 * @author antoinev
 */
public class IndividualServerResourceTest {

    private static Component component;

    private static String url;

    public IndividualServerResourceTest() {
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

    /**
     * Test of retrieve method, of class IndividualServerResource.
     */
    @Test
    public void test() throws IOException {
        BasicIndividual basicIndividual = new BasicIndividual();
        IndividualDescriptor expected = new IndividualDescriptor(
                basicIndividual.getName(), basicIndividual.getClass().getName(),
                "", basicIndividual.getSupportedMediaTypes(), false);

        ClientResource clientResource = new ClientResource(
                url + IndividualServerResource.URL + BasicIndividual.class.getName());
        IndividualDescriptor individual = clientResource.get(IndividualDescriptor.class);
        assertNotNull(individual);
        assertEquals(expected, individual);
        assertIndividualDescriptorEquals(expected, individual);

        IndividualsClient client = new IndividualsClient(url);
        IndividualDescriptor ind = client.getIndividualDescriptor(BasicIndividual.class.getName());
        assertNotNull(ind);
        assertEquals(expected, ind);
        assertIndividualDescriptorEquals(expected, ind);

        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        xstream.processAnnotations(IndividualDescriptor.class);
        IndividualDescriptor indFromXml = (IndividualDescriptor) xstream.fromXML(xmlRepresentation.getStream());
        assertNotNull(indFromXml);
        assertEquals(expected, indFromXml);
        assertIndividualDescriptorEquals(expected, indFromXml);

        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.processAnnotations(IndividualDescriptor.class);
        IndividualDescriptor indFromJson = (IndividualDescriptor) xstream.fromXML(jsonRepresentation.getStream());
        assertNotNull(indFromJson);
        assertEquals(expected, indFromJson);
        assertIndividualDescriptorEquals(expected, indFromJson);

        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private static void assertIndividualDescriptorEquals(
            IndividualDescriptor expected, IndividualDescriptor actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertArrayEquals(expected.getSupportedMediaTypes().toArray(), actual.getSupportedMediaTypes().toArray());
        assertEquals(expected.hasHelp(), actual.hasHelp());
    }

    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
