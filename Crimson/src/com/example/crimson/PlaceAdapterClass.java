package com.example.crimson;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceAdapterClass extends ArrayAdapter<Place> {

    Context mContext;
    int resource;
	
    
    public PlaceAdapterClass(Context context, int resource, List<Place> items) {

        super(context, resource, items);

        this.resource = resource;
    
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)   {
        LinearLayout placeView;
        //Get the current pert object
        Place p = getItem(position);
        
        //Inflate the view
        if(convertView==null)
        {
            placeView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, placeView, true);
        }
        else
        {
            placeView = (LinearLayout) convertView;
        }
        //Get the text boxes from the listitem.xml file
        TextView placeNameText =(TextView)placeView.findViewById(R.id.placeName);
        TextView placeAddressText =(TextView)placeView.findViewById(R.id.placeAddress);
        TextView healingSpotText =(TextView)placeView.findViewById(R.id.healingSpot); 
    
        placeNameText.setText(p.name);
        placeAddressText.setText(p.address);
        healingSpotText.setText(p.health);
        healingSpotText.setTextColor(0xffff0000);
        
        return placeView;
    }

}