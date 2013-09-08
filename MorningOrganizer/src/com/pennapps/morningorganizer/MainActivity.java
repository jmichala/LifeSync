package com.pennapps.morningorganizer;

import java.util.Calendar;

<<<<<<< HEAD
import android.R;
=======

>>>>>>> 581d4061b063484fafd31546ad44328eb82df792
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

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
	
	WakeLock alarmLock;
	
	//Storage location of local file data
	public static final String PREFS_NAME = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nuanceObject = new Nuance();

		//Assign listener to the button
		findViewById(R.id.startButton).setOnClickListener(this);
		PreferenceManager.setDefaultValues(this, R.layout.preferences, false);
		setup();
	}
	
	//Do all internet-related stuff
	private void doStuff(Context c)
	{
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
			    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
			    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON      
			);
		GetInfoTask myTask = new GetInfoTask();
		myTask.execute(c);
		
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
		
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		alarmLock = pm.newWakeLock(pm.FULL_WAKE_LOCK, "Lock");
		
		
		
		Toast.makeText(c, "Scheduled Alarm for " + Integer.toString(returnVal[0]) + ":" + Integer.toString(returnVal[1]), Toast.LENGTH_LONG).show();
		Calendar oldCal = Calendar.getInstance();
		alarmTimeCal.set(Calendar.HOUR_OF_DAY, returnVal[0]);
		alarmTimeCal.set(Calendar.MINUTE, returnVal[1]);
		alarmTimeCal.set(Calendar.SECOND, 0);
		if (alarmTimeCal.compareTo(oldCal) <= 0)
			alarmTimeCal.add(Calendar.DATE, 1);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeCal.getTimeInMillis(), pendingIntent);
	}
	
	boolean go = true;
	
	@Override
	public void onClick(View v) {
		//Do stuff right now
		//Set up time for alarm

		Context c = v.getContext().getApplicationContext();
		ImageView test = (ImageView)v;
		if (go)
		{
			
		//test.setImageResource(R.drawable.stopbutton);
		go=false;
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
		else
		{
		//code to stop things
			go=true;
			//test.setImageResource(R.drawable.gobutton);
			nuanceObject.endSpeech();
		}
	}
	
	public boolean[] getSharedPrefs()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean[] reportStuff = new boolean[3];
		reportStuff[0] = sharedPref.getBoolean("news_sync", true);
		reportStuff[1] = sharedPref.getBoolean("cal_sync", true);
		reportStuff[2] = sharedPref.getBoolean("weather_sync", true);
		return reportStuff;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getOrder()){
			case 100:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case 200:
				startActivity(new Intent(this, AboutActivity.class));
				return true;
		}
		return false;
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
