package com.pennapps.morningorganizer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class GetInfoTask extends AsyncTask<Context, Void, String> {
	String informationString;
	Handler errorHandler;
	Context thisContext;
	Nuance nuanceObject = new Nuance();
	
	//Only give this 1 Context, pleaaase
	protected String doInBackground(Context... c)
	{
		thisContext = c[0];
		//1. Run weather, mail, etc. functions and get input
		Weather handleWeather = new Weather();
		String weatherData = handleWeather.weather();
		Calendar calendar = new Calendar();
		String calendarData = calendar.getCalendarStuff(thisContext);
		SMSCount smsData = new SMSCount();
		String smsDataString = smsData.getSMSstring(thisContext);
		//2. Turn values into strings, put info into 
		//   informationString
		informationString = weatherData + " " + smsDataString + ". " + calendarData;
			//3. Hemanth put your shit here
		//String debugString = "hi world how are you today?";
		

		nuanceObject.initializeSpeechKit(thisContext, errorHandler);
		return informationString;
		
	}
	
	protected void onPostExecute(String result)
	{
		
		nuanceObject.speakTheString(result, thisContext);
	}
}
