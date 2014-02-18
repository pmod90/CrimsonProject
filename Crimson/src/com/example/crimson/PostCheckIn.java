package com.example.crimson;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PostCheckIn extends Activity {
	NumberPicker altitudePicker;
	
	int altitude;
	int alt;
	int testListItem1;
	int testListItem2;
	int testListItem3;
	public String name;
	Context context;
	
	int otherplayers;
	public double pLat;
	public double pLong;
	  List<Integer> al;
	  List<Integer> al1;
	  List<Integer> al2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postcheckin);
		startService(new Intent(getBaseContext(),BattleChallengeService.class));
		startService(new Intent(getBaseContext(),AIAttackService.class));
		
		Intent i = getIntent();
		name = i.getExtras().getString("currentPlaceName");
		  Toast.makeText(getApplicationContext(),
                  "Successfully Checked in at "+name,
                  Toast.LENGTH_LONG).show();
		  
		  //Ed's addition when user checks into a healing spot their health gets reset to 100
		  ParseQuery<ParseObject> checkinPlacesQuery = ParseQuery.getQuery("checkinPlaces");
			 checkinPlacesQuery.whereEqualTo("placeName", name);
			 checkinPlacesQuery.whereEqualTo("isHealingSpot", true);
			 checkinPlacesQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			   public void done(ParseObject object, ParseException e) {
			     if (object == null) {
			       Log.d("healingspot", "The healingspot request failed.");
			      
			     } 
			     else {
			       Log.d("healingspot", "Retrieved the object.");
			       
			       ParseUser currentUser = ParseUser.getCurrentUser();
			       currentUser.put("health", 100);
			       currentUser.saveInBackground();
			       
			       Toast.makeText(getApplicationContext(),
			                  "You have been healed! Your health is now 100.",
			                  Toast.LENGTH_LONG).show();
			       
			     }
			   }
			   
			   
			   
			 });
		  
		  
		  altitudePicker = (NumberPicker) findViewById(R.id.altitudePicker);
		  
		  ParseQuery<ParseObject> query = ParseQuery.getQuery("checkinPlaces");			 
		  query.whereEqualTo("placeName",name);
		 
		  
		  
		  
		  query.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject object, ParseException e) {
		      if (object == null) {
		        Log.d("score", "The getFirst request failed.");
		      } else {
		    	  alt = object.getInt("altitude");
		    	  altitudePicker.setMaxValue(alt);
		    	  altitudePicker.setMinValue(1);
		    	  altitudePicker.setValue(alt - 2);
		    	  
		    	  al = new ArrayList<Integer>();
		    	  al = object.getList("resourcesGold");
		        testListItem1 = al.get(altitudePicker.getValue() - 1);
		        TextView locationGold = (TextView) findViewById(R.id.locationGold);		        
		       locationGold.setText("Gold = "+testListItem1);
		       
		       al1 = new ArrayList<Integer>();
		       al1 = object.getList("resourcesLumber");
		        testListItem2 = al1.get(altitudePicker.getValue() - 1);
		        TextView locationLumber = (TextView) findViewById(R.id.locationLumber);
		        locationLumber.setText("Lumber = "+testListItem2);
		        
		        al2 = new ArrayList<Integer>();
			       al2 = object.getList("resourcesStone");
			        testListItem3 = al2.get(altitudePicker.getValue() - 1);
			        TextView locationStone = (TextView) findViewById(R.id.locationStone);
			        locationStone.setText("Stone = "+testListItem3);
		        
		      }
		    }
		  });
		  
		  
		  Intent i1 = getIntent();
			pLat = i1.getExtras().getDouble("lat");
			pLong = i1.getExtras().getDouble("long");
			ParseUser currentUser = ParseUser.getCurrentUser();
			String place = currentUser.getString("lastCheckinPlace");
		  ParseGeoPoint geopoint = new ParseGeoPoint (pLat, pLong);


		  ParseQuery<ParseObject> query1 = ParseQuery.getQuery("ForagingStatus");
		  query1.whereEqualTo("isForaging",true);
		  query1.whereEqualTo("foragingPlace", place);
		  query1.whereNotEqualTo("username", currentUser.get("username").toString());
		  

		  
		  query1.findInBackground(new FindCallback<ParseObject>(){
			  public void done(List<ParseObject> playerCount, ParseException e){
				  if (e==null){
					  otherplayers = playerCount.size();
					  TextView otherPlayers = (TextView) findViewById(R.id.otherPlayers);
					  otherPlayers.setText("There are "+ otherplayers+" other players foraging at this location");
					 
					  
				  }
				  else{
					  Log.d("score", "Error in retrieving foragers");
				  }
			  }
		  });
				  
		  
		  
		
		  altitudePicker.setOnValueChangedListener(new OnValueChangeListener(){

			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				
				TextView locationGold = (TextView) findViewById(R.id.locationGold);
				testListItem1 = al.get(altitudePicker.getValue() - 1);
				locationGold.setText("Gold = "+testListItem1);
				
				TextView locationLumber = (TextView) findViewById(R.id.locationLumber);
				testListItem2 = al1.get(altitudePicker.getValue() - 1);
				locationLumber.setText("Lumber = "+testListItem2);
				
				
				TextView locationStone = (TextView) findViewById(R.id.locationStone);
				testListItem3 = al2.get(altitudePicker.getValue() - 1);
				locationStone.setText("Stone = "+testListItem3);
			}});
		

		  
	}
	

	public void saveBattleStuff(int a,int d)
	{
		ParseUser currentUser = ParseUser.getCurrentUser();
		String uname = currentUser.getUsername();
		final Number attack = a;
		final Number defense = d;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", uname);
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> objects, ParseException e) {
		    if (e == null) {
		    	ParseUser user = objects.get(0);
		    	int a = attack.intValue();
		    	int d = defense.intValue();
		    	List<Integer> artifact = user.getList("artifactsOwned");
		    	int artifactPoints;
		    	if(artifact!=null)
		    	{
		    	artifactPoints = artifact.size()*50;
		    	}
		    	else
		    	{
		    		 artifactPoints = 0;
		    	}
		    	int bPoints = a + d + artifactPoints;
		    	Number battlePoints = (Number) bPoints;
		    	user.put("battlePoints",battlePoints);
		    	user.saveInBackground();
		        // The query was successful.
		    } else {
		        // Something went wrong.
		    }
		  }
		});
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("attackResources");
//		query.whereEqualTo("uname", uname);
//		query.findInBackground(new FindCallback<ParseObject>() {
//		    public void done(List<ParseObject> objects, ParseException e) {
//		        if (e == null) {
//		            
//		        	ParseObject object = objects.get(0);
//		        	object.put("attack", attack);
//		        	object.put("defense", defense);
//		        	
//		        	object.saveInBackground();
//		        	//Log.d("score", "Retrieved " + scoreList.size() + " scores");
//		        } else {
//		            Log.d("score", "Error: " + e.getMessage());
//		        }
//		    }
//		});
	}
	

	public void ambushClicked(View v)
	{
		ParseUser currentUser = ParseUser.getCurrentUser();
		String uname = currentUser.getUsername();
		
		ParseQuery<ParseObject> queryAlt = ParseQuery.getQuery("PlaceBattleAttributes");
		queryAlt.whereEqualTo("Level", altitudePicker.getValue());
		queryAlt.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> objects, ParseException e) {
		        if (e == null) {
		            
		        	ParseObject lvl = objects.get(0);
		        int attack = lvl.getNumber("attack").intValue();
		        int defense = lvl.getNumber("defense").intValue();
		        	saveBattleStuff(attack, defense);
		        	//Log.d("score", "Retrieved " + scoreList.size() + " scores");
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }
		});
		
