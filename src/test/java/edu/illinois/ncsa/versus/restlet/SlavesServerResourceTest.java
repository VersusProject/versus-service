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
package edu.illinois.ncsa.versus.restlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.mina.util.AvailablePortFinder;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author antoinev
 */
public class SlavesServerResourceTest {

    private static Component masterComponent;

    private static int port;

    private static ServerApplication masterApplication;

    public SlavesServerResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        port = AvailablePortFinder.getNextAvailable(8080);
        masterApplication = new ServerApplication(port, "/versus");

        masterComponent = new Component();
        masterComponent.getServers().add(Protocol.HTTP, port);

        masterComponent.getDefaultHost().attach("/versus", masterApplication);
        masterComponent.start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        masterComponent.stop();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private Component connectSlave() throws Exception {
        int slavePort = AvailablePortFinder.getNextAvailable(8080);
        ServerApplication slaveApplication = new ServerApplication(slavePort, "/versus", "http://127.0.0.1:" + port + "/versus");
        Component slaveComponent = new Component();
        slaveComponent.getServers().add(Protocol.HTTP, slavePort);
        slaveComponent.getDefaultHost().attach("/versus", slaveApplication);
        slaveComponent.start();
        return slaveComponent;
    }

    @Test
    public void test() throws Exception {

        SlavesManager slavesManager = masterApplication.getSlavesManager();

        assertTrue(slavesManager.getSlaves().isEmpty());

        ArrayList<Component> slaves = new ArrayList<Component>(10);
        for (int i = 1; i <= 10; i++) {
            slaves.add(connectSlave());
            assertEquals(i, slavesManager.getSlavesNumber());
        }
        masterApplication.getAdaptersId();
        for(Component slave : slaves) {
            slave.stop();
        }
        masterApplication.getAdaptersId();
        
        assertTrue(slavesManager.getSlaves().isEmpty());
    }
}
