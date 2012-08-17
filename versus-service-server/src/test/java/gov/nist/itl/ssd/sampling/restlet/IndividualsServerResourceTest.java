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
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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

import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import gov.nist.itl.ssd.sampling.IndividualsClient;

/**
 *
 * @author antoinev
 */
public class IndividualsServerResourceTest {

    private static Component component;

    private static String url;

    public IndividualsServerResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        int port = AvailablePortFinder.getNextAvailable(8080);
        url = "http://127.0.0.1:" + port + "/versus";

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
        IndividualsClient individualsClient = new IndividualsClient(url);
        HashSet<String> individuals = individualsClient.getIndividuals();
        assertNotNull(individuals);
        assertFalse(individuals.isEmpty());

        ClientResource clientResource = new ClientResource(url + IndividualsServerResource.URL);

        Representation xmlRepresentation = clientResource.get(MediaType.TEXT_XML);
        XStream xstream = new XStream();
        HashSet<String> individualsFromXml =
                getIndividualsFromRepresentation(xstream, xmlRepresentation);
        assertNotNull(individualsFromXml);
        assertTrue(CollectionUtils.isEqualCollection(individuals, individualsFromXml));

        Representation jsonRepresentation = clientResource.get(MediaType.APPLICATION_JSON);
        XStream jsonXstream = new XStream(new JettisonMappedXmlDriver());
        HashSet<String> individualsFromJSON = getIndividualsFromRepresentation(jsonXstream, jsonRepresentation);
        assertNotNull(individualsFromJSON);
        assertTrue(CollectionUtils.isEqualCollection(individuals, individualsFromJSON));

        Representation htmlRepresentation = clientResource.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRepresentation.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private HashSet<String> getIndividualsFromRepresentation(XStream xstream,
            Representation representation) throws IOException {
        xstream.alias("individuals", Set.class);
        xstream.registerConverter(new StringCollectionConverter() {

            @Override
            protected String getNodeName() {
                return "individual";
            }

            @Override
            protected Collection getNewT() {
                return new HashSet();
            }

            @Override
            public boolean canConvert(Class type) {
                return HashSet.class.isAssignableFrom(type);
            }
        });
        return (HashSet<String>) xstream.fromXML(representation.getStream());
    }

    private String streamToString(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
