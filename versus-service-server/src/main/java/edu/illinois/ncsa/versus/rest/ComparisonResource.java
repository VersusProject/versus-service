/**
 *
 */
package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;

import com.google.inject.Injector;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.engine.impl.ExecutionEngine;
import edu.illinois.ncsa.versus.registry.CompareRegistry;
import edu.illinois.ncsa.versus.restlet.NoSlaveAvailableException;
import edu.illinois.ncsa.versus.restlet.ServerApplication;
import edu.illinois.ncsa.versus.restlet.SlavesManager;
import edu.illinois.ncsa.versus.restlet.comparison.ComparisonSubmitter;
import edu.illinois.ncsa.versus.store.ComparisonServiceImpl;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Submit and query comparisons.
 *
 * @author Luigi Marini
 *
 */
@Path("/comparisons")
public class ComparisonResource {

    private static Log log = LogFactory.getLog(ComparisonResource.class);

    @GET
    @Produces("application/json")
    public List<String> list(@Context ServletContext context) {

        Injector injector = (Injector) context.getAttribute(Injector.class.getName());
        ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
        List<String> json = new ArrayList<String>();
        for (Comparison comparison : comparisonService.listAll()) {
            json.add(comparison.getId());
        }
        return json;
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getComparison(@PathParam("id") String id,
            @Context ServletContext context) {

        Injector injector = (Injector) context.getAttribute(Injector.class.getName());

        ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
        Comparison c = comparisonService.getComparison(id);
        if (c == null) {
            log.debug("Comparison not found " + id);
            return Response.status(404).entity("Comparison " + id + " not found.").build();
        } else {
            log.debug("Comparison found " + id);
            return Response.status(200).entity(c).build();
        }
    }

    @GET
    @Path("/{id}/status")
    @Produces("text/plain")
    public Response getStatus(@PathParam("id") String id,
            @Context ServletContext context) {

        Injector injector = (Injector) context.getAttribute(Injector.class.getName());

        ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
        Comparison c = comparisonService.getComparison(id);
        if (c == null) {
            log.debug("Comparison not found " + id);
            return Response.status(404).entity("Comparison " + id + " not found.").build();
        } else {
            log.debug("Comparison found " + id);
            return Response.status(200).entity(c.getStatus()).build();
        }
    }

    @GET
    @Path("/{id}/value")
    @Produces("text/plain")
    public Response getValue(@PathParam("id") String id,
            @Context ServletContext context) {

        Injector injector = (Injector) context.getAttribute(Injector.class.getName());

        ComparisonServiceImpl comparisonService = injector.getInstance(ComparisonServiceImpl.class);
        Comparison c = comparisonService.getComparison(id);
        if (c == null) {
            log.debug("Comparison not found " + id);
            return Response.status(404).entity("Comparison " + id + " not found.").build();
        } else {
            log.debug("Comparison found " + id);
            return Response.status(200).entity(c.getValue()).build();
        }
    }

    /**
     * Submit new comparison.
     *
     * @param entity
     * @return
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String submit(@Form ComparisonForm comparisonForm,
            @Context ServletContext context) {

        CompareRegistry registry = (CompareRegistry) context
                .getAttribute(CompareRegistry.class.getName());
        ExecutionEngine engine = (ExecutionEngine) context
                .getAttribute(ExecutionEngine.class.getName());
        SlavesManager slavesManager = (SlavesManager) context
                .getAttribute(SlavesManager.class.getName());

        String dataset1Name = comparisonForm.dataset1;
        String dataset2Name = comparisonForm.dataset2;
        InputStream dataset1Stream;
        InputStream dataset2Stream;
        try {
            dataset1Stream = new URL(dataset1Name).openStream();
            dataset2Stream = new URL(dataset2Name).openStream();
        } catch (IOException e) {
            log.error("Internal error writing to disk", e);
            return "Internal error writing to disk";
        }

        Comparison comparison = new Comparison(dataset1Name, dataset2Name,
                comparisonForm.adapter, comparisonForm.extractor,
                comparisonForm.measure);

        try {
            comparison = submitComparison(registry, engine, slavesManager,
                    comparison, dataset1Stream, dataset2Stream);
        } catch (IOException ex) {
            log.error("Cannot acces submitted datasets.", ex);
            return "Cannot acces submitted datasets.";
        } catch (NoSlaveAvailableException ex) {
            log.error("No slaves are available that can handle this comparison", ex);
            return "No slaves are available that can handle this comparison";
        }
        return comparison.getId();
    }

    private Comparison submitComparison(CompareRegistry registry,
            ExecutionEngine engine, SlavesManager slavesManager,
            Comparison comparison,
            InputStream dataset1Stream, InputStream dataset2Stream)
            throws IOException, NoSlaveAvailableException {
        ComparisonSubmitter submitter = new ComparisonSubmitter(registry, engine,
                slavesManager, comparison, dataset1Stream, dataset2Stream);
        return submitter.submit();
    }

    public static class ComparisonForm {

        @FormParam("dataset1")
        private String dataset1;

        @FormParam("dataset2")
        private String dataset2;

        @FormParam("adapter")
        private String adapter;

        @FormParam("extractor")
        private String extractor;

        @FormParam("measure")
        private String measure;

        @Override
        public String toString() {
            return "dataset1=" + dataset1 + "&dataset2=" + dataset2
                    + "&adapter=" + adapter + "&extractor=" + extractor
                    + "&measure=" + measure;
        }
    }
}
