package com.example.arcadi.treballfigrau.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.general.Reward;

import java.util.ArrayList;


public class RewardAdapter extends ArrayAdapter<Reward> {
    public RewardAdapter(Context context, ArrayList<Reward> arrayOfRewards) {
        super(context, 0, arrayOfRewards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Reward reward = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_reward, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.Name_reward);
        TextView Points = (TextView) convertView.findViewById(R.id.Points_reward);
        TextView Description = (TextView) convertView.findViewById(R.id.Description_reward);
       // TextView Id = (TextView) convertView.findViewById(R.id.id_reward);


        // Populate the data into the template view using the data object
        Name.setText(reward.getName());
        Points.setText( getContext().getResources().getString((R.string.Points)) + ": " + String.valueOf(reward.getPoints()));
        Description.setText( getContext().getResources().getString((R.string.Description_task_title)) +": " +reward.getDescription());
       // Id.setText("ID: "+String.valueOf(reward.getId()));


        // Return the completed view to render on screen
        return convertView;
    }
}
