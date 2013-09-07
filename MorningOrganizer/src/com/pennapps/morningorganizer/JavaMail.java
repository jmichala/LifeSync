package com.pennapps.morningorganizer;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import android.util.Log;
 
@SuppressWarnings("all")
public class JavaMail {
 public JavaMail(){}
 
 public String jm2(){
		int ret = jm();
		Log.i("info", "jm(): successfully returned ret=" + ret);
		String output = "";
		if (ret>0)
			output = "You have " + ret + " unread emails.";
		return output;
 }
 
 public int jm(){
	Log.i("info","jm() started");
	int ret=0;
	Properties props = System.getProperties();
	props.setProperty("mail.store.protocol", "imaps");
	try
	{
		Session session = Session.getDefaultInstance(props, null);
		 session.setDebug(true);
		 Store store = session.getStore("imaps");
		 store.connect("imap.gmail.com", "pennapps.morningorganizer@gmail.com", "pokemanz$$$808303");
		 Log.i("info","javamail store: " + store);
		 
		 Folder inbox = store.getFolder("Inbox");
		 inbox.open(Folder.READ_ONLY);
		 FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		 Message messages[] = inbox.search(ft);
		 Log.i("info","MESSAGE NUMBER=" + messages.length);
		 ret = messages.length;
	}
	catch (Exception e)
	{
		Log.i("error","jm(): Failed to connect to email.");
		ret=0;
	}
	 return ret;
 }
 /*
  * JavaMail jm = new JavaMail();
		int ret = jm.jm();
		Log.i("info", "jm(): successfully returned ret=" + ret);
		String output = "";
		if (ret>0)
			output = "You have " + ret + " unread emails.";
  * */
}