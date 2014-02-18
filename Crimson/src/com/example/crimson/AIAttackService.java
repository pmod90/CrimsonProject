package com.example.crimson;

import java.util.Timer;
import java.util.TimerTask;

import com.example.crimson.BattleChallengeService.BackgroundQuery;
import com.parse.ParseUser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AIAttackService extends Service {

	String aiName = "Tiger";
	int aiAttack = 500;
	int aiDefense = 500;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;

	}
	public int onStartCommand(Intent intent,int flags,int startId)
	{ 
		
		Random r = new Random();
		 int randomInt=r.nextInt(180000-120000) + 120000;
		
		 //Toast.makeText(this, "AI attack has started", Toast.LENGTH_LONG).show();
		 ParseUser currentUser = ParseUser.getCurrentUser();
		
		 Timer timer = new Timer();
		 timer.schedule(new BackgroundQuery(),randomInt, randomInt);
		
		 return START_STICKY;
	}
	
	public class BackgroundQuery extends TimerTask
	{

		@Override
		public void run() {
			
			Random r = new Random();
			 int i1=r.nextInt(6-1) + 1;
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> aiQuery = ParseQuery.getQuery("AI");
			 aiQuery.whereEqualTo("ID", i1);
			 aiQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			   public void done(ParseObject object, ParseException e) {
			     if (object == null) {
			       Log.d("score", "The getFirst request failed.");
			     } else {
			       Log.d("score", "Retrieved the object.");
			       aiName = object.getString("AIName");
			       aiAttack = object.getInt("Attack");
			       aiDefense = object.getInt("Defense");
			       
			       /*
			       ParseFile profile = (ParseFile)object.get("photo");
				   profile.getDataInBackground(new GetDataCallback(){

							@Override
							public void done(byte[] data, ParseException e) {
								// TODO Auto-generated method stub
								Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
								
								
								
							}});
			        */		
			       
			     }
			   }
			 });

			Intent dialog = new Intent(getApplicationContext(), AIDialogActivity.class); 
    		 	dialog.putExtra("aiName", aiName);
    		 	dialog.putExtra("aiAttack", aiAttack);
    		 	dialog.putExtra("aiDefense", aiDefense);
				dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		
		startActivity(dialog);
		}
	
	}
}
