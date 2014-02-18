package com.example.crimson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BattleChallengeService extends Service {

	public ParseQuery<ParseObject> query;
	public Handler handler;
	public String username;
public int lvl;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void makeToast(String attacker) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Attacked by "+attacker+"!", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{ 
		 Toast.makeText(this, "Battle Challenge Service Started", Toast.LENGTH_LONG).show();
		 ParseUser currentUser = ParseUser.getCurrentUser();
		 username = currentUser.getString("username");
	
		 Timer timer = new Timer();
		 timer.schedule(new BackgroundQuery(),0, 1*5000);
		
		 return START_STICKY;
	}
	public class BackgroundQuery extends TimerTask
	{
	

	
	
	public void run() {
	
		
		
		
		
		// TODO Auto-generated method stub
		 ParseUser currentUser = ParseUser.getCurrentUser();
		 Log.i("Trying","one");
		 query = ParseQuery.getQuery("Attack");
		 query.whereEqualTo("victim", currentUser.getUsername());
		 query.findInBackground(new FindCallback<ParseObject>(){
			 @Override
			 public void done(List<ParseObject> objects, ParseException e)
			 {Log.i("Trying","two");
			 Log.i("Trying","size"+objects.size());
				 if ((e == null)&&(objects.size()>0))
				 {
					 
					 Log.i("Trying","ondvse");
				 ParseObject attack = objects.get(0);
				 makeToast(attack.getString("attacker"));
				 String attackerName = attack.getString("attacker");
				 
				// getLevel(attackerName,attack);
				Intent dialog = new Intent(getApplicationContext(), BattleDialogActivity.class); 
    		dialog.putExtra("attackerName",attackerName);	    	
				dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		
		startActivity(dialog);
		 attack.put("victim", "");
		// attack.put("attackerPoints", level);
			attack.saveInBackground();
				
				
				 }
				 else
				 {
					 
					 Log.i("fd","Something went wrong.");
				 }
			 }

//			private int getLevel(String attackerName,final ParseObject attack) {
//				// TODO Auto-generated method stub
//			
////				ParseQuery<ParseUser> query = ParseUser.getQuery();
////				query.whereEqualTo("username", attackerName);
////				query.findInBackground(new FindCallback<ParseUser>() {
////				  public void done(List<ParseUser> objects, ParseException e) {
////				    if (e == null) {
////				    	
////				        // The query was successful.
////				    	ParseUser attacker = objects.get(0);
////				    	Number level = attacker.getNumber("lastAltitude");
////				    	lvl = level.intValue();
////				    	Log.i("levl","test"+lvl);
////				    	putLevel(lvl,attack);
////				    	
////				    	
////				    } else {
////				        // Something went wrong.
////				    	
////				    }
////				  }
////
////				private void putLevel(int lvl, ParseObject attack) {
////					// TODO Auto-generated method stub
////					attack.put("attackerPoints", lvl);
////					attack.saveInBackground();
////					
////				}
////
////				
////				});
//				
//				return lvl;
//				
//			}
		 });
		 
		//ParseACL acl = new ParseACL(currentUser);
		//acl.setWriteAccess(currentUser, true);
//		query = ParseUser.getQuery();
//		query.whereEqualTo("isAttacking", currentUser.getUsername());
//		query.findInBackground(new FindCallback<ParseUser>() {
//			 
//
//			@Override
//			public void done(List<ParseUser> objects, ParseException e) {
//				// TODO Auto-generated method stub
//				  if ((e == null)&&(objects.size() != 0))
//				    {
//				        // The query was successful.
//				
//				    		ParseUser attacker = objects.get(0);
//				    		 ParseUser currentUser = ParseUser.getCurrentUser();
//				    		Number attackerFightId = attacker.getNumber("fightID");
//				    		Number victimFightId = currentUser.getNumber("fightID");
//				    		
//				    		Number temp = attackerFightId.intValue()+1;
//				    		Log.i("afi"," "+attackerFightId);
//				    		Log.i("vfi"," "+victimFightId);
//				    		Log.i("2"," "+attacker.getUsername());
//				    		Log.i("1"," "+currentUser.getString("currentAttacker"));
//				    		
//				    		
//				    		
//				    		
//				    		
//				    		if(!(currentUser.getString("currentAttacker").equalsIgnoreCase(attacker.getUsername()) )||(victimFightId == attackerFightId))
//				    		{
//				    			makeToast(attacker.getUsername());
//				    			Log.i("in","yes");
//				    			currentUser.put("currentAttacker", attacker.getUsername());
//				    			ParseObject attack = new ParseObject("Attack");
//				    			
//				    			currentUser.put("fightID", attackerFightId.intValue()+1);
//				    		
//					    		currentUser.saveInBackground();
//				    		}
//				    		
//				    		
//				    		
//				    	
//						
//				    		//String attackerName = attacker.getUsername();
//				    	//	ParseUser attacker1 = battle.getParseUser("Attacker");
//				    	//	Log.i("dontest",attacker1.getUsername());
//				    	//	makeToast(attacker1.getUsername());
//				    		//Log.i("userTest",attacker.getUsername());
//				    		//battle.put("Victim", "nulll");
//				    		//battle.saveInBackground();
//				    		//attacker.saveInBackground();
////				    		 attacker.saveInBackground(new SaveCallback() {
////				    			   public void done(ParseException e) {
////				    			     if (e == null) {
////				    			    	 
//////				 			    		//Log.i("ambustest",attackerName);
////				 			    		
////				 			    		Intent dialog = new Intent(getApplicationContext(), BattleDialogActivity.class); 
////				 			    		//dialog.putExtra("attackerName",attackerName);
////				 			    		dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
////			 			    		
////			 			    		startActivity(dialog);
////				    			     } else {
////				    			    	  Log.i("bf","Something went wrong.");
////				    			    	  e.printStackTrace();
////				    			    	  
////			    			     }
////				    			   }
////			    			 });
//				    		
//				    		
//					
//				    } 
//					  else
//					  {
//				        Log.i("fd","Something went wrong.");
//				    }	
//			}
//
//		
//			});
	}
}
}
