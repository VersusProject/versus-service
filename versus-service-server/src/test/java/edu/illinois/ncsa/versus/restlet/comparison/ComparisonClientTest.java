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

import java.io.File;
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

import edu.illinois.ncsa.versus.adapter.impl.DummyAdapter;
import edu.illinois.ncsa.versus.core.StringCollectionConverter;
import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.ComparisonClient;
import edu.illinois.ncsa.versus.extract.impl.DummyExtractor;
import edu.illinois.ncsa.versus.extract.impl.DummyExtractor2;
import edu.illinois.ncsa.versus.measure.impl.DummyMeasure;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class ComparisonClientTest {

    private static Component component;

    private static ComparisonClient client;

    private static String host;

    public ComparisonClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        int port = AvailablePortFinder.getNextAvailable(8080);
        host = "http://127.0.0.1:" + port + "/versus";
        client = new ComparisonClient(host);

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
     * Test of getComparison method, of class ComparisonClient.
     */
    @Test
    public void testGetComparison() throws IOException {
        System.out.println("getComparison");
        Comparison comparison = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor2.class.getName(),
                DummyMeasure.class.getName());
        String submitted = client.submit(comparison);
        Comparison result = client.getComparison(submitted);
        assertNotNull(result);

        ClientResource cr = new ClientResource(host + ComparisonServerResource.URL + submitted);

        Representation xmlRep = cr.get(MediaType.TEXT_XML);
        Comparison comparisonFromXml = getComparisonFromRepresentation(new XStream(), xmlRep);
        assertNotNull(comparisonFromXml);
        assertEquals(result, comparisonFromXml);

        Representation jsonRep = cr.get(MediaType.APPLICATION_JSON);
        Comparison comparisonFromJson = getComparisonFromRepresentation(new XStream(new JettisonMappedXmlDriver()), jsonRep);
        assertNotNull(comparisonFromJson);
        assertEquals(result, comparisonFromJson);

        Representation htmlRep = cr.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRep.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private Comparison getComparisonFromRepresentation(XStream xstream, Representation representation) throws IOException {
        xstream.processAnnotations(Comparison.class);
        return (Comparison) xstream.fromXML(representation.getStream());
    }

    @Test
    public void testGetComparisons() throws IOException {
        Comparison comparison1 = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor2.class.getName(),
                DummyMeasure.class.getName());
        Comparison comparison2 = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor.class.getName(),
                DummyMeasure.class.getName());
        client.submit(comparison1);
        client.submit(comparison2);

        HashSet<String> comparisons = client.getComparisons();
        assertNotNull(comparisons);
        assertFalse(comparisons.isEmpty());

        ClientResource cr = new ClientResource(host + ComparisonsServerResource.URL);

        Representation xmlRep = cr.get(MediaType.TEXT_XML);
        HashSet<String> comparisonsFromXml = getComparisonsFromRepresentation(new XStream(), xmlRep);
        assertNotNull(comparisonsFromXml);
        CollectionUtils.isEqualCollection(comparisons, comparisonsFromXml);

        Representation jsonRep = cr.get(MediaType.APPLICATION_JSON);
        HashSet<String> comparisonsFromJson = getComparisonsFromRepresentation(new XStream(new JettisonMappedXmlDriver()), jsonRep);
        assertNotNull(comparisonsFromJson);
        CollectionUtils.isEqualCollection(comparisons, comparisonsFromJson);

        Representation htmlRep = cr.get(MediaType.TEXT_HTML);
        String html = streamToString(htmlRep.getStream());
        assertNotNull(html);
        assertFalse(html.isEmpty());
    }

    private HashSet<String> getComparisonsFromRepresentation(XStream xstream, Representation representation) throws IOException {
        xstream.alias("comparisons", Set.class);
        xstream.registerConverter(new StringCollectionConverter() {

            @Override
            protected String getNodeName() {
                return "comparison";
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

    /**
     * Test of submit method, of class ComparisonClient.
     */
    @Test
    public void testSubmit() throws IOException {
        System.out.println("submit");
        Comparison comparison = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor2.class.getName(),
                DummyMeasure.class.getName());
        String id = client.submit(comparison);
        assertNotNull(id);
        
        Comparison result = client.getComparison(id);
        assertEquals(id, result.getId());
        assertEquals(comparison.getAdapterId(), result.getAdapterId());
        assertEquals(comparison.getExtractorId(), result.getExtractorId());
        assertEquals(comparison.getMeasureId(), result.getMeasureId());
        assertEquals(comparison.getFirstDataset(), result.getFirstDataset());
        assertEquals(comparison.getSecondDataset(), result.getSecondDataset());
    }

    @Test
    public void testSubmitFiles() throws IOException {
        String submit = client.submit(DummyAdapter.class.getName(),
                                DummyExtractor.class.getName(), 
                                DummyMeasure.class.getName(), 
                                new File("data/test_1.jpg"), 
                                new File("data/test_2.jpg"));
        
    }

    /**
     * Test of supportComparison method, of class ComparisonClient.
     */
    @Test
    public void testSupportComparison() {
        System.out.println("supportComparison");
        String adapterId = DummyAdapter.class.getName();
        String extractorId = DummyExtractor2.class.getName();
        String measureId = DummyMeasure.class.getName();
        assertTrue(client.supportComparison(adapterId, extractorId, measureId));
        assertFalse(client.supportComparison("adapter", "extractor", "measure"));
    }
}
