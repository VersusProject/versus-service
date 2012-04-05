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

import org.apache.mina.util.AvailablePortFinder;
import org.restlet.Component;
import org.restlet.data.Protocol;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.DummyAdapter;
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

    public ComparisonClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        int port = AvailablePortFinder.getNextAvailable(8080);
        String host = "http://127.0.0.1:" + port + "/versus";
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
    public void testGetComparison() {
        System.out.println("getComparison");
        Comparison comparison = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor2.class.getName(),
                DummyMeasure.class.getName());
        Comparison submitted = client.submit(comparison);
        Comparison result = client.getComparison(submitted.getId());
        assertNotNull(result);
        assertEquals(submitted, result);
    }

    /**
     * Test of submit method, of class ComparisonClient.
     */
    @Test
    public void testSubmit() {
        System.out.println("submit");
        Comparison comparison = new Comparison(
                "http://isda.ncsa.illinois.edu/img/ISDA-logo.png",
                "http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif",
                DummyAdapter.class.getName(),
                DummyExtractor2.class.getName(),
                DummyMeasure.class.getName());
        Comparison result = client.submit(comparison);
        assertNotNull(result);
        assertEquals(comparison.getId(), result.getId());
        assertEquals(comparison.getAdapterId(), result.getAdapterId());
        assertEquals(comparison.getExtractorId(), result.getExtractorId());
        assertEquals(comparison.getMeasureId(), result.getMeasureId());
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
