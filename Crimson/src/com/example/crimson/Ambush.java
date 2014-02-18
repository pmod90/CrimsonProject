package com.example.crimson;



import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Ambush extends Activity {

ListView checkinsList;
ParseObject attackedForager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.ambush);
	
	Context context = getApplicationContext();
	ForageAdapter adapter = new ForageAdapter(context);
	checkinsList = (ListView) findViewById(R.id.checkinsList);
	checkinsList.setAdapter(adapter);
	
	final AlertDialog.Builder ambushDialog = new AlertDialog.Builder(this);
	ambushDialog.setMessage(R.string.ambushattack)
	.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int id) {
			 String attackedForagerName = attackedForager.getString("username");
			 Log.i("forgare",attackedForagerName);
		
			 ParseUser currentUser = ParseUser.getCurrentUser();
			 currentUser.put("isAttacking", attackedForagerName);
			 ParseObject attack = new ParseObject("Attack");
			 String uname = currentUser.getUsername();
			 attack.put("attacker", uname);
			 attack.put("victim", attackedForagerName);
			 attack.saveInBackground();
			 startService(new Intent(getBaseContext(),BattleRecieverService.class));
			 if(currentUser.getNumber("fightID") == null)
			 {currentUser.put("fightID", 1);}
			 else
			 {
				 currentUser.put("fightID", currentUser.getNumber("fightID").intValue() + 1);
			 }
			 //currentUser.put("fightID", value);
			 currentUser.saveInBackground();
		
		}
	});
	
	checkinsList.setClickable(true);
	checkinsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		  @Override
		  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		     attackedForager = (ParseObject)checkinsList.getItemAtPosition(position);
		  
		     AlertDialog dialog = ambushDialog.create();
		     ParseUser currUser = ParseUser.getCurrentUser();
	    		
	    		int healthVal = currUser.getNumber("health").intValue();
	    		if(healthVal == 0)
	    		{
	    			  Toast.makeText(getApplicationContext(),
	    	                  "You are at 0 health and hence can't ambush",
	    	                  Toast.LENGTH_LONG).show();
	    		}
	    		else
	    		{     dialog.show();}
		

		  }
		});
	

	}


}
