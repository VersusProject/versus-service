package edu.illinois.ncsa.versus.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class CensusIndexerBuild {
	private static Log log = LogFactory.getLog(CensusIndexerBuild.class);

	public static void main(String[] args) {
		HttpClient client = new DefaultHttpClient();
		String id = "273af0f1-5e40-4860-a5fa-97a9c5a017c8";
		String requestUrl = "http://localhost:8080/api/v1/index/" + id + "/add";

		File inputFolder = new File(
				"/Users/smruti/NCSAResearch/workspace-versus/ex1");
		for (File file : inputFolder.listFiles()) {
			if (file.isFile()) {
				HttpPost httpPost = new HttpPost(requestUrl);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("infile",
						"file:///C:/Users/smruti/NCSAResearch/workspace-versus/ex1/"
								+ file.getName()));
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nvps,
							HTTP.UTF_8));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpResponse response = null;
				try {
					response = client.execute(httpPost);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(
									(response.getEntity().getContent())));

					String output, output1 = null;
					System.out.println("Response from the Server ....POST: \n");

					while ((output = br.readLine()) != null) {
						output1 = output;
						log.debug(output);
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// log.debug(response);

			}
		}

	}

}
