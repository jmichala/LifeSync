package com.pennapps.morningorganizer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.util.Log;


public class Weather {
	public static void main(String[] args){
		Log.i("info","yo");
//		weather();
	}
	public static void weather(){
		Log.i("info","working maybe?");
		AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
	    try {
	      // specify the host, protocol, and port
	      HttpHost target = new HttpHost("weather.yahooapis.com", 80, "http");
	
	      // specify the get request 
	      HttpGet getRequest = new HttpGet("/forecastrss?p=80020&u=f");
	
	      Log.i("info","executing request to " + target);
	
	      HttpResponse httpResponse = httpclient.execute(target, getRequest);
	      Log.i("info","getting entity");
	      HttpEntity entity = httpResponse.getEntity();
	
	      Log.i("info","----------------------------------------");
	      Log.i("info",httpResponse.getStatusLine().toString());
	      Header[] headers = httpResponse.getAllHeaders();
	      for (int i = 0; i < headers.length; i++) {
	        Log.i("info",headers[i].toString());
	      }
	      Log.i("info","----------------------------------------");
	
	      if (entity != null) {
	        Log.i("info",EntityUtils.toString(entity));
	      }
	
	    } catch (Exception e) {
	    	Log.i("info","---ERROR---");
	      e.printStackTrace();
	    } finally {
	      // When HttpClient instance is no longer needed,
	      // shut down the connection manager to ensure
	      // immediate deallocation of all system resources
	      httpclient.getConnectionManager().shutdown();
	    }
	
	}
}

