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


public class Weather {
	Context c;
	public Weather(Context c){
		this.c=c;
	}
	public String weather(){
		String buf=new String();
		String s="";

		Log.i("info","working maybe?");
		AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
		try {
			// specify the host, protocol, and port
			HttpHost target = new HttpHost("weather.yahooapis.com", 80, "http");

			// specify the get request
//			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
//			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			double lng = location.getLongitude();
//			double lat = location.getLatitude();
			
//			Geocoder geocoder = new Geocoder(c, Locale.getDefault());
//			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			HttpGet getRequest = new HttpGet("/forecastrss?p=19104&u=t");

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
			if(nodes.getLength()>0)  {
				int temp = Integer.parseInt(((org.w3c.dom.Element)nodes.item(0)).getAttribute("temp"));
				if (temp < 50) s+= temp + " degrees? Let's speed up this Global Warming thing.";
				else if (temp < 60) s+= "It's " + temp + "degrees. Good God it's cold. Not that you were going to go outside, anyway. ";
				else if (temp < 70) s+= temp + "degrees outside? That's pretty alright.";
				else if (temp < 80) s+= "The temperature is " + temp + ". Today could be a good day to start going outside. ";
				else if (temp < 90) s+= "It's " + temp + " degrees. That's not bad. ";
				else if (temp < 100) s+= temp + " degrees? That's pretty hot. ";
				else s+= temp + " degrees? Are you Satan? ";
				//s+="Current temperature: "+((org.w3c.dom.Element)nodes.item(0)).getAttribute("temp")+". ";
			}
			if(nodes2.getLength()>0){ // So then change what these might be...
				s+="Today the temperature will range from "+((org.w3c.dom.Element)nodes2.item(0)).getAttribute("low");
				s+=" to "+((org.w3c.dom.Element)nodes2.item(0)).getAttribute("high")+" degrees. ";
				switch (Integer.parseInt(((org.w3c.dom.Element)nodes2.item(0)).getAttribute("code"))){
					case 0: s+="You may have some tornadoes coming your way! "; break;
					case 1: s+="A tropical storm is going to stop by. Don't forget a towel! "; break;
					case 2: s+="Hurricanes are scary shit. "; break;
					case 3:
					case 4: s+="There will be bright flashes and loud noises outside today. Everything is okay; try to remain calm. "; break;
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10: s+="Be on the watch for ice! "; break;
					case 11:
					case 12: s+="It's gonna be wet! "; break;
					case 13:
					case 14:
					case 15:
					case 16: s+="There's snow afoot. Watch out for the Yeti! "; break;
					case 17:
					case 18: s+="Hard wet stuff is falling from the sky. "; break;
					case 19:
					case 20:
					case 21: s+="You won't be able to see much through your windows today. Like outside, not in your computer. "; break;
					case 22: s+="It's smokey outside!"; break;
					case 23:
					case 24: s+="Air is moving fast outside. Don't get blown away. "; break;
					case 25: s+="Seriously. It's cold. "; break;
					case 26:
					case 27:
					case 28:
					case 29:
					case 30: s+="It will be cloudy with a chance of meatballs. "; break;
					case 31:
					case 32:
					case 33:
					case 34: s+="No other reason to complain. "; break;
					case 35: s+="Today will be tempermental. "; break;
					case 36: s+="You're gonna be sweating your balls off. "; break;
					case 37:
					case 38:
					case 39: s+="You will occasionally be scared by bright flashes and loud sounds. ";break; 
					case 40: s+="Kinda wet. Don't forget a towel! "; break;
					case 41:
					case 42:
					case 43: s+="Snow! Snow! Snow! Don't forget a toboggan! "; break;
					case 44: s+="There will be more white stuff in the sky than usual. "; break;
					case 45:
					case 46:
					case 47: s+="Various forms of water will be coming from the sky. "; break;
					default: s+="I screwed up the weather. I'm really sorry. "; break;
				}
				//s+="Today will be "+((org.w3c.dom.Element)nodes2.item(0)).getAttribute("code")+". ";
				s+="...";
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
