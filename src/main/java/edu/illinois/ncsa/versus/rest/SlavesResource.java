package edu.illinois.ncsa.versus.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.Form;


import edu.illinois.ncsa.versus.restlet.Slave;


@Path("/slaves")
public class SlavesResource {
	
	private static Log log = LogFactory.getLog(SlavesResource.class);
    
	@GET
	//@Path("/list")
	@Produces("text/html")
	public String listHTML(@Context ServletContext context) {

		log.trace("/slaves requested");
		log.debug("/slaves requested");
		Collection<Slave> slaves = getSlaves(context);
		if (slaves.size() == 0) {
			return "No slaves";
		} else {
			String content = new String("<h3>Versus > Slaves</h3><ul>");
			for (Slave slave : slaves) {
				/*content += "<li><a href='slaves/"
						+ slave.getUrl() + "'>"
						+ slave.getUrl() + "</a></li>";*/
				content+="<li><a href="+slave.getUrl()+">"+slave.getUrl()+"</a></li>";
			}
			content += "</ul>";
			return content;
		}
	}

	@POST
	@Path("/add")
	//@Produces("text/html")
	//@Consumes("application/")
	
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String submit(@Form RegistrationForm regForm,
			@Context ServletContext context) {
        SlavesList slaveList=(SlavesList) context.getAttribute(SlavesList.class.getName());
		//Injector injector = (Injector) context.getAttribute(Injector.class
		//		.getName());
		//SlavesList slaveList=injector.getInstance(SlavesList.class);
			
		slaveList.addSlave(regForm.slaveURL);
		return "Sucess";
		}
	
	
	public static class RegistrationForm{
		@FormParam("url")
		private String slaveURL;
		public String toString(){
			return "url="+slaveURL;
		}
	}
	

	public List<Slave> getSlaves(ServletContext context){
	
	SlavesList slaveList = (SlavesList) context
			.getAttribute(SlavesList.class.getName());

	return SlavesList.getSlaves();
}
	

}