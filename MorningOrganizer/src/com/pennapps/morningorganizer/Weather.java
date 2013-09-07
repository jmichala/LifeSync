package com.pennapps.morningorganizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.net.http.AndroidHttpClient;
import android.util.Log;


public class Weather {
	public static void main(String[] args){
		Log.i("info","yo");
		//		weather();
	}
	public Weather(){}
	public String weather(){
		String buf=new String();
		String s="";

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
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(buf));
			Document doc = db.parse(inStream);
			NodeList nodes = doc.getElementsByTagName("yweather:condition");
			NodeList nodes2= doc.getElementsByTagName("yweather:forecast");
			if(nodes.getLength()>0)  s+="Current temperature: "+((org.w3c.dom.Element)nodes.item(0)).getAttribute("temp")+" ";
			if(nodes2.getLength()>0){ 
				s+="Today's low: "+((org.w3c.dom.Element)nodes.item(0)).getAttribute("low")+" ";
				s+="Today's high: "+((org.w3c.dom.Element)nodes.item(0)).getAttribute("high")+" ";
				s+="Today will be "+((org.w3c.dom.Element)nodes.item(0)).getAttribute("text");
			}
			Log.i("weather",s);
			/*
			Scanner scan = new Scanner(buf);
			Log.i("weather", buf);
			// <yweather:forecast day="Fri" date="6 Sep 2013" low="65" high="94" text="Clear" code="31"/>
			//scan.findInLine("low=\"(\\d+)\" high=\"(\\d+)\" text=\"(\\w+)\" code=\"(\\d+)\"");
			//scan.findInLine("<yweather:forecast day=\"Sat\" date=\"7 Sep 2013\" low=\"(\\d+)\" high=\"(\\d+)\" text=\"(\\w+)\" code=\"(\\d+)\" />");

			
			// **** TODO: CHANGE THIS SHIT TO SML PARSING - APPARENTLY THAT EXISTS ****
			Log.i("weather","\n");
			int chk = 0;
			while(scan.hasNextLine()){
				if (scan.hasNext("<yweather:condition")){ 
					scan.findInLine("low=\"(\\d+)\""); //Doesn't work either, wtf
					MatchResult match = scan.match();
					for(int i=0;i<match.groupCount();i++)
						l.add(match.group(i).toString());
					chk=1;
					break;
				}
				Log.i("weather",scan.nextLine());
			}
			if(chk==0) Log.i("weather", "ran out of lines to scan");
			*/
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
		return s;
	}
}
