package com.example.crimson;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends Activity {
	EditText password;
	EditText username;
	String usernametxt;
	String passwordtxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username = (EditText) findViewById(R.id.username2);
		password = (EditText) findViewById(R.id.password);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	public void createaccountClicked(View v)
    {
    Intent intent = new Intent(this,SignUpActivity.class);
    startActivity(intent);
    }
	
	public void loginClicked(View v)
	{
		// Retrieve the text entered from the EditText
		usernametxt = username.getText().toString();
		passwordtxt = password.getText().toString();

		// Send data to Parse.com for verification
		ParseUser.logInInBackground(usernametxt,
                passwordtxt,
                new LogInCallback(){

					@Override
					public void done(ParseUser user, ParseException e) {
						// TODO Auto-generated method stub
						
						if (user != null) {
							Intent intent2 = new Intent(MainActivity.this,Home.class);
			                 startActivity(intent2);
			                        Toast.makeText(getApplicationContext(),
			                                "Successfully Logged in",
			                                Toast.LENGTH_LONG).show();
			                        finish();	// If user exist and authenticated, send user to home.class
						   
						 	
				
							//finish();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"No such user exist, please signup",
									Toast.LENGTH_LONG).show();
						}
					}});
	
	}
}
