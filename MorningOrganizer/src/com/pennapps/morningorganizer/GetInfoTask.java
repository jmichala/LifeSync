package com.pennapps.morningorganizer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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
		Calendar calendar = new Calendar();
		String calendarData = calendar.getCalendarStuff(thisContext);
		
		String emailTextData = socialResponses();
		//2. Turn values into strings, put info into 
		//   informationString
		informationString = weatherData + " " + emailTextData + " " + calendarData;
		
		
		return informationString;
		
	}
	
	protected void onPostExecute(String result)
	{
		
		nuanceObject.speakTheString(result, thisContext);
	}
	
	protected String socialResponses()
	{	
		JavaMail jm = new JavaMail();
		int unreadEmails = jm.jm();
		
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

