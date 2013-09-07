package com.pennapps.morningorganizer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class ScheduleDialog extends DialogFragment {
	private TimePicker timePicker;
	int[] returnInt;
	
	public ScheduleDialog()
	{
		
	}
	

	
	OnDialogResultListener onDialogResultListener;
	
	
	public interface OnDialogResultListener{
		public abstract void onReturn(int[] returnval);
	}
	
	
	public void setOnDialogResultListener(OnDialogResultListener listener)
	{
		this.onDialogResultListener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.scheduler, container);
		//mScheduleButton = (Button)view.findViewById(R.id.scheduleButton);
		timePicker = (TimePicker)view.findViewById(R.id.timePicker1);
		getDialog().setTitle("Scheduler");
		
		Button mButton = (Button)view.findViewById(R.id.scheduleButton);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				returnInt = new int[2];
				returnInt[0] = timePicker.getCurrentHour();
				returnInt[1] = timePicker.getCurrentMinute();
				MainActivity callingActivity = (MainActivity) getActivity();
				callingActivity.onUserSelectValue(returnInt);
				//onDialogResultListener.onReturn(returnInt);
				endThis();
			}
		});
		
		return view;
	}

	public void endThis()
	{
		this.dismiss();
	}
	/*
	@Override
	public void onClick(View view) {
		Context c = view.getContext();
		
		
		if (view.getId() == R.id.scheduleButton) {
			Log.i("info", "Schedule made it");
			returnInt = new int[2];
			returnInt[0] = timePicker.getCurrentHour();
			returnInt[1] = timePicker.getCurrentMinute();
			MainActivity callingActivity = (MainActivity) getActivity();
			callingActivity.onUserSelectValue(returnInt);
			//onDialogResultListener.onReturn(returnInt);
			this.dismiss();
		}
	}*/
	
	public void onClickButton(View view) {
		returnInt = new int[2];
		returnInt[0] = timePicker.getCurrentHour();
		returnInt[1] = timePicker.getCurrentMinute();
		MainActivity callingActivity = (MainActivity) getActivity();
		callingActivity.onUserSelectValue(returnInt);
		//onDialogResultListener.onReturn(returnInt);
		this.dismiss();
	}
	
	
}
