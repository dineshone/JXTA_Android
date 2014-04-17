package com.android.d0d0;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;


public class ApplicationClass extends Application {
	
	public static Context context = null;
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		context = getApplicationContext();
	}
	
	public void onConfigurationChange(Configuration newConfig){
		
		super.onConfigurationChanged(newConfig);
	}
	
	
	
	public ApplicationClass() {
		// TODO Auto-generated constructor stub
		
		
	}
	
	
	public static Context getApplicationClassContext(){
		
		return context;
	}
	
}
