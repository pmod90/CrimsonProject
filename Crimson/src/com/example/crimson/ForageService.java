package com.example.crimson;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ForageService extends Service {
	
	int locgold;
	int locstone;
	int loclumber;
	int usergold;
	int userstone;
	int userlumber;
	int otherplayers;
	int altitude;
	boolean isForageComplete=false;
	String name;
	String username;
	Timer t =new Timer();
	List<Integer> al;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int StartId)
	{
		final ParseUser CurrentUser = ParseUser.getCurrentUser();
		
//////////////////// Retrieving username, altitude, lastcheckin ////////////////////////		 
		  username = CurrentUser.getString("username");		
		altitude = CurrentUser.getInt("lastAltitude");
		name = CurrentUser.getString("lastCheckinPlace"); 
				
////////////////////     //retrieving User resource information  /////////////////////
		
		usergold = (Integer) CurrentUser.get("resourcesGold");
		userstone = (Integer) CurrentUser.get("resourcesStone");
		userlumber = (Integer) CurrentUser.get("resourcesLumber");
		
///////////////////setting isForaging = true for User table	/////////////////		
		CurrentUser.put("IsForaging", true);
		CurrentUser.saveInBackground();
		
////////////////////Retrieving gold,lumber and stone from location altitude //////////////////////////
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("checkinPlaces");			 
		query.whereEqualTo("placeName",name);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject object, ParseException e) {
		      if (object == null) {
		        Log.d("score", "The getFirst request failed.");
		      } else {
		    	  
		    	  		
		    	  al = new ArrayList<Integer>();
		    	  al = object.getList("resourcesGold");
		    	  if(altitude == 0)
		    	  {altitude = 1;}
		    	  locgold = al.get(altitude-1);
		    	  
		    	  }}});

///////////////////setting isForaging = true for UserCheckIn table /////////////	
		 // ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ForagingStatus");
		//  query2.whereEqualTo("username",username );
		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ForagingStatus");
		query2.whereEqualTo("username", username);
		query2.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> objects, ParseException e) {
		        if (e == null) {
		           ParseObject object = objects.get(0);
		           object.put("isForaging", true);
		           object.put("foragingPlace", name);
		           object.saveInBackground();
		        	// Log.d("score", "Retrieved " + scoreList.size() + " scores");
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		  
//		  query2.getFirstInBackground(new GetCallback<ParseObject>(){
//			  public void done(ParseObject object, ParseException e){
//				  if (e==null){
//					  object.put("IsForaging", true);
//					  object.saveInBackground();
//				  }
//				  else{
//					  Log.d("score", "Error in setting IsForaging true for UserCheckIn");
//				  }
//			  }
//		  });

///////////////////Retrieving otherplayers at location//////////////////////////
		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("User");
		  query1.whereEqualTo("lastCheckinPlace", name);
		  query1.whereEqualTo("isForaging", true);
		  
		  query1.findInBackground(new FindCallback<ParseObject>(){
			  public void done(List<ParseObject> playerCount, ParseException e){
				  if (e==null){
					  otherplayers = playerCount.size();  
				  }
				  else{
					  Log.d("score", "Error in retrieving foragers");
				  }
			  }
		  });
			
		Toast.makeText(this,"Foraging started", Toast.LENGTH_SHORT).show();
////////////////////    //Timer code for scheduled service run ////////////////////////////		
		t.schedule(new TimerTask(){
			@Override
			public void run(){
					Log.i("Timer in","Timer");
				
				///////////////////// Retrieving Location Gold,Stone and Lumber ///////////////////

							
							///////////////////////////    Incrementing User resources on foraging //////////

							
							///////////////////////////    Decrementing location resources      ////////////
							
							ParseQuery<ParseObject> query = ParseQuery.getQuery("checkinPlaces");			 
							query.whereEqualTo("placeName",name);
							query.getFirstInBackground(new GetCallback<ParseObject>() {
							    public void done(ParseObject object, ParseException e) {
							      if (object == null) {
							        Log.d("score", "The getFirst request failed.");
							      } else {
							    	  
							    	  		
//							    	  al = new ArrayList<Integer>();
//							    	  al = object.getList("resourcesGold");
//							    	  locgold = al.get(altitude-1);
							    	  if(locgold!=0)
//							    		  Toast.makeText(this,"No more resources to Forage", Toast.LENGTH_SHORT).show();
							    	  
							    	  {
							    		  
							    		 
//							    		  Log.i("nonsense","nonsense");
							    		  
							    		  ///////////////// Incrementing gold for user and decrementing gold at loc //////////////////
//							    		  usergold+= (0.05*locgold)/(otherplayers+1);
							    		  
								    	  locgold-= 10*(otherplayers+1);
								    	  usergold+=10;
								    	  CurrentUser.put("resourcesGold", usergold);
//								    	  Log.i("location gold", "locg"+locgold);
								    	  al.set(altitude-1, locgold);
								    	  object.put("resourcesGold", al);
//								    	  UpdateResources(locgold, altitude);
								    	  
								    	  ///////////////// Incrementing lumber for user and decrementing lumber at loc //////////////
								    	  al = object.getList("resourcesLumber");
								    	  loclumber = al.get(altitude-1);
								    	  userlumber+= 10;
								    	  CurrentUser.put("resourcesLumber", userlumber);
								    	  loclumber-= 10*(otherplayers+1);
								    	  al.set(altitude-1, loclumber);
								    	  object.put("resourcesLumber", al);
								    	  
								    	  ///////////////// Incrementing stone for user and decrementing stone at loc ///////////////
								    	  al = object.getList("resourcesStone");
								    	  locstone = al.get(altitude-1);
								    	  userstone+= 10;
								    	  CurrentUser.put("resourcesStone", userstone);
								    	  locstone-= 10*(otherplayers+1);
								    	  al.set(altitude-1, locstone);
								    	  object.put("resourcesStone", al);
								    	  object.saveInBackground();
								    	  CurrentUser.saveInBackground();
							    	  }
							    	  else
							    	  {
							    		  Log.i("zero gold","gold"+locgold);
								  		  
								  		  
								  		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ForagingStatus");
										query2.whereEqualTo("username", username);
										query2.findInBackground(new FindCallback<ParseObject>() {
										    public void done(List<ParseObject> objects, ParseException e) {
										        if (e == null) {
										           ParseObject object = objects.get(0);
										           object.put("isForaging", false);
										           object.saveInBackground();
										        	// Log.d("score", "Retrieved " + scoreList.size() + " scores");
										        } else {
										            Log.d("score", "Error: " + e.getMessage());
										        }
										    }
										});
										  
										CurrentUser.put("IsForaging", false);
								  		  CurrentUser.saveInBackground();
								  		  isForageComplete=true;
								  		 maketoast();
								  		  t.cancel();
							    	  }
							    	  												    	  							        
							      }
							    }

//								private void UpdateResources(int gold, int altitude) {
//									// TODO Auto-generated method stub
//									
//									TextView locationGold = (TextView) findViewById(R.id.locationGold);
//									locationGold.setText("Gold = "+gold);
//								}
							  });
							
//						}
//					}
//				});
							
			}
		}, 1000,5000);
		if(isForageComplete){
		Toast.makeText(this,"No more resources to Forage", Toast.LENGTH_SHORT).show();
		
		}
		
		return START_STICKY;
	}
	public void maketoast() {
		// TODO Auto-generated method stub
		Toast.makeText(this,"No more resources to Forage", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Toast.makeText(this, "Foraging Stopped", Toast.LENGTH_SHORT).show();
	}

}
