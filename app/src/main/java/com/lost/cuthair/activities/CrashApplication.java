package com.lost.cuthair.activities;

import android.app.Application;

import com.lost.cuthair.utils.CustomCrashHandler;

public class CrashApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
		mCustomCrashHandler.setCustomCrashHandler(getApplicationContext());
	}
	
	

}
