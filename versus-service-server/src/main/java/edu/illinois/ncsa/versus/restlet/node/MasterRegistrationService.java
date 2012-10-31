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
package edu.illinois.ncsa.versus.restlet.node;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import edu.illinois.ncsa.versus.core.node.SlavesClient;

/**
 *
 * @author antoinev
 */
public class MasterRegistrationService {

    private static final long checkFrequency = 3600;

    private static final long retryFrequency = 10;

    private final String masterUrl;

    private final ScheduledExecutorService executor;

    private final String slaveUrl;

    private final SlavesClient slavesClient;

    public MasterRegistrationService(int port, String baseUrl, String masterUrl) throws SocketException {
        this.masterUrl = masterUrl;
        slavesClient = new SlavesClient(masterUrl, 2, 0);

        ArrayList<String> ips = new ArrayList<String>();
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (ni.isUp() && !ni.isLoopback()) {
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();
                    if (ia instanceof Inet4Address) {
                        ips.add(ia.getHostAddress());
                    }
                }
            }
        }
        String ip = ips.get(0); //TODO choose the good IP on computers with multiple network interfaces
        slaveUrl = "http://" + ip + ':' + port + baseUrl;

        executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "MasterRegistrationService");
                thread.setDaemon(true);
                return thread;
            }
        });
        executor.schedule(new MasterRegistrationTask(), 0, TimeUnit.SECONDS);
    }

    private class MasterRegistrationTask implements Runnable {

        @Override
        public void run() {
            if (!isRegistered()) {
                try {
                    registerWithMaster();
                } catch (Exception e) {
                    Logger.getLogger(MasterRegistrationTask.class.getName()).
                            log(Level.WARNING,
                            "Cannot register with master. Retrying in "
                            + retryFrequency + "s.", e);
                    executor.schedule(this, retryFrequency, TimeUnit.SECONDS);
                    return;
                }
            }

            executor.schedule(this, checkFrequency, TimeUnit.SECONDS);
        }

        private boolean isRegistered() {
            try {
                return slavesClient.getSlaves().contains(slaveUrl);
            } catch (Exception e) {
                Logger.getLogger(MasterRegistrationTask.class.getName()).log(
                        Level.WARNING, "Cannot get slaves list from master.", e);
                return false;
            }
        }

        private void registerWithMaster() {
            Logger.getLogger(MasterRegistrationTask.class.getName()).log(
                    Level.INFO, "Registering to master {0}", masterUrl);

            ClientResource masterResource = new ClientResource(masterUrl
                    + SlavesServerResource.URL);
            Form form = new Form();
            form.add("url", slaveUrl);
            Representation post = masterResource.post(form.getWebRepresentation());
            if (post == null) {
                throw new RuntimeException("No response from master.");
            }
            Logger.getLogger(MasterRegistrationTask.class.getName()).log(
                    Level.INFO, "Registration with master {0} done.", masterUrl);
        }
    }
}
