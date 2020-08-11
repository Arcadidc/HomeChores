package com.example.arcadi.treballfigrau.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.general.User;

import java.util.ArrayList;

public class FamilyAdapter  extends ArrayAdapter<User> {
    public FamilyAdapter(Context context, ArrayList<User> arrayOfUsers) {
        super(context, 0, arrayOfUsers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        TextView Points = (TextView) convertView.findViewById(R.id.Points);
        TextView Description = (TextView) convertView.findViewById(R.id.Name_surname);
        TextView Email = (TextView) convertView.findViewById(R.id.Email);


        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        Points.setText("Points: "+String.valueOf(user.getPoints()));
        Email.setText("Email: "+String.valueOf(user.getEmail()));


        // Return the completed view to render on screen
        return convertView;
    }
}
