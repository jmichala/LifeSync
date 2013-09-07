package com.pennapps.morningorganizer;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	//Time length of one second
	final static private long SECOND = 1000;
	
	Calendar alarmTimeCal = Calendar.getInstance();
	
	PendingIntent pendingIntent;
	BroadcastReceiver broadcastReceiver;
	AlarmManager alarmManager;
	String informationString = "";
	Handler errorHandler;
	Nuance nuanceObject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nuanceObject = new Nuance();
		//Assign listener to the button
		findViewById(R.id.startButton).setOnClickListener(this);
		
		setup();
	}
	
	//Sets up the Alarm Manager
	private void setup() {
		
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i)
			{
				//1. Run weather, mail, etc. functions and get input
				Weather handleWeather = new Weather();
				String[] weatherData = handleWeather.weather();
				
				
				//2. Turn values into strings, put info into 
				//   informationString
			/*	informationString = "High: " + weatherData[0].toString() 
							+ "Low: " + weatherData[1].toString() + " " + weatherData[2];
		*/		//3. Hemanth put your shit here
				String debugString = "hi world how are you today?";
				
				nuanceObject.initializeSpeechKit(c, errorHandler);

				Toast.makeText(c, "initialized", Toast.LENGTH_LONG).show();
				nuanceObject.speakTheString(debugString, c);

				
				//Debug message to make sure alarm shit is working
				Toast.makeText(c, "The alarm worked!", Toast.LENGTH_LONG).show();
			}
		};
		registerReceiver(broadcastReceiver, new IntentFilter("com.pennapps.morningorganizer"));
		
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.pennapps.morningorganizer"), 0);
		
		alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
	}

	@Override
	public void onClick(View v) {
		//Set up time for alarm
		TimePicker timePicker = (TimePicker)(findViewById(R.id.timePicker));
		alarmTimeCal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		alarmTimeCal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		alarmTimeCal.set(Calendar.SECOND, 0);
		
		//Start the alarm manager to wake up the app
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeCal.getTimeInMillis(), pendingIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy()
	{
		alarmManager.cancel(pendingIntent);
		unregisterReceiver(broadcastReceiver);
		nuanceObject.closeSpeechKit();
		super.onDestroy();
	}

}
