package com.example.crimson;



import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "QtZp32q9GfvXr9dISg1bHLiRCPUAQupJuvVLUR4A", "W55RAUhxQlpfQW43SeTQPvFxuJuIaupbpvrLRFxD"); 

		//ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		
		//Call PostCheckIn activity when a push notification is recieved
		PushService.setDefaultPushCallback(this, PostCheckIn.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}

}

