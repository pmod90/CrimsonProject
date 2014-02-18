package com.example.crimson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int REQ_CODE_PICK_IMAGE = 1;
	// Declare Variables
	Button loginbutton;
	Button signup;
	String usernametxt;
	String passwordtxt;
	String addresstxt;
	String emailtxt;
	String clantxt;
	List<Integer> av;
	EditText password;
	EditText email;
	EditText username;
	EditText address;
	EditText clan;
	ParseFile profilePic;
	byte[] image;
	@Override

	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.signup);
	username = (EditText) findViewById(R.id.username);
	password = (EditText) findViewById(R.id.password2);
	email = (EditText) findViewById(R.id.email);
	address = (EditText) findViewById(R.id.address);
	clan = (EditText) findViewById(R.id.clan);
	ParseObject test2 = new ParseObject("testObject");
	
	
	test2.saveInBackground();
	}//signup is the xml file

	public void choosePic(View v)
	{
	    Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
       
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) { 
	    case REQ_CODE_PICK_IMAGE:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(
	                               selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();


	            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	       
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
	            byte[] image = stream.toByteArray();
	            ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                // Upload the image into Parse Cloud
       
                 profilePic = new ParseFile("profile.png", image);
                 profilePic.saveInBackground();
   
               
                
	        }
	    }
       
     
    }
	public void signupClicked(View arg0)
	{
		// Retrieve the text entered from the EditText
		usernametxt = username.getText().toString();
		passwordtxt = password.getText().toString();
		addresstxt = address.getText().toString();
		emailtxt = email.getText().toString();
		clantxt = clan.getText().toString();

		// Force user to fill up the form
		if (usernametxt.equals("") && passwordtxt.equals("")) {
			Toast.makeText(getApplicationContext(),
					"Please complete the sign up form",
					Toast.LENGTH_LONG).show();

		} else {
			// Save new user data into Parse.com Data Storage
			ParseUser user = new ParseUser();
			user.setUsername(usernametxt);
			user.setPassword(passwordtxt);
			user.setEmail(emailtxt);
			user.put("address", addresstxt);
			user.put("photo", profilePic);
			user.put("clan", clantxt);
			av = new ArrayList<Integer>();
			av.add(1);
			user.put("artifactsOwned", av);
			//Win/loss
			user.put("totalWins", 0);
			user.put("totalLosses", 0);
			
			//Resource Database
			user.put("battlePoints", 0);
			user.put("resourcesGold", 1000);
			user.put("resourcesStone", 1000);
			user.put("resourcesLumber", 1000);
			
			ParseObject usr = new ParseObject("attackResources");
			usr.put("resources", 1000);
			usr.put("health", 100);
			usr.put("uname", usernametxt);
			usr.saveInBackground();
			
			//Health and Artifacts
			user.put("health", 100);
			user.put("artifacts", 0);
			

			ParseObject object = new ParseObject("ForagingStatus");
			object.put("username",usernametxt);
			object.put("isForaging", false);
			object.saveInBackground();
			

			user.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(com.parse.ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						// Show a simple Toast message upon successful registration
						Toast.makeText(getApplicationContext(),
								"Successfully Signed up, please log in.",
								Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(getApplicationContext(),
								"Sign up Error", Toast.LENGTH_LONG)
								.show();
					
					}
				}
		
			});
			
		}
		   Intent intent = new Intent(this,MainActivity.class);
		    startActivity(intent);	   
	}

}
