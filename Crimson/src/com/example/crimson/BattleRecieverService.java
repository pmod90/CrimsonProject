package com.example.crimson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.crimson.BattleChallengeService.BackgroundQuery;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BattleRecieverService extends Service {
	 Timer timer = new Timer();
	
	private void toastResult(String result) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "You "+result, Toast.LENGTH_LONG).show();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{ 
		 Toast.makeText(this, "Battle Reciever Service Started", Toast.LENGTH_LONG).show();
		 
		 
		 timer.schedule(new ResultListener(),0, 1*5000);
		
		 return START_STICKY;
	}

	public class ResultListener extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Attack");
			//	query.whereEqualTo("playerName", "Dan Stemkoski");
				query2.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> objects, ParseException e) {
				        if (e == null) {
				        	ParseObject objectBattle = objects.get(0);
				        	String result = objectBattle.getString("result");
				        	
				        	if(result == null)
				        	{
				        		Log.i("Still","Waiting");
				        	}
				        	else
				        	{
				        		Log.i("Battle","1");
				        		
					        	
				        		if(result.equals("lost"))
				        		{
						    		//toastResult("lost and injured!");

				        			ParseUser currUser = ParseUser.getCurrentUser();
				        			currUser.put("totalLosses", currUser.getNumber("totalLosses").intValue() + 1);
				        			currUser.put("resourcesLumber", currUser.getNumber("resourcesLumber").intValue()/2);
						    		currUser.put("resourcesGold", currUser.getNumber("resourcesGold").intValue()/2);
						    		currUser.put("resourcesStone", currUser.getNumber("resourcesStone").intValue()/2);
						    		int healthVal = currUser.getNumber("health").intValue();
						    		Log.i("Battle","2");
						        	
						    		if(healthVal == 100)
						    		{currUser.put("health", 50);
						    		currUser.saveInBackground();
						    		toastResult("lost and injured yourself.Go to a healing spot!");

						    		}
						    		else if(healthVal == 50)
						    		{currUser.put("health", 0);
						    		currUser.saveInBackground();
						    		toastResult("lost and died.Go to a healing spot!");
						    		}
						    		
						    		
				        			
				        		}
				        		else if(result.equals("won"))
				        		{
				        			ParseUser currUser = ParseUser.getCurrentUser();

				        			toastResult("won!");
				        			currUser.put("totalWins", currUser.getNumber("totalWins").intValue() + 1);
currUser.saveInBackground();
				        			
				        		}
				        		else if(result.equals("tie"))
				        		{Log.i("BattleResult",result);
					        	
				        			toastResult("tie");
				        		}
				        	
				        		objectBattle.deleteInBackground();
				        		stopSelf();
				        		timer.cancel();
				        	}
				        } else {
				            Log.d("score", "Error: " + e.getMessage());
				        }
				    }

				
				});
			
		}
		
	}
}

