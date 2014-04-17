/**
 * 
 */
package com.android.d0d0.jxta.impl.membership.pse;

import android.app.Activity;
import android.view.View;
import android.content.Context;
import com.android.d0d0.R;

//import android.annotation.TargetApi;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.MotionEvent;

//import com.android.d0d0.util.SystemUiHider;



/**
 * @author d0d0
 *
 */
public class DialogAuthenticatorActivity extends Activity  {

	private static Context context;
	
	public String KeyStorePassword;
	public String IdentityName;
	public String IdentityPassword;
	boolean OK_Button_Clicked = false;
	boolean Cancel_Button_Clicked = false;
	
	
	/*
	 * When the OK Button is pressed
	 */
	
	public void OK_Button(View view){
		
		OK_Button_Clicked = true;
		KeyStorePassword = findViewById(R.id.editText1).toString();
		IdentityName = findViewById(R.id.editText2).toString();
		IdentityPassword = findViewById(R.id.EditText01).toString();
		
	
		
		
	}
	
	
	/*
	 * When the Cancel Button is pressed
	 */
	
	void Cancel_Button(){
		
	}
	
	protected void onActivityResult(){
		
	}
	
}
