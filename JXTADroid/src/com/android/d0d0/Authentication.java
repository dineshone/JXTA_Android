/**
 * 
 */
package com.android.d0d0;

/**
 * @author d0d0
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;


public class Authentication {

	/**
	 * 
	 */
	
	public String line;
	
	
	
	public Authentication() {
		// TODO Auto-generated constructor stub
	}
	
	public void formURL() throws IOException{


		URL url;
		url = new URL("https://accounts.google.com/o/oauth2/auth?" +
				"response_type=code&" +
				"client_id=30565316723.apps.googleusercontent.com&" +
				"redirect_uri=http://localhost:80&" +
				"scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fmapsengine");
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
	    connection.setDoOutput(true);
	    connection.connect();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    
	    while ((line = reader.readLine()) != null) {
	        System.out.println(line);
	      }
	    
		
	}

	
}
