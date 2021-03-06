package com.pennapps.morningorganizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.util.Log;


public class RSS {
	Context c;
	public RSS(Context c){
		this.c=c;
	}
	public String readRSS(){
		String buf=new String();
		String s="";

		Log.i("info","working maybe?");
		AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
		try {
			// specify the host, protocol, and port
			HttpHost target = new HttpHost("www.npr.org", 80, "http");
			
			// specify the get request
//			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
//			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			double lng = location.getLongitude();
//			double lat = location.getLatitude();
			
//			Geocoder geocoder = new Geocoder(c, Locale.getDefault());
//			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			HttpGet getRequest = new HttpGet("/rss/rss.php?id=1001");

			Log.i("rss","executing request to " + target);

			HttpResponse httpResponse = httpclient.execute(target, getRequest);
			Log.i("rss","getting entity");
			HttpEntity entity = httpResponse.getEntity();
			buf = EntityUtils.toString(entity);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(buf));
			Document doc = db.parse(inStream);
			NodeList nodes = doc.getElementsByTagName("title");
			for(int i=1;i<nodes.getLength();i++)  
				s+=nodes.item(i).getTextContent()+" ... ";
			Log.i("rss",s);
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
			Log.i("rss","---ERROR---");
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
