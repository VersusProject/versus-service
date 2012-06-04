///*
// * This software was developed at the National Institute of Standards and
// * Technology by employees of the Federal Government in the course of
// * their official duties. Pursuant to title 17 Section 105 of the United
// * States Code this software is not subject to copyright protection and is
// * in the public domain. This software is an experimental system. NIST assumes
// * no responsibility whatsoever for its use by other parties, and makes no
// * guarantees, expressed or implied, about its quality, reliability, or
// * any other characteristic. We would appreciate acknowledgement if the
// * software is used.
// */
//package edu.illinois.ncsa.versus.restlet;
//
//import edu.illinois.ncsa.versus.core.StringCollectionConverter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.mina.util.AvailablePortFinder;
//import org.restlet.Component;
//import org.restlet.data.MediaType;
//import org.restlet.data.Protocol;
//import org.restlet.data.Status;
//import org.restlet.representation.Representation;
//import org.restlet.resource.ClientResource;
//import org.restlet.resource.ResourceException;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.BeforeClass;
//
///**
// *
// * @author antoinev
// */
//public class SlavesServerResourceTest {
//
//    private static Component masterComponent;
//
//    private static String uri;
//
//    private static ServerApplication masterApplication;
//
//    public SlavesServerResourceTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        int port = AvailablePortFinder.getNextAvailable(8080);
//
//        uri = "http://127.0.0.1:" + port + "/versus";
//
//        masterApplication = new ServerApplication(port, "/versus");
//
//        masterComponent = new Component();
//        masterComponent.getServers().add(Protocol.HTTP, port);
//
//        masterComponent.getDefaultHost().attach("/versus", masterApplication);
//        masterComponent.start();
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//        masterComponent.stop();
//    }
//
//    @Before
//    public void setUp() {
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    private Component connectSlave() throws Exception {
//        int slavePort = AvailablePortFinder.getNextAvailable(8080);
//        return connectSlave(slavePort);
//    }
//
//    private Component connectSlave(int slavePort) throws Exception {
//        ServerApplication slaveApplication = new ServerApplication(slavePort, "/versus", uri);
//        Component slaveComponent = new Component();
//        slaveComponent.getServers().add(Protocol.HTTP, slavePort);
//        slaveComponent.getDefaultHost().attach("/versus", slaveApplication);
//        slaveComponent.start();
//        return slaveComponent;
//    }
//
//    private static ArrayList<Integer> getNextAvailablesPorts(int fromPort, int number) {
//        ArrayList<Integer> result = new ArrayList<Integer>(number);
//
//        for (int i = 0; i < number; i++) {
//            int avPort = AvailablePortFinder.getNextAvailable(fromPort);
//            result.add(avPort);
//            fromPort = avPort + 1;
//        }
//
//        return result;
//    }
//
//    @Test
//    public void testStartStop() throws Exception {
//        SlavesManager slavesManager = masterApplication.getSlavesManager();
//        assertTrue(slavesManager.getSlaves().isEmpty());
//
//        ArrayList<Component> slaves = new ArrayList<Component>(10);
//        for (int i = 1; i <= 10; i++) {
//            slaves.add(connectSlave());
//            assertEquals(i, slavesManager.getSlavesNumber());
//        }
//        masterApplication.getAdaptersId();
//        for (Component slave : slaves) {
//            slave.stop();
//        }
//        masterApplication.getAdaptersId();
//        assertTrue(slavesManager.getSlaves().isEmpty());
//    }
//
//    @Test
//    public void testWebService() throws Exception {
//        Component slave1 = connectSlave();
//        Component slave2 = connectSlave();
//
//        try {
//            ClientResource clientResource = new ClientResource(uri + SlavesServerResource.URL);
//
//            HashSet<String> slaves = clientResource.get(HashSet.class);
//            assertNotNull(slaves);
//            assertEquals(2, slaves.size());
//
//            Representation xmlRep = clientResource.get(MediaType.TEXT_XML);
//            HashSet<String> slavesXml = getSlavesFromRepresentation(new XStream(), xmlRep);
//            assertNotNull(slavesXml);
//            CollectionUtils.isEqualCollection(slaves, slavesXml);
//
//            Representation jsonRep = clientResource.get(MediaType.APPLICATION_JSON);
//            HashSet<String> slavesJson = getSlavesFromRepresentation(new XStream(new JettisonMappedXmlDriver()), jsonRep);
//            assertNotNull(slavesJson);
//            CollectionUtils.isEqualCollection(slaves, slavesJson);
//
//            Representation htmlRep = clientResource.get(MediaType.TEXT_HTML);
//            String html = streamToString(htmlRep.getStream());
//            assertNotNull(html);
//            assertFalse(html.isEmpty());
//
//        } finally {
//            slave1.stop();
//            slave2.stop();
//            masterApplication.getAdaptersId();
//        }
//    }
//
//    private HashSet<String> getSlavesFromRepresentation(XStream xstream, Representation representation) throws IOException {
//        xstream.alias("slaves", Set.class);
//        xstream.registerConverter(new StringCollectionConverter() {
//
//            @Override
//            protected String getNodeName() {
//                return "slave";
//            }
//
//            @Override
//            protected Collection getNewT() {
//                return new HashSet();
//            }
//
//            @Override
//            public boolean canConvert(Class type) {
//                return HashSet.class.isAssignableFrom(type);
//            }
//        });
//        return (HashSet<String>) xstream.fromXML(representation.getStream());
//    }
//
//    private String streamToString(InputStream inStream) {
//        return new Scanner(inStream).useDelimiter("\\A").next();
//    }
//
//    @Test
//    public void testThreadSafety() throws Exception {
//        SlavesManager slavesManager = masterApplication.getSlavesManager();
//
//        final int slavesNumber = 10;
//        ExecutorService executor = Executors.newFixedThreadPool(slavesNumber);
//        // Get 10 availabes port
//        ArrayList<Integer> slavesPorts = getNextAvailablesPorts(8080, slavesNumber);
//
//        // Connect 10 slaves at the same time
//        ArrayList<Future<Component>> futures = new ArrayList<Future<Component>>(slavesNumber);
//        for (final int slavePort : slavesPorts) {
//            Future<Component> future = executor.submit(new Callable<Component>() {
//
//                @Override
//                public Component call() throws Exception {
//                    return connectSlave(slavePort);
//                }
//            });
//            futures.add(future);
//        }
//
//        ArrayList<Component> slaves = new ArrayList<Component>(slavesNumber);
//        for (Future<Component> future : futures) {
//            Component slave = future.get();
//            slaves.add(slave);
//        }
//
//        masterApplication.getAdaptersId();
//        assertEquals(slavesNumber, slavesManager.getSlavesNumber());
//
//        // Shutdown the slaves
//        for (Component slave : slaves) {
//            slave.stop();
//        }
//
//        assertEquals(slavesNumber, slavesManager.getSlavesNumber());
//
//        // Start parallel queries which should all try to remove the disconnected slaves
//        ArrayList<Future<?>> submits = new ArrayList<Future<?>>(slavesNumber);
//        for (int i = 0; i < 20; i++) {
//            Future<?> submit = executor.submit(new Runnable() {
//
//                @Override
//                public void run() {
//                    masterApplication.getAdaptersId();
//                }
//            });
//            submits.add(submit);
//        }
//
//        for (Future<?> submit : submits) {
//            submit.get();
//        }
//
//        assertEquals(0, slavesManager.getSlavesNumber());
//    }
//}
