/**
 * 
 */
package com.android.d0d0.SpeechService;

import java.util.logging.Logger;

import net.jxta.logging.Logging;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.speech.SpeechRecognizer;

import com.android.d0d0.ApplicationClass;
import com.android.d0d0.R;
import com.android.d0d0.droidEdge;

import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.mediator.Mediator;;

/**
 * @author d0d0
 *
 */
public class d0d0SpeechService extends Service {




	public static final String ACTIVATION_TYPE_INTENT_KEY = "ACTIVATION_TYPE_INTENT_KEY";
	public static final String ACTIVATION_RESULT_INTENT_KEY = "ACTIVATION_RESULT_INTENT_KEY";
	public static final String ACTIVATION_RESULT_BROADCAST_NAME = "com.android.d0d0.SpeechService";
	public static final String ACTIVATION_STOP_INTENT_KEY = "ACTIVATION_STOP_INTENT_KEY";
	public static final int NOTIFICATION_ID = 10298;
	public static boolean p2pStarted = false; 
	
	 // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
	
	public IMediator mediator;

	private boolean isStarted;
	private SpeechRecognizer recognizer;
	private Context context = ApplicationClass.getApplicationClassContext();
	private final static Logger LOG = Logger.getLogger(d0d0SpeechService.class.getName());
	
	
	
	public d0d0SpeechService() {
		// TODO Auto-generated constructor stub

		p2pStarted = false;
		

	}
	
	/*
	 * 
	 * The FullScreenActivity will start speech service and then bind to this service
	 * when the bind is successful, it will provide the mediator
	 * 
	 */
	
	public void setMediator(IMediator mediator) {
		// TODO Auto-generated constructor stub

		this.mediator = mediator;

	}

/*	
 * 
 * This method contains all the necessary methods that do the main work of this service
 * 
 */ 
	public void startAllServices(){
		
		Logging.logCheckedInfo(LOG,"Starting JXTA using the mediator");
		mediator.startJXTA();
		System.out.println(mediator.toString());
		Logging.logCheckedInfo(LOG,"Starting the voice detector using the mediator");
		mediator.startVoiceDector();
		startForeground(NOTIFICATION_ID, getNotification());
		isStarted = true;
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Logging.logCheckedInfo(LOG,"Starting onStartCommand");
		
		
		
		

		if(intent != null)
		{

			if (intent.hasExtra(ACTIVATION_STOP_INTENT_KEY)){
				// Service needs to be stopped

				isStarted = false;
				
			}else if(!isStarted){
				//Service has not been started, so start it


			}

		}
		return Service.START_NOT_STICKY;
	}

	
	
	
	 public class LocalBinder extends Binder {
		
		 public d0d0SpeechService getService() {
	            // Return this instance of LocalService so clients can call public methods
	            return d0d0SpeechService.this;
	        }
		 
		 
	    }



	private Notification getNotification() {
		// TODO Auto-generated method stub

		Logging.logCheckedInfo(LOG,"Sending Notification about the Service");

		// determine label based on the class
		String name = "d0d0 Speech Service";
		String message = "d0d0 Speech Service";
		String title = "d0d0 Speech Service";

		PendingIntent pi = PendingIntent.getService(this, 0, makeServiceStopIntent(this),  0);

		Notification notification;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			Notification.Builder builder = new Notification.Builder(this);
			builder.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis()).setTicker(message)
			.setContentTitle(title).setContentText(message)
			.setContentIntent(pi);
			notification = builder.getNotification();
		}
		else
		{
			notification =  new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis());
			notification.setLatestEventInfo(this, title, message, pi);

		}

		return notification;
	}

	private Intent makeServiceStopIntent(d0d0SpeechService d0d0SpeechService) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context, d0d0SpeechService.class);
		i.putExtra(ACTIVATION_STOP_INTENT_KEY, true);
		return i;

	}


	@Override
	public void onCreate(){

		super.onCreate();
		isStarted = false;
		Logging.logCheckedInfo(LOG,"Starting onCreate");
		//		android.os.Debug.waitForDebugger();

	}

	@Override
	public void onDestroy(){

	}

	@Override
	public void onLowMemory(){

		Logging.logCheckedInfo(LOG,"Starting onLowMemory");

	}

	

	
	

	/**
	 * 
	 */
	protected void d0d0P2PNewThread() {
		Thread threadService = new Thread(){

			public void run(){
				droidEdge d0d0p2p = new droidEdge();
				d0d0p2p.startJXTA();
			}
		};

		threadService.start();
	}


	
	
	
	
}
