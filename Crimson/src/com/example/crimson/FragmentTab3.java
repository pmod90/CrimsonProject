package com.example.crimson;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
 
public class FragmentTab3 extends SherlockFragment {
	Button editprofile;
	Button logout;
	ImageView imageView;
 
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab3.xml
        View view = inflater.inflate(R.layout.fragmenttab3, container, false);
       
    	imageView = (ImageView) view.findViewById(R.id.imageView1);
    	ParseUser currentUser = ParseUser.getCurrentUser();
    	ParseFile profile = (ParseFile)currentUser.get("photo");
    	profile.getDataInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] data, ParseException e) {
				// TODO Auto-generated method stub
				Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
				
				imageView.setImageBitmap(bmp);
				
			}});

		
		// Convert currentUser into String
		String addr = currentUser.get("address").toString();
		String uName = currentUser.getUsername().toString();

		String health = currentUser.get("health").toString();
		String clan = currentUser.get("clan").toString();
		
		String wins = currentUser.get("totalWins").toString();
		String losses = currentUser.get("totalLosses").toString();
		
		TextView displayClan = (TextView) view.findViewById(R.id.displayClan);
		TextView addressInfo = (TextView) view.findViewById(R.id.addressInfo);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView healthTextView =(TextView)view.findViewById(R.id.healthTextView);
		TextView winTextView = (TextView) view.findViewById(R.id.winRecord);
		TextView lossTextView = (TextView) view.findViewById(R.id.lossRecord);
		
		// Set the currentUser String into TextView
		addressInfo.setText("Address: " + addr);
		name.setText(uName);
		healthTextView.setText("Health: " + health);
		displayClan.setText("Clan: "+clan);
		winTextView.setText("Wins: " + wins);
		lossTextView.setText("Losses: " + losses);
		
        return view;
    }
        
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
 
}