package com.pennapps.morningorganizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

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
	public Weather(){}
	public String[] weather(){
		String buf=new String();
		List<String> l = new ArrayList<String>();
		String[] s=null;

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
			buf = EntityUtils.toString(entity);
			Scanner scan = new Scanner(buf);
			Log.i("info", buf);
			// <yweather:forecast day="Fri" date="6 Sep 2013" low="65" high="94" text="Clear" code="31"/>
			//scan.findInLine("low=\"(\\d+)\" high=\"(\\d+)\" text=\"(\\w+)\" code=\"(\\d+)\"");
			//scan.findInLine("<yweather:forecast day=\"Sat\" date=\"7 Sep 2013\" low=\"(\\d+)\" high=\"(\\d+)\" text=\"(\\w+)\" code=\"(\\d+)\" />");

			
			// **** TODO: try this? ****
			
			while(scan.hasNextLine()){
				if (scan.hasNext("yweather")){ 
					scan.findInLine("low=\"(\\d+)\""); //Doesn't work either, wtf
					MatchResult match = scan.match();
					for(int i=0;i<match.groupCount();i++)
						l.add(match.group(i).toString());
					break;
				}
				scan.nextLine();
			}
			
			/*
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
			*/

		} catch (Exception e) {
			Log.i("info","---ERROR---");
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();

		}
		s=(String[])l.toArray(new String[l.size()]);
		return s;
	}
}