//		ParseQuery<ParseObject> query = ParseQuery.getQuery("attackResources");
//		query.whereEqualTo("uname", uname);
//		query.findInBackground(new FindCallback<ParseObject>() {
//		    public void done(List<ParseObject> objects, ParseException e) {
//		        if (e == null) {
//		            
//		        	ParseObject object = objects.get(0);
//		        	object.put("lastAltitude", altitudePicker.getValue());
//		        	object.saveInBackground();
//		        	//Log.d("score", "Retrieved " + scoreList.size() + " scores");
//		        } else {
//		            Log.d("score", "Error: " + e.getMessage());
//		        }
//		    }
//		});
		
		
		
		Intent intent = new Intent(this,Ambush.class);
		    startActivity(intent);
	}
	

	public void checkArtifactsClicked(View v)
	{
		   Intent intent = new Intent(this,CheckArtifacts.class);
		   intent.putExtra("currentPlaceName",name);
		   startActivity(intent);
	}

	public void forageClicked(View v)
	{
			altitude = altitudePicker.getValue()-1;
			ParseUser CurrentUser = ParseUser.getCurrentUser();
			CurrentUser.put("lastAltitude", altitudePicker.getValue());
			CurrentUser.saveInBackground();
			
			String uname = CurrentUser.getUsername();
			
			ParseQuery<ParseObject> queryAlt = ParseQuery.getQuery("PlaceBattleAttributes");
			queryAlt.whereEqualTo("Level", altitudePicker.getValue());
			queryAlt.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> objects, ParseException e) {
			        if (e == null) {
			            
			        	ParseObject lvl = objects.get(0);
			        int attack = lvl.getNumber("attack").intValue();
			        int defense = lvl.getNumber("defense").intValue();
			        	saveBattleStuff(attack, defense);
			        	//Log.d("score", "Retrieved " + scoreList.size() + " scores");
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
//			Intent intent1 = new Intent(ForageService.class.getName());	
//			intent1.putExtra("ArrayIndex", altitudePicker.getValue()-1);
//			intent1.putExtra("LocationName", name);
			startService(new Intent(getBaseContext(), ForageService.class));
		
	}
	


}
