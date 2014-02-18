package com.example.crimson;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.parse.ParseObject;
import com.parse.ParseUser;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Home extends SherlockFragmentActivity {
    // Declare Variables
	//Git Test
    ActionBar mActionBar;
    ViewPager mPager;
    Tab tab;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		// Retrieve current user from Parse.com
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		// Convert currentUser into String
		String struser = currentUser.getUsername().toString();
		   ParseObject test = new ParseObject("Test");
		   test.put("testcol", "value");
		   test.saveInBackground();
		// Activate Navigation Mode Tabs
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Locate ViewPager in activity_main.xml
		mPager = (ViewPager) findViewById(R.id.pager);
		
		// Activate Fragment Manager
		FragmentManager fm = getSupportFragmentManager();

		// Capture ViewPager page swipes
		ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				// Find the ViewPager Position
				mActionBar.setSelectedNavigationItem(position);
			}
		};

		mPager.setOnPageChangeListener(ViewPagerListener);
		// Locate the adapter class called ViewPagerAdapter.java
		ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm);
		// Set the View Pager Adapter into ViewPager
		mPager.setAdapter(viewpageradapter);
		
		// Capture tab button clicks
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// Pass the position on tab click to ViewPager
				mPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}
		};

		// Create first Tab
		tab = mActionBar.newTab().setText("NEARBY").setTabListener(tabListener);
		mActionBar.addTab(tab);
		
		// Create second Tab
		tab = mActionBar.newTab().setText("RESOURCES").setTabListener(tabListener);
		mActionBar.addTab(tab);
		
		// Create third Tab
		tab = mActionBar.newTab().setText("CHARACTER").setTabListener(tabListener);
		mActionBar.addTab(tab);
		
		
	
	}
	
public void editprofileClicked(View arg0) {
    	

    	Intent intent = new Intent(this, EditProfile.class);
    	startActivity(intent);
    	
    }

public void logoutClicked(View arg0) {
	
	System.out.println("Logout Clicked");
	ParseUser currentUser = ParseUser.getCurrentUser();
	currentUser.logOut();
	
	 /*
	 Intent intent = new Intent(this, SignUpActivity.class);
	 startActivity(intent);
	 this.finish();
	*/
	Intent intent2 = new Intent(this, MainActivity.class);
	startActivity(intent2);
	this.finish();
	
	Toast.makeText(getApplicationContext(),
            "Successfully Logged out",
            Toast.LENGTH_LONG).show();
}
}
