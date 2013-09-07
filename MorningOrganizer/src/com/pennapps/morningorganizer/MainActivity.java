package com.pennapps.morningorganizer;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements View.OnClickListener {
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
	
	//Do all internet-related stuff
	private void doStuff(Context c)
	{
		new GetInfoTask().execute(c);
		
		//Debug message to make sure alarm shit is working
		Toast.makeText(c, "The alarm worked!", Toast.LENGTH_LONG).show();
	}
	
	//Sets up the Alarm Manager
	private void setup() {
		
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i)
			{
				doStuff(c);
			}
		};
		registerReceiver(broadcastReceiver, new IntentFilter("com.pennapps.morningorganizer"));
		
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.pennapps.morningorganizer"), 0);
		
		alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
	}

	public void onClickTwo(View v) {
		Context c = v.getContext().getApplicationContext();
		FragmentManager fm = getSupportFragmentManager();

		ScheduleDialog dialog = new ScheduleDialog();
		/*dialog.setOnDialogResultListener(new ScheduleDialog.OnDialogResultListener() {
			
			@Override
			public void onReturn(int[] returnval) {
				Context c = getApplicationContext();
				doStuff(c);
			}
		});*/
		dialog.show(fm, "scheduler");
		//Toast.makeText(c, "Scheduled Alarm!", Toast.LENGTH_LONG).show();
	}
	
	public void onUserSelectValue(int[] returnVal)
	{
		Context c = getApplicationContext();
		Toast.makeText(c, "Scheduled Alarm for " + Integer.toString(returnVal[0]) + ":" + Integer.toString(returnVal[1]), Toast.LENGTH_LONG).show();
		alarmTimeCal.set(Calendar.HOUR_OF_DAY, returnVal[0]);
		alarmTimeCal.set(Calendar.MINUTE, returnVal[1]);
		alarmTimeCal.set(Calendar.SECOND, 0);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeCal.getTimeInMillis(), pendingIntent);
	}
	
	@Override
	public void onClick(View v) {
		//Do stuff right now
		//Set up time for alarm

		Context c = v.getContext().getApplicationContext();
		
		/*
		TimePicker timePicker = (TimePicker)(findViewById(R.id.timePicker));
		alarmTimeCal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		alarmTimeCal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		alarmTimeCal.set(Calendar.SECOND, 0);
		*/

		Toast.makeText(c, "Preparing...", Toast.LENGTH_LONG).show();
		doStuff(c);
		//Start the alarm manager to wake up the app
		//alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeCal.getTimeInMillis(), pendingIntent);
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
