package com.pennapps.morningorganizer;

import java.util.Date;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.format.Time;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Calendar {
	
	public String getCalendarStuff (Context context) {
/*	
	String[] projection = 
		      new String[]{
		            Calendars._ID, 
		            Calendars.NAME, 
		            Calendars.ACCOUNT_NAME, 
		            Calendars.ACCOUNT_TYPE};
			long begin = // starting time in milliseconds
			long end = // ending time in milliseconds
			String[] proj = 
			      new String[]{
			            Instances._ID, 
			            Instances.BEGIN, 
			            Instances.END, 
			            Instances.EVENT_ID};
			Cursor cursor = 
			      Instances.query(context.getContentResolver(), proj, begin, end);
			if (cursor.getCount() > 0) {
			   // deal with conflict
			} */
			Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
		            .buildUpon();
		Time start = new Time();
		start.setToNow();
		ContentUris.appendId(eventsUriBuilder, start.toMillis(false));
		ContentUris.appendId(eventsUriBuilder, (start.toMillis(false) + 86400000));
		Uri eventsUri = eventsUriBuilder.build();
		Cursor cursor = null;       
		cursor = context.getContentResolver().query(eventsUri, null, null, null, CalendarContract.Instances.DTSTART + " ASC");
		Log.i("cursorColumnIndices", "initialized");
		String toReturn = new String();
		int i = 0;
		if (cursor.moveToFirst())
		{
			String arrayOfNames[] = cursor.getColumnNames(); 
			
			for (i = 0; i < arrayOfNames.length; i++)
			{
				//Log.i("cursorColumnTitles", arrayOfNames[i]);
			}
			i = 1;
			String nameEvent = cursor.getString(cursor.getColumnIndex("title"));
			//Log.i("cursorData", nameEvent);
			//Log.i("cursorData", cursor.getString(cursor.getColumnIndex("dtstart")));	
			Date d = new Date(Long.decode(cursor.getString(cursor.getColumnIndex("dtstart"))));
			int hours = d.getHours();
			int minutes = d.getMinutes();
		//	Log.i("cursorData", Integer.toString(hours) + ":" + Integer.toString(minutes));
			if (hours >= 12)
			{				
				if (minutes != 0)
				{
					toReturn = toReturn + "You have " + nameEvent + " at " + (hours - 12) + ":" + minutes + " PM... ";
				}
				else
				{
					toReturn = toReturn + "You have " + nameEvent + " at " + (hours - 12) + " PM... ";
				}
			}
			else
			{
			if (hours == 0) { hours = 12; }
				 if (minutes != 0)
				{	
					toReturn = toReturn + "You have " + nameEvent + " at " + (hours) + ":" + minutes + " AM... ";
				}
				else
				{
					toReturn = toReturn + "You have " + nameEvent + " at " + (hours) + " AM... ";	
				}
			}
			while (cursor.moveToNext())
			{
				i++;
				//Log.i("cursorData", cursor.getString(cursor.getColumnIndex("title")));
				//Log.i("cursorData", cursor.getString(cursor.getColumnIndex("dtstart")));
				d = new Date(Long.decode(cursor.getString(cursor.getColumnIndex("dtstart"))));
				hours = d.getHours();
				minutes = d.getMinutes();
				nameEvent = cursor.getString(cursor.getColumnIndex("title"));
				//Log.i("cursorData", Integer.toString(hours) + ":" + Integer.toString(minutes));
				if (hours >= 12)
				{				
					if (minutes != 0)
					{
						toReturn = toReturn + "You have " + nameEvent + " at " + (hours - 12) + ":" + minutes + " PM... ";
					}
					else
					{
						toReturn = toReturn + "You have " + nameEvent + " at " + (hours - 12) + " PM... ";
					}
				}
				else
				{
				if (hours == 0) { hours = 12; }
					if (minutes != 0)
					{	
						toReturn = toReturn + "You have " + nameEvent + " at " + (hours) + ":" + minutes + " AM... ";
					}
					else
					{
						toReturn = toReturn + "You have " + nameEvent + " at " + (hours) + " AM... ";	
					}
				}
			}
			Log.i("cursorData", toReturn);
		}
		toReturn = "You have " + i + " events today. " + toReturn; 
		return toReturn;
	}
}
