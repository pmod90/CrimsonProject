package com.example.crimson;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseUser;
import com.parse.signpost.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.Serializable;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
public class FragmentTab1 extends SherlockFragment {
	ListView places;
	Place placeDetail;


	PlaceAdapterClass placesAdapter;
	String[] placesArray = {"No Places"};
	ArrayList<Place> placesArrayList ;
	ArrayList<String> parseHealthSpots;
	Context context;
	//Removed: String feedURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=41.8712612,-87.6506242&radius=700&types=bar&sensor=false&key=AIzaSyBwO3eVAp7L0VEy2GM-wvcQYtxNpvwD2OM";
	String feedURL;
	Place p;
	//Edit:Vishal
	TextView textLat; // Donot Confuse with IDs
	TextView textLong;
	//TextView placeListItem;
	PlacesListTask loadPlaces;
	ParseObject userCheckIn;
	boolean locationChangedFlag = false;
	ParseUser currentUser;
	double pLong;
	double pLat;
	
	//Added by Ed
	String health;
	
	
	
	//End Vishal
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab1, container, false);
		
		
		 currentUser = ParseUser.getCurrentUser();
		placesArrayList = new ArrayList<Place>();
		parseHealthSpots = new ArrayList<String>();
		parseHealthSpots.add("Saint Ignatius College Prep");
		parseHealthSpots.add("TCF Bank");
		parseHealthSpots.add("University Village / Little Italy");
		parseHealthSpots.add("Paul Mitchell the School Chicago");
		parseHealthSpots.add("Jane Addams Hull-House Museum");
		
		
		context = getActivity(); //Moved from down. Needed it to be instantiated.
		
		
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,ll);
	
		final AlertDialog.Builder checkInDialog = new AlertDialog.Builder(getActivity());
		checkInDialog.setMessage(R.string.checkindialog);
		checkInDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	   String username = currentUser.get("username").toString();
	        
	        	   
		        	  ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCheckIn");
		        	  query.whereEqualTo("isCheckedIn", true);
		        	  query.whereEqualTo("username", username);
		        	  query.findInBackground(new FindCallback<ParseObject>() {
		        	      public void done(List<ParseObject> earlierCheckins, ParseException e) {
		        	        Log.i("arraysize","SETSIZE"+earlierCheckins.size());
		        	    	  if (e == null) {
		        	        	  for (int i = 0;i<earlierCheckins.size();i++)
		        		        	{
		        		        		ParseObject earlierCheckinObj = earlierCheckins.get(i);
		        		        		earlierCheckinObj.put("isCheckedIn", false);
		        		        		//Log.i("checkinTest",checkinPlace);
		        		        		earlierCheckinObj.saveInBackground();
		        		        	}
		        	            
		        	          } else {
		        	              Log.d("score", "Error: " + e.getMessage());
		        	          }
		        	      }
		        	  });
	        	   Intent intent = new Intent(getActivity(),PostCheckIn.class);
	        	   intent.putExtra("currentPlaceName",placeDetail.name);
	        	   intent.putExtra("currentPlaceAddress",placeDetail.address);
	        	  userCheckIn = new ParseObject("UserCheckIn");
	        	  userCheckIn.put("place", placeDetail.name);
	        	  userCheckIn.put("isCheckedIn", true);
	        	  userCheckIn.put("username", currentUser.get("username"));
	        	
	        	  userCheckIn.saveInBackground();
	        	  ParseUser currentUser = ParseUser.getCurrentUser();
	        	  currentUser.put("lastCheckinPlace", placeDetail.name);
	        	  currentUser.saveInBackground();
	        	  

	        	  
	        	    startActivity(intent);
	           }
	       });
	checkInDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
       
		
		places = (ListView) view.findViewById(R.id.placesList);
		// placeListItem = (TextView)view2.findViewById(R.id.placeName);
		
		places.setClickable(true);
		places.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			  @Override
			  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//Person p = (Person) myListView.getItemAtPosition(position);
			     placeDetail = (Place)places.getItemAtPosition(position);
