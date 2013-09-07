package com.pennapps.morningorganizer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;

public class GetInfoTask extends AsyncTask<Context, Void, String> {
	String informationString;
	Handler errorHandler;
	Context thisContext;
	Nuance nuanceObject = new Nuance();
	
	//Only give this 1 Context, pleaaase
	protected String doInBackground(Context... c)
	{
		thisContext = c[0];
		nuanceObject.initializeSpeechKit(thisContext, errorHandler);
		//1. Run weather, mail, etc. functions and get input
		
		Time now = new Time();
		now.setToNow();
		String day = now.format("%A %B %d");
		int hour = Integer.parseInt(now.format("%I"));
		String meridien = now.format("%p");
		int minute = Integer.parseInt(now.format("%M"));
		String time = ""+hour+", "+(minute<10 ? "oh "+(minute==0? "clock":minute) : minute)+meridien;
		nuanceObject.speakTheString(time+" "+day, thisContext);
		
		Weather handleWeather = new Weather(thisContext);
		String weatherData = handleWeather.weather();
		
		JavaMail jm = new JavaMail();
		String emailData = jm.jm2();
		
		SMSCount smsData = new SMSCount();
		String smsDataString = smsData.getSMSstring(thisContext);
		//2. Turn values into strings, put info into 
		//   informationString
		informationString = weatherData + " " + smsDataString + " " + emailData;
			//3. Hemanth put your shit here
		//String debugString = "hi world how are you today?";
		

		
		return informationString;
		
	}
	
	protected void onPostExecute(String result)
	{
		
		nuanceObject.speakTheString(result, thisContext);
	}
}
