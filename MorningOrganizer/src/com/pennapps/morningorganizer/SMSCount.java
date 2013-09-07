package com.pennapps.morningorganizer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SMSCount {
	
	static int unreadMessagesCount = -1;
	
	public int getSMSCount (Context context) {
		if (unreadMessagesCount == -1)
		{
			final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");
			Cursor inboxCursor = context.getContentResolver().query(SMS_INBOX_URI, null, "read = 0", null, null);
			unreadMessagesCount = inboxCursor.getCount();
			inboxCursor.close();
			final Uri MMS_INBOX_URI = Uri.parse("content://mms/inbox");
			Cursor mInboxCursor = context.getContentResolver().query(MMS_INBOX_URI, null, "read = 0", null, null);
			unreadMessagesCount += mInboxCursor.getCount();
			mInboxCursor.close();
		}
		return unreadMessagesCount;
	}
	
	public String getSMSstring (Context context) {
		int numOfEmails = getSMSCount(context);
		String returnString = String.format("You have %d unread texts.", numOfEmails);
		return returnString;
		
	}
}
