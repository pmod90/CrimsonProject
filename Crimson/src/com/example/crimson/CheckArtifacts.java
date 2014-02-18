package com.example.crimson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckArtifacts extends Activity {
	String name;
	List<Integer> aa;
	List<Integer> us;
	List<Integer> al;
	ListView artifactsList,ownedList;
	int artifactPosition,ownedPosition;
	//int artifactFlag = 0;
	final ParseUser currentUser = ParseUser.getCurrentUser();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkartifacts);    
		
		Intent i = getIntent();
		name = i.getExtras().getString("currentPlaceName");
		loadUserArtifacts();
		ParseQuery<ParseObject> q2 = ParseQuery.getQuery("checkinPlaces");
		q2.whereEqualTo("placeName", name);
		q2.findInBackground(new FindCallback<ParseObject>(){
			  public void done(List<ParseObject> artifactsAvailable, ParseException e){
				  if (e==null){
					  ParseObject checkinPlace = artifactsAvailable.get(0);
					  aa = checkinPlace.getList("artifactsAvailable");
					  Log.d("List", aa.toString());
					  loadPlaceArtifacts();
					  }
				  else{
					  Log.d("Crimson", "Error in retrieving Artifacts");
				  }
			  }
		  });
		
		final AlertDialog.Builder artifactDialog = new AlertDialog.Builder(this);
		artifactDialog.setMessage(R.string.wantArtifact);
		artifactDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	   //String username = currentUser.get("username").toString();
	        	 if(checkDuplicates())
	        	 {
		        	 if(al.size()<5)
		        	 {
		        	 al.add(artifactPosition);  
		        	 currentUser.put("artifactsOwned", al);
		        	 currentUser.put("artifactPower", calculatePower());
		        	 currentUser.put("battlePoints", currentUser.getNumber("battlePoints").intValue() + 50);
		        	 //*********
		        	 currentUser.saveInBackground();
		        	 Toast.makeText(getApplicationContext(),
	                         "Artifact Acquired!",
	                         Toast.LENGTH_LONG).show();
		        	 loadUserArtifacts();
		        	 }//end if
		        	 else{
		        		 Toast.makeText(getApplicationContext(),
		                         "Note: Artifact Limit is 5.",
		                         Toast.LENGTH_LONG).show();
		        	 }//end else
	        	 }
	           }
	       });
	artifactDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
		
		artifactsList = (ListView) findViewById(R.id.artifactsAvailable);
		artifactsList.setClickable(true);
		artifactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				artifactPosition=aa.get(position);
				AlertDialog dialog = artifactDialog.create();
				dialog.show();
			}
		});
		
		//----------------
		final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
		deleteDialog.setMessage(R.string.deleteArtifact);
		deleteDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	   //String username = currentUser.get("username").toString();
	        	   	 al.remove(al.indexOf(ownedPosition)); 
		        	 currentUser.put("artifactsOwned", al);
		        	 currentUser.put("artifactPower", calculatePower()); //*********
		        	 currentUser.saveInBackground();
		        	 Toast.makeText(getApplicationContext(),
	                         "Artifact given up.",
	                         Toast.LENGTH_LONG).show();
		        	 loadUserArtifacts();
		        	 
	           }
	       });
	deleteDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
	
		ownedList = (ListView) findViewById(R.id.artifactsOwned);
		ownedList.setClickable(true);
		ownedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ownedPosition=al.get(position);
				//ownedPosition = position;
				AlertDialog dialog = deleteDialog.create();
				dialog.show();
			}
		});
		//----------------
		
	} //End of onCreate()
	
	public void loadUserArtifacts(){
		//final ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQueryAdapter<ParseObject> adapter =
				  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
				    public ParseQuery<ParseObject> create() {
				    	//List<Integer> al;
						al = new ArrayList<Integer>();
				  	  	al = currentUser.getList("artifactsOwned");
				  	  Log.d("ListVish unsorted", al.toString());
				  	  Collections.sort(al);
				  	  Log.d("ListVish sorted", al.toString());	
				  	  	ParseQuery<ParseObject> query = new ParseQuery("ArtifactTypes");
				  	  	query.whereContainedIn("artifactID",al);
				  	  	return query;
				    } //End of Create()
				  }); //End of Adapter Intialization
		
		adapter.setTextKey("artifactName");
		adapter.setImageKey("artifactImage");
		
		
		
		ListView listView = (ListView) findViewById(R.id.artifactsOwned);
		listView.setAdapter(adapter);
	}//End of loadUserArtifacts
	
	public void loadPlaceArtifacts()
	{
		
	ParseQueryAdapter<ParseObject> adapter1 =
			  new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			    public ParseQuery<ParseObject> create() {
			  	  	ParseQuery<ParseObject> query1 = new ParseQuery("ArtifactTypes");
			  	  Log.i("list test","list test");
			  	  query1.whereContainedIn("artifactID",aa);
			  	  	return query1;
			    } //End of Create()
			  }); //End of Adapter Intialization
	
	adapter1.setTextKey("artifactName");
	adapter1.setImageKey("artifactImage");
	ListView listView1 = (ListView) findViewById(R.id.artifactsAvailable);
	listView1.setAdapter(adapter1);	
}
	public Boolean checkDuplicates(){
		for(int i = 0; i<al.size();i++)
		{
				if( al.get(i).equals(artifactPosition) )
				{
					Toast.makeText(getApplicationContext(),
	                         "Artifact is already owned by you!",
	                         Toast.LENGTH_LONG).show();
					return false;
				}//end if
		}//end i
		return true;
	}//end checkDuplicates
	
	public int calculatePower()
	{
		int defence = 0;
		int attack = 0;
		int artifactId;
		for(int i=0;i<al.size();i++)
		{
			artifactId = al.get(i);
			switch(artifactId)
			{
			case 1: defence += 0; //Sword
					attack += 30;
					break;
			case 2: defence += 30; //Shield
					attack += 0;
					break;
			case 3: defence += 10; //Hammer
					attack += 40;
					break;
			case 4: defence += 40; //Web
					attack += 10;
					break;
			case 5: defence += 20; //Trident
					attack += 10;
					break;	
			case 6: defence += 100; //Exilir
					attack += 0;
					break;
			case 7: defence += 30; //Axe
					attack += 40;
					break;
			case 8: defence += 10; //Spear
					attack += 60;
					break;
			case 9: defence += 70; //Spirits
					attack += 40;
					break;
			case 10: defence += 50; //Firebolt
					attack += 50;
					break;
			
			default:break;
			}
		} // End of For
		Toast.makeText(getApplicationContext(),
                "You artifact power is +"+String.valueOf(defence+attack)+"!",
                Toast.LENGTH_LONG).show();
		Log.d("test",String.valueOf(defence+attack));
		return(defence+attack);
	}
	
	
}//End of Class
