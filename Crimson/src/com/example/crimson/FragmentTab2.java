package com.example.crimson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
 
public class FragmentTab2 extends SherlockFragment {
 
	List<Integer> as;
	final ParseUser currentUser = ParseUser.getCurrentUser();
	View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        view = inflater.inflate(R.layout.fragmenttab2, container, false);

        String health2 = currentUser.get("health").toString();
        
        String gold = currentUser.get("resourcesGold").toString();
        String stone = currentUser.get("resourcesStone").toString();
        String lumber = currentUser.get("resourcesLumber").toString();
        
        
        loadUserArtifacts2();
        TextView health = (TextView) view.findViewById(R.id.health);
        
        TextView resourcesGold = (TextView) view.findViewById(R.id.resourcesGold);
        TextView resourcesStone = (TextView) view.findViewById(R.id.resourcesStone);
        TextView resourcesLumber = (TextView) view.findViewById(R.id.resourcesLumber);
        
        health.setText("Health: " + health2);
 
        resourcesGold.setText("Gold: " + gold);
        resourcesStone.setText("Stone: " + stone);
        resourcesLumber.setText("Lumber: " + lumber);
        
        
     
        return view;
    
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    public void loadUserArtifacts2(){
		//final ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQueryAdapter<ParseObject> adapter =
				new ParseQueryAdapter<ParseObject>(getActivity(), new ParseQueryAdapter.QueryFactory<ParseObject>() {
				    public ParseQuery<ParseObject> create() {
				    	//List<Integer> al;
						as = new ArrayList<Integer>();
				  	  	as = currentUser.getList("artifactsOwned");
				  	  Log.d("ListVish unsorted", as.toString());
				  	  Collections.sort(as);
				  	  Log.d("ListVish sorted", as.toString());	
				  	  	ParseQuery<ParseObject> query = new ParseQuery("ArtifactTypes");
				  	  	query.whereContainedIn("artifactID",as);
				  	  	return query;
				    } //End of Create()
				  }); //End of Adapter Intialization
		
		adapter.setTextKey("artifactName");
		adapter.setImageKey("artifactImage");
		
		
		
		ListView listView = (ListView) view.findViewById(R.id.artifactsOwnedList);
		listView.setAdapter(adapter);
	}//End of loadUserArtifacts
 
}