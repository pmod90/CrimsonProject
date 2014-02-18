package com.example.crimson;

import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;



public class ForageAdapter extends ParseQueryAdapter<ParseObject> {

	public ForageAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery<ParseObject> create() {
				
				 ParseUser currentUser = ParseUser.getCurrentUser();
				 String place = currentUser.getString("lastCheckinPlace");
				ParseQuery query = new ParseQuery("ForagingStatus");	
				query.whereEqualTo("isForaging", true);
				query.whereEqualTo("foragingPlace", place);
				query.whereNotEqualTo("username", currentUser.get("username").toString());
				
				return query;
			}
		});
	}
	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.ambush_item_list, null);
		}

		super.getItemView(object, v, parent);

	
		TextView checkinPlace = (TextView) v.findViewById(R.id.checkinPlace);
		checkinPlace.setText(object.getString("foragingPlace"));
		TextView checkinUser = (TextView) v.findViewById(R.id.checkinUser);
		checkinUser.setText(object.getString("username"));
		return v;
	}

}



