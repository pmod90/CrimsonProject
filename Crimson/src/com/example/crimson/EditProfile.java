package com.example.crimson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
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


import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class EditProfile extends Activity implements DialogInterface.OnClickListener {
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int REQ_CODE_PICK_IMAGE = 1;
	Button submitchanges;
	Button deleteAccount;
	
	String passwordtxt;
	String addresstxt;
	String emailtxt;
	String clantxt;
	
	EditText clan;
	EditText emailaddress;
	EditText postaladdress;
	String accountID;
	
	
	ParseFile profilePic;
	
	boolean picExists;
	
	byte[] image;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		String currentEmail = currentUser.getEmail();
		String currentAddress = currentUser.get("address").toString();
		String currentClan = currentUser.get("clan").toString();
		
		emailaddress = (EditText) findViewById(R.id.emailaddressIn);
		postaladdress = (EditText) findViewById(R.id.postaladdressIn);
		clan = (EditText) findViewById(R.id.editClan);
		
		emailaddress.setText(currentEmail);
		postaladdress.setText(currentAddress);
		clan.setText(currentClan);
		
		
		//When the activity is created no picture is selected so it does not exists
		picExists = false;
		
	}
	
	
	public void changeprofilepic(View v)
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
                 picExists = true; // We select a picture so it exists
   
               
                
	        }
	    }
       
     
    }
	
	public void submitchanges(View v)
	{
		
		
		addresstxt = postaladdress.getText().toString();
		emailtxt = emailaddress.getText().toString();
		clantxt = clan.getText().toString();
		
		//Note: Need to update to not require all fields to be changed

		
		
			// Save new changes to users data into Parse.com Data Storage
			ParseUser currentUser = ParseUser.getCurrentUser();
			currentUser.setEmail(emailtxt);
			currentUser.put("address", addresstxt);
			currentUser.put("clan", clantxt);
			
			if(picExists==true){
				currentUser.put("photo", profilePic);
			}
			
			currentUser.saveInBackground();
			
			Toast.makeText(getApplicationContext(),
					"Changes have been saved!",
					Toast.LENGTH_LONG).show();
			
			
			
			//Go back to home
			Intent intent2 = new Intent(this, Home.class);
            startActivity(intent2);
            finish();			
		}
	
	public void deleteAccount(View v){
	
			
   		AlertDialog deleteAccount = new AlertDialog.Builder(this)
   		.setMessage("Are you sure you want to delete your account? This cannot be undone!")
   		.setTitle("Delete Account?")
   		.setPositiveButton("Yes", this)
   		.setNeutralButton("Cancel", this)
   		.setCancelable(false)
   		.create();
   		deleteAccount.show();
		
		

	    }
	

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which){
			case DialogInterface.BUTTON_POSITIVE: // yes the user is sure and wants to delete their account
				
				//Delete the User's account
				ParseUser user = ParseUser.getCurrentUser();
				user.deleteInBackground();
				
				//Display farewell message
				Toast.makeText(getApplicationContext(),
						"Your account has been deleted! \n Goodbye",
						Toast.LENGTH_LONG).show();
				
				//Go to main activity to let user log in again for next time
				Intent intent2 = new Intent(this, MainActivity.class);
				startActivity(intent2);
				this.finish();
				
				
				//Exit the android application now that the account has been deleted
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				
				
				

				break;
			case DialogInterface.BUTTON_NEUTRAL: // User clicked cancel
				break;
			default:
				// nothing
				break;
		}
		
	}
		     

   
}
