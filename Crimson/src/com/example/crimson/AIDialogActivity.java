package com.example.crimson;

import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AIDialogActivity extends Activity implements OnClickListener {

	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		
		final String aiName = i.getExtras().getString("aiName");
		final int aiAttack =i.getExtras().getInt("aiAttack");
		final int aiDefense =i.getExtras().getInt("aiDefense");
		
		 AlertDialog aiAttackDialog = new AlertDialog.Builder(this)
		 .setMessage("Attack: " + aiAttack + "\nDefense: " + aiDefense +  "\n\nIs trying to attack you. What would you like to do? \n" )
	   		.setTitle(aiName)
	   		//.setIcon(R.drawable.ic_launcher)
	   		.setPositiveButton("Attack", this)
	   		.setNeutralButton("Evade", this)
	   		.setCancelable(false)
	   		.create();
		 	aiAttackDialog.show();
		 
	   		
		 
	  }
	
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		
		final String aiName = i.getExtras().getString("aiName");
		final int aiAttack =i.getExtras().getInt("aiAttack");
		final int aiDefense =i.getExtras().getInt("aiDefense");
		
		switch(arg1){
		case DialogInterface.BUTTON_POSITIVE: // User clicked the attack button		

			ParseUser currentUser = ParseUser.getCurrentUser();
			int currentUserAttack = currentUser.getInt("battlePoints");
			
			
			int aiTotalPoints = aiAttack + aiDefense;
			//int aiTotalPoints = 500;
			
			if(currentUserAttack >aiTotalPoints){
				System.out.println("User Wins Battle");
				currentUser.increment("totalWins");
				currentUser.saveInBackground();
				Toast.makeText(getApplicationContext(),
						"You won the battle!",
						Toast.LENGTH_LONG).show();
			}
			else if(currentUserAttack<aiTotalPoints){
				System.out.println("User losses Battle");
				
				int numberOfGold = (Integer) currentUser.get("resourcesGold");
				int numberOfLumber = (Integer) currentUser.get("resourcesLumber");
				int numberOfStone = (Integer) currentUser.get("resourcesStone");
				
				//Halve all the resouces since the user lost
				numberOfGold = numberOfGold/2;
				numberOfLumber = numberOfLumber/2;
				numberOfStone = numberOfStone/2;
				
				currentUser.put("resourcesGold", numberOfGold);
				currentUser.put("resourcesLumber", numberOfLumber);
				currentUser.put("resourcesStone", numberOfStone);
				
				currentUser.increment("totalLosses");
				currentUser.saveInBackground();
				int currentHealth = currentUser.getInt("health");
				
				if(currentHealth==100){
					currentUser.put("health", 50);
					currentUser.saveInBackground();
				}
				else if(currentHealth==50){
					currentUser.put("health", 0);
					currentUser.saveInBackground();
				}
				else{
					
				}
				
				Toast.makeText(getApplicationContext(),
						"You lost the battle. Your heath is now " + currentUser.getInt("health"),
						Toast.LENGTH_LONG).show();
			}
			else{
				System.out.println("Battle is a tie!");
				Toast.makeText(getApplicationContext(),
						"The battle resulted in a tie.",
						Toast.LENGTH_LONG).show();
			}
			
			/*
			 ParseQuery<ParseObject> aiQuery = ParseQuery.getQuery("AI");
			 aiQuery.whereEqualTo("AIName", aiName);
			 aiQuery.getFirstInBackground(new GetCallback<ParseObject>() {
			   public void done(ParseObject object, ParseException e) {
			     if (object == null) {
			       Log.d("score", "The getFirst request failed.");
			     } else {
			       Log.d("score", "Retrieved the object.");
			       int attack = object.getInt("Attack");
			       int defense = object.getInt("Defense");
			       int totalPoints = attack + defense;
			     }
			   }
			   */
			   
			   
			// });
			
			
			break;
		case DialogInterface.BUTTON_NEUTRAL: // User clicked the evade button
			//Stop foraging
			Toast.makeText(getApplicationContext(),
					"You escaped the attack!",
					Toast.LENGTH_LONG).show();
			break;
		default:
			// nothing
			break;
		}
	}

	   		
	   		
		 
	   		
		 
	 

		
	}