AlertDialog dialog = checkInDialog.create();
dialog.show();
			  }
			});
		
		placesAdapter = new PlaceAdapterClass(getActivity(),R.layout.places_list_item,placesArrayList);
		places.setAdapter(placesAdapter);
		
		
	
		//Edit:Vishal

	    //End Vishal
				
		return view;
	}
	
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
		
	public class PlacesListTask extends AsyncTask<Void,Void,Void> 
	{

	ProgressDialog dialog;
		@Override
		protected void onPostExecute(Void result)
		{
			dialog.dismiss();
			placesAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
			
		}
		
		@Override
		protected void onPreExecute()
		{
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Loading nearby places");
			dialog.show();
			
			
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(feedURL);
			try {
				org.apache.http.HttpResponse response =  client.execute(getRequest);
			  StatusLine statusline = response.getStatusLine();
				int statusCode = statusline.getStatusCode();
				if(statusCode!=200)
				{
					return null;
				}
				InputStream jsonStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
				StringBuilder builder = new StringBuilder();
				String line;
				while((line = reader.readLine())!= null)
				{
					builder.append(line);
				}
				
				//Ed's query for finding all the spots that are healing spots
				ParseQuery<ParseObject> checkinPlacesQuery = ParseQuery.getQuery("checkinPlaces");
				 checkinPlacesQuery.whereEqualTo("isHealingSpot", true);
				 checkinPlacesQuery.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> healthSpotList, ParseException e) {
				        if (e == null) {
				            Log.d("score", "Retrieved " + healthSpotList.size() + " scores");
				            
				            for(int i=0; i<healthSpotList.size();i++){
				            	//System.out.println("This is a health spot: " + healthSpotList.get(i).get("placeName"));
				            	String spotName = (String) healthSpotList.get(i).get("placeName");
				            	System.out.println("This is a health spot: " + spotName);
				            	//parseHealthSpots.add(spotName);
				            }
				            
				        } else {
				            Log.d("score", "Error: " + e.getMessage());
				        }
				    }
				});
				
				
				String jsonData = builder.toString();
				JSONObject json = new JSONObject(jsonData);
				//Place p; //Ed commented this it might be useful DONT DELETE
				JSONArray results = json.getJSONArray("results");
				placesArrayList.clear();
				for(int i =0; i<results.length(); i++)
				{
					JSONObject place = results.getJSONObject(i);
					String placeName = place.getString("name");
					String addressName = place.getString("vicinity");
					p = new Place();
					
					
					
					
					System.out.println(placeName);
					
					//Add query 
					//Jane Addams Hull-House Museum
					/*
					 ParseQuery<ParseObject> checkinPlacesQuery2 = ParseQuery.getQuery("checkinPlaces");
					 checkinPlacesQuery2.whereEqualTo("placeName", placeName);
					 checkinPlacesQuery2.whereEqualTo("isHealingSpot", true);
					 //checkinPlacesQuery.getFirstInBackground(new GetCallback<ParseObject>() {
					 checkinPlacesQuery.findInBackground(new FindCallback<ParseObject>() { 
						 public void done(List<ParseObject> objects, ParseException e) {
					     if (e == null) {
					    	 //ParseObject object = objects.get(0);
					      
					       
					       String healths = "Healing Spot";
					       
					       p.setHealth(healths);
					      
					     } 
					     else {
					       Log.d("score", "Retrieved the object.");
					       
					       //String placeHealth = object.getString("placeName");
					       System.out.println("!!!!!!!!!!!!!!!");
					       //System.out.println(placeHealth + " HEALING SPOT");
					       
					      
					      
					       
					     }
					   }
					   
					   
					   
					 });
					 
					 */
					//if(succed){ p.setHealth(H)
					//p.setHealth(health);
					
					
					for(int j=0; j<parseHealthSpots.size(); j++){
						if(parseHealthSpots.get(j).equals(placeName)){
							String healths = "Healing Spot";
							 p.setHealth(healths);
						}
					}
					
					
					p.setName(placeName);
					p.setAddress(addressName);
					 
					placesArrayList.add(p);
				}
				
				for(int i=0; i<placesArrayList.size(); i++){
					System.out.println(placesArrayList.get(i).name + " " + placesArrayList.get(i).health);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	

	//Edit:Vishal
	private class mylocationlistener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location location) {
			
			if(location != null)
			{
				pLong = location.getLongitude();
				pLat = location.getLatitude();
				Log.i("TEST","location="+pLong);
				ParseGeoPoint point = new ParseGeoPoint(pLat,pLong);
				//Intent intent1 = new Intent(getActivity(),PostCheckIn.class);
	        	//intent1.putExtra("lat", pLat);
	        	//intent1.putExtra("long", pLong);
//	        	userCheckIn.put("co-ordinates", point);		//dont uncomment
				
				if(locationChangedFlag)
				{
//					ParseUser currentUser = ParseUser.getCurrentUser();
//					ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCheckIn");
//					query.whereEqualTo("username", currentUser.getUsername());
//					query.findInBackground(new FindCallback<ParseObject>() {
//					    public void done(List<ParseObject> objects, ParseException e) {
//					        if (e == null) {
//					            ParseObject object = objects.get(objects.size() - 1);
//					           // Log.i("objectID","id"+object.getString("objectId"));
//					        } else {
//					            Log.d("score", "Error: " + e.getMessage());
//					        }
//					    }
//					});
//					userCheckIn.put("isCheckedIn", false);
//				userCheckIn.saveInBackground();
				}
				locationChangedFlag = true;
				
				
				
				//textLat.setText(Double.toString(pLat)); //Displays location. Created to check wheather GPS is working.
				//textLong.setText(Double.toString(pLong)); //Can be deleted in the end.
				feedURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+Double.toString(pLat)+","+Double.toString(pLong)+"&radius=700&sensor=false&key=AIzaSyBwO3eVAp7L0VEy2GM-wvcQYtxNpvwD2OM";
				
				loadPlaces = new PlacesListTask();
				loadPlaces.execute();
			}	

		}
	
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}
	//End Vishal
	
} //End of Class


