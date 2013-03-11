/**
 * 
 */
package edu.illinois.ncsa.versus.restlet;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author lmarini
 * 
 */
public class Slave {

	private URL url;
	private boolean statusBusy=false;

	public Slave(String hostRef) {
		try {
			this.url = new URL(hostRef);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public URL getUrl() {
		return url;
	}
	
	public boolean getStatus(){
		return statusBusy;
	}
	
	public void setStatus(boolean status){
	      statusBusy=status;	
	}
}
