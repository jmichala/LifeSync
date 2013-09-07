package com.pennapps.morningorganizer;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.SpeechKit;
import com.nuance.nmdp.speechkit.Vocalizer;


public class Nuance implements Vocalizer.Listener {

	public static final String TTS_KEY = "com.nuance.nmdp.sample.tts";	 //to change later?
    
    private static Vocalizer _vocalizer;
    private static Object _lastTtsContext = null;
    
	
	private static SpeechKit _speechKit;
    
    // Allow other activities to access the SpeechKit instance.
    SpeechKit getSpeechKit()
    {
        return _speechKit;
    }
    
    void initializeSpeechKit(Context appContext, Handler handler)
    {
    	if (_speechKit == null)
    	{
    		  _speechKit = SpeechKit.initialize(appContext, NuanceAppInfo.SpeechKitAppId, NuanceAppInfo.SpeechKitServer, NuanceAppInfo.SpeechKitPort, NuanceAppInfo.SpeechKitSsl, NuanceAppInfo.SpeechKitApplicationKey);
               _speechKit.connect();
               initializeTheVocalizer(appContext, handler);
    	}
    	
    }
    
    void initializeTheVocalizer(Context appContext, Handler handler)
    {
    	_vocalizer = (Vocalizer) _speechKit.createVocalizerWithLanguage("en_US", this, handler);
    }
    
    void speakTheString(String stringToSay, Context appContext)
    {
    	_vocalizer.speakString(stringToSay, appContext);
    }
    
    void closeSpeechKit()
    {
    	if (_speechKit != null)
    	{
    		_speechKit.cancelCurrent();
    		_speechKit.release();
    	}
    }

	@Override
	public void onSpeakingBegin(Vocalizer arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub

		Toast.makeText((Context) arg2, "The speech worked!", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onSpeakingDone(Vocalizer arg0, String arg1, SpeechError arg2,
			Object arg3) {
		if (arg2 != null)
		{
			System.out.printf(arg2.getErrorDetail());
		}
		
	}
	
}
