package com.pennapps.morningorganizer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;

public class GetInfoTask extends AsyncTask<Context, Void, String> {
	String informationString;
	Handler errorHandler;
	Context thisContext;
	Nuance nuanceObject = new Nuance();
	
	//Only give this 1 Context, pleaaase
	protected String doInBackground(Context... c)
	{
		thisContext = c[0];
		Vibrator v = (Vibrator) thisContext.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 1 second
		v.vibrate(1000);
		nuanceObject.initializeSpeechKit(thisContext, errorHandler);
		//1. Run weather, mail, etc. functions and get input
		
		Time now = new Time();
		now.setToNow();
		String day = now.format("%A %B %d");
		int hour = Integer.parseInt(now.format("%I"));
		String meridien = now.format("%p");
		int minute = Integer.parseInt(now.format("%M"));
		String time = ""+hour+", "+(minute<10 ? "oh "+(minute==0? "clock":minute) : minute)+meridien;
		String greeting="";
		if(meridien.equals("am")){
			if(hour<6)
				greeting="Why are you up? Go to sleep. It's ";
			else if(hour<10)
				greeting="Good morning. It's ";
			else
				greeting="Good morning, lazy. It's ";
		}
		else{
			if (hour<5)
				greeting="Good afternoon, it's ";
			else
				greeting="Good evening, it is ";
		}
		nuanceObject.speakTheString(greeting+time+" "+day, thisContext);
		
		Weather handleWeather = new Weather(thisContext);
		String weatherData = handleWeather.weather();
		Calendar calendar = new Calendar();
		String calendarData = calendar.getCalendarStuff(thisContext);
		
		JavaMail jm = new JavaMail();
		String emailData = jm.jm2();
		
		SMSCount smsData = new SMSCount();
		String smsDataString = smsData.getSMSstring(thisContext);
		//2. Turn values into strings, put info into 
		//   informationString
		informationString = weatherData + " " + smsDataString + ". " + emailData +". " + calendarData;
		
		
		return informationString;
		
	}
	
	protected void onPostExecute(String result)
	{
		
		nuanceObject.speakTheString(result, thisContext);
	}
}
