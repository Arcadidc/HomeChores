package com.example.arcadi.treballfigrau.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.general.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_task, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
       // TextView tvHome = (TextView) convertView.findViewById(R.id.PID);
        TextView Points = (TextView) convertView.findViewById(R.id.Points);
        TextView Description = (TextView) convertView.findViewById(R.id.Description);
        TextView Completed = (TextView) convertView.findViewById(R.id.Completed);
        TextView Limit_date = (TextView) convertView.findViewById(R.id.Limit_date);
        TextView Assigned_to_name = (TextView) convertView.findViewById(R.id.created_by);
        TextView Created_by_name = (TextView) convertView.findViewById(R.id.Assigned_to);
        TextView Repetitive = (TextView) convertView.findViewById(R.id.Repetitive);
        // Populate the data into the template view using the data object
        tvName.setText(task.getName());
      //  tvHome.setText(String.valueOf((task.getId())));
        Points.setText(getContext().getResources().getString((R.string.Points))+": "+String.valueOf(task.getPoints()));
        Description.setText(getContext().getResources().getString((R.string.Description_task_title))+ ": " +task.getDescription());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Limit_date.setText(getContext().getResources().getString((R.string.end_date_task))+ ": " +formatter.format(task.getDate_end()));
        Assigned_to_name.setText(getContext().getResources().getString((R.string.Task_assigned))+ ": " + String.valueOf(task.getAssigned_to_name()));
        Assigned_to_name.setText(getContext().getResources().getString((R.string.Task_created_by))+ ": " + String.valueOf(task.getCreated_by_name()));

        if  (task.getRepetitive() && (task.getDays() != null)){
            String concatenated_Days = "Repetitive: YES Days: ";
            String[] days = task.getDays();
            for (int i = 0; i < task.getDays().length; i++) {
                if (i ==  (task.getDays().length - 1 ) ){
                concatenated_Days += days[i];
            } else {
                    concatenated_Days += days[i] + ",";
                }
                }
            Repetitive.setText (concatenated_Days);
        }
        else if (task.getRepetitive()) {
            Repetitive.setText(getContext().getResources().getString(R.string.Repetitive) + ": " + getContext().getResources().getString((R.string.Yes)));
        } else {
            Repetitive.setText(getContext().getResources().getString(R.string.Repetitive) + ": " + getContext().getResources().getString((R.string.No)));
        }


        if ( task.getCompleted() == 1){
            convertView.setBackgroundColor(Color.GREEN);
            Repetitive.setText(getContext().getResources().getString(R.string.Completed) + ": " + getContext().getResources().getString((R.string.Yes)));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            Repetitive.setText(getContext().getResources().getString(R.string.Completed) + ": " + getContext().getResources().getString((R.string.No)));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
