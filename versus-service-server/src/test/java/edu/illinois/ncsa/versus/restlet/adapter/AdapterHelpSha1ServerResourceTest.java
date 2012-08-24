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

import org.apache.mina.util.AvailablePortFinder;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.illinois.ncsa.versus.adapter.impl.DummyAdapter;
import edu.illinois.ncsa.versus.core.adapter.AdaptersClient;
import edu.illinois.ncsa.versus.restlet.ServerApplication;

/**
 *
 * @author antoinev
 */
public class AdapterHelpSha1ServerResourceTest {
    
    private static Component component;

    private static String url;

    public AdapterHelpSha1ServerResourceTest() {
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

        ClientResource clientResource = new ClientResource(url + "/adapters/" + DummyAdapter.class.getName() + "/helpsha1");
        String expected = dummyAdapter.getHelpSHA1();
        String result = clientResource.get(String.class);
        assertNotNull(result);
        assertEquals(expected, result);
        
        expected = dummyAdapter.getHelpSHA1();
        result = new AdaptersClient(url).getAdapterHelpSha1(DummyAdapter.class.getName());
        assertNotNull(result);
        assertEquals(expected, result);
    }
}
