package com.android.d0d0;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.mediator.Mediator;

import com.android.d0d0.SpeechService.d0d0SpeechService;
import com.android.d0d0.SpeechService.d0d0SpeechService.LocalBinder;
import com.android.d0d0.util.SystemUiHider;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	
	public static Context context = ApplicationClass.getApplicationClassContext();
	
	public static IMediator mediator = new Mediator() ;
	
	public d0d0SpeechService speechService; 
	static boolean mBound = false;
	
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);

//		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		
	
		
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(10);
//		startSpeechService();
//		startJxtaThread();
//		startMapActivity();
		startReportGridActivity();
		
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
//			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	
	
	
	
	public void VoiceCommandButton(View view){
		 boolean on = ((ToggleButton) view).isChecked();
		
		 if(on) {
			 	//d0d0Edge.startSpeechService();
		 }else {
			 	
			 //d0d0Edge.stopSpeechService();
			 			 
		 }
		
	}

	public void DisplayOffButton(View view){
		
		boolean on = ((ToggleButton) view).isChecked();
	}
	
	
	public void startMapActivity(){
		Intent startMapIntent = new Intent(context, MapActivity.class);
		startActivity(startMapIntent);
		
	}
	

	public void startReportGridActivity(){
		Intent startReportGridIntent = new Intent(context, ReportGridActivity.class);
		startActivity(startReportGridIntent);
		
	}
	
	/**
	 * 
	 */
	public  void startSpeechService() {
		Thread threadService = new Thread(){
		
			public void run(){
				Intent startServiceIntent = new Intent(context, d0d0SpeechService.class);
				mBound = bindService(startServiceIntent, speechServiceConnection,Context.BIND_AUTO_CREATE);
				
				if(mBound == true){
				
				}
			
			}
		};
		 
		threadService.start();
		
	}

	/**
	 * 
	 */	
	public static void stopSpeechService() {
				Intent stopServiceIntent = new Intent(context, d0d0SpeechService.class);
				context.stopService(stopServiceIntent);		
	}
	
	

	/** Defines callback for service binding, passed to bindService() */
     private ServiceConnection speechServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder =  (LocalBinder) service;
            speechService = binder.getService();
            mBound = true;
            speechService.setMediator(mediator);
			speechService.startAllServices();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

		
    };
    


    
    /**
	 * 
	 */
	public  void startJxtaThread() {
		Thread threadJxta = new Thread(){
		
			public void run(){
				
				mediator.startJXTA();
				
			}
		};
		 
		threadJxta.start();
		
	}
    
}
