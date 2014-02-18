package com.example.crimson;

import java.util.Currency;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BattleDialogActivity extends Activity {
	int attackerPoints;
	int victimPoints;
	String result = "0";
	String subresult;
	private void makeToast(String result) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "You "+result, Toast.LENGTH_LONG).show();
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		final String attackerName = i.getExtras().getString("attackerName");
//		
//		ParseUser currentUser = ParseUser.getCurrentUser();
//		ParseQuery<ParseUser> query = ParseUser.getQuery();
//		query.whereEqualTo("isAttacking", currentUser.getUsername());
//		query.findInBackground(new FindCallback<ParseUser>() {
//			  public void done(List<ParseUser> objects, ParseException e) {
//	
//				  if (e == null)
//			    {
//			        // The query was successful.
//			
//			    		
//				
//			    } 
//				  else
//				  {
//			        Log.i("fd","Something went wrong.");
//			    }	  
//			  }
//			});

		final AlertDialog.Builder battleDialog = new AlertDialog.Builder(this);
		battleDialog.setMessage(attackerName.concat(" has attacked you.Fight back?"))
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				
				//Victim stats
				ParseUser victim = ParseUser.getCurrentUser();
				
			//	Log.i("vicitmaddr",victim.getString("address"));
				try {
					victim.refresh();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Number bPoints = victim.getNumber("battlePoints");
				Log.i("vicitm","point"+bPoints);
				Log.i("Battle","Points"+bPoints);
				victimPoints = bPoints.intValue();
				Log.i("vicitm1","point"+victimPoints);
				ParseQuery<ParseUser> query = ParseUser.getQuery();
				query.whereEqualTo("username", attackerName);
				query.findInBackground(new FindCallback<ParseUser>() {
				  public void done(List<ParseUser> objects, ParseException e) {
				    if (e == null) {
				        // The query was successful.
				    	ParseUser attacker = objects.get(0);
				    	Number attackerPts = attacker.getNumber("battlePoints");
				    	attackerPoints = attackerPts.intValue();
				    	Log.i("attacker","point"+attackerPoints);
				    	
				    	if(attackerPoints>victimPoints)
				    	{
				    		result = "won";
				    		
				    		ParseUser currUser = ParseUser.getCurrentUser();
				    		currUser.put("resourcesLumber", currUser.getNumber("resourcesLumber").intValue()/2);
				    		currUser.put("resourcesGold", currUser.getNumber("resourcesGold").intValue()/2);
				    		currUser.put("resourcesStone", currUser.getNumber("resourcesStone").intValue()/2);
		        			currUser.put("totalLosses", currUser.getNumber("totalLosses").intValue() + 1);

				    		currUser.saveInBackground();
				    		int healthVal = currUser.getNumber("health").intValue();
				    		if(healthVal == 100)
				    		{currUser.put("health", 50);
				    		currUser.saveInBackground();
				    		makeToast("lost and injured yourself.Your health is now 50!");
				    		}
				    		else if(healthVal == 50)
				    		{currUser.put("health", 0);
				    		currUser.saveInBackground();
				    		makeToast("lost and died!Go to a healing spot!");
				    		}
				    		
				    		
				    		
				    		
				    	}
				    	else if(victimPoints > attackerPoints)
				    	{
				    		result = "lost";
				    		ParseUser currUser = ParseUser.getCurrentUser();

		        			currUser.put("totalWins", currUser.getNumber("totalWins").intValue() + 1);
currUser.saveInBackground();
				    		makeToast("won!");
				    	}
				    	else if(victimPoints == attackerPoints)
				    	{
				    		result = "tie";
				    		makeToast("tied");
				    	}
				    } else {
				        // Something went wrong.
				    }
				  }

				
				});
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Attack");
			//	query.whereEqualTo("playerName", "Dan Stemkoski");
				query2.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> objects, ParseException e) {
				        if (e == null) {
				        	ParseObject objectBattle = objects.get(0);
				        	//Log.i("TestBatt","Test"+objectBattle.getNumber("victimPoints"));
				        	
				        	objectBattle.put("result", result);
				        	objectBattle.saveInBackground();
				          //  Log.d("score", "Retrieved " + scoreList.size() + " scores");
				        } else {
				            Log.d("score", "Error: " + e.getMessage());
				        }
				    }
				});
				
			
				
//			//Victim stats
//				ParseUser victim = ParseUser.getCurrentUser();
//			//Attacker stats
//				ParseQuery<ParseUser> query = ParseUser.getQuery();
//				query.whereEqualTo("username", attackerName);
//				query.findInBackground(new FindCallback<ParseUser>() {
//				  public void done(List<ParseUser> objects, ParseException e) {
//				    if (e == null) {
//				      ParseUser attacker = objects.get(0);
//				      Number alt = (Number)attacker.get("lastAltitude");
//				      int al = alt.intValue();
//				      String objid = attacker.getString("objectId");
//				      Log.i("Query","Successful"+al);
//				    setAttackerStats(al,objid);
//				      
//				    } else {
//				        // Something went wrong.
//				    }
//				  }
//
//				private void setAttackerStats(final int al,final String objId) {
//					// TODO Auto-generated method stub
//					Log.i("Object",objId);
//					ParseQuery<ParseObject> query = ParseQuery.getQuery("Attack");
//					query.whereEqualTo("objectId", objId);
//					query.getFirstInBackground(new GetCallback<ParseObject>() {
//					    public void done(ParseObject objects, ParseException e) {
//					        if (e == null) {
//					        	//ParseObject object = objects.get(objects.size()-1);
//					        	objects.put("attackerPoints", al);
//					        	objects.saveInBackground();
//					        	
//					        	
//					            //Log.d("score", "Retrieved " + scoreList.size() + " scores");
//					        } else {
//					            Log.d("score", "Error: " + e.getMessage());
//					        }
//					    }
//					});
////					ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
////					query.whereEqualTo("playerEmail", "dstemkoski@example.com");
////					query.getFirstInBackground(new GetCallback<ParseObject>() {
////					  public void done(ParseObject object, ParseException e) {
////					    if (object == null) {
////					      Log.d("score", "The getFirst request failed.");
////					    } else {
////					      Log.d("score", "Retrieved the object.");
////					    }
////					  }
////					});
//				}
//				});
//				
////			     Log.i("au",stats.atest);
////					Log.i("vu",stats.vtest);
				
				
			
			}
		});
		battleDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	        		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Attack");
	    			//	query.whereEqualTo("playerName", "Dan Stemkoski");
	    				query2.findInBackground(new FindCallback<ParseObject>() {
	    				    public void done(List<ParseObject> objects, ParseException e) {
	    				        if ((e == null)&&(objects.size()>0)) {
	    				        	
	    				        	ParseObject objectBattle = objects.get(0);
	    				        	objectBattle.deleteInBackground();
	    				        	//String result = objectBattle.getString("result");
	    				        
	    				        } else {
	    				            Log.d("score", "Error: " + e.getMessage());
	    				        }
	    				    }

	    				
	    				});
	           }
	       });
		AlertDialog dialog = battleDialog.create();
	     dialog.show();

	     
	}
	  
}
