package com.pennapps.morningorganizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.format.Time;

@SuppressLint("NewApi")
public class GetInfoTask extends AsyncTask<Context, Void, String> {
	String informationString;
	Handler errorHandler;
	Context thisContext;
	Nuance nuanceObject = new Nuance();

	//Only give this 1 Context, pleaaase
	protected String doInBackground(Context... c)
	{
		thisContext = c[0];
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(thisContext);
		
		boolean[] reportStuff = new boolean[4];
		reportStuff[0] = sharedPref.getBoolean("news_sync", true);
		reportStuff[1] = sharedPref.getBoolean("cal_sync", true);
		reportStuff[2] = sharedPref.getBoolean("weather_sync", true);
		reportStuff[3] = sharedPref.getBoolean("email_sync", true);
		String emailUser = sharedPref.getString("email_value", "pennapps.morningorganizer@gmail.com");
		String emailPword = sharedPref.getString("email_password", "pokemanz$$$808303");
		
		Vibrator v = (Vibrator) thisContext.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 1 second
		if (android.os.Build.VERSION.SDK_INT >= 11)
			if(v.hasVibrator()) v.vibrate(1000);
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
		String weatherData;
		
		if (reportStuff[2])
		{
			Weather handleWeather = new Weather(thisContext);
			weatherData = handleWeather.weather();
		}
		else
			weatherData = "";
		
		String RSSData;
		if (reportStuff[0])
		{
			RSS reader = new RSS(thisContext);
			RSSData = reader.readRSS();
		}
		else
			RSSData = "";
		
		
		String emailTextData;
		if (reportStuff[3]) 
			emailTextData = socialResponses(emailUser, emailPword );
		else
			emailTextData = "";
		//2. Turn values into strings, put info into 
		//   informationString
		
		if (android.os.Build.VERSION.SDK_INT >= 14 && reportStuff[1])
		{
			Calendar calendar = new Calendar();
			String calendarData = calendar.getCalendarStuff(thisContext);
			informationString = weatherData + " " + emailTextData + " " + calendarData + " " + RSSData;
		}
		else
			informationString = weatherData + " " + emailTextData + " " + RSSData + ". ";
		


		return informationString;

	}

	protected void onPostExecute(String result)
	{

		nuanceObject.speakTheString(result, thisContext);
	
	}

	protected String socialResponses(String username, String password)
	{	
		JavaMail jm = new JavaMail();
		int unreadEmails = jm.jm(username, password);

		SMSCount smsData = new SMSCount();
		int unreadTexts = smsData.getSMSCount(thisContext);

		//String smsDataString = smsData.getSMSstring(thisContext);

		String noMessagesArray[] = {
				"No new messages... what a surprise.",
				"No new messages... Time to invest in tissues...",
				"Good job getting no new texts or emails."
		};

		String fewMessagesArray[] = {
				"%d emails and %d texts. No surprises there...",		
				"%d emails and %d texts... Bet you wish you were more popular...",
				"You have %d emails and %d texts... you should work on being nicer so you can get more"				
		};

		String manyMessagesArray[] = {
				"Ooh, looks like mister popular has %d emails and %d texts today!",
				"%d emails and %d texts. Clearly SOMEONE still cares about you.",
				"Wow, %d emails and %d texts! That's a surprise!"
		};

		String returnResponse = new String();

		if  ((unreadEmails + unreadTexts) < 4)
		{
			if (unreadEmails == 0 && unreadTexts == 0)
			{
				returnResponse = noMessagesArray[(int) ((Math.random() * noMessagesArray.length) % noMessagesArray.length)];

			}
			else
			{
				returnResponse = String.format(fewMessagesArray[(int) ((Math.random() * fewMessagesArray.length) % fewMessagesArray.length)], unreadEmails, unreadTexts);

			}
		}

		else
		{
			returnResponse = String.format(manyMessagesArray[(int) ((Math.random() * manyMessagesArray.length) % manyMessagesArray.length)], unreadEmails, unreadTexts);

		}

		return returnResponse;


	}
}

