package com.example.arcadi.treballfigrau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.fragment.ProfileFrame;
import com.example.arcadi.treballfigrau.fragment.RewardFrame;
import com.example.arcadi.treballfigrau.fragment.TaskFrame;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.general.Task;
import com.example.arcadi.treballfigrau.utils.CustomComparator;
import com.example.arcadi.treballfigrau.utils.IOnBackPressed;
import com.example.arcadi.treballfigrau.utils.RecyclerAdapter;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ShowTask  extends AppCompatActivity {

   // private int id;
    private TextView Title,Description,Limit_Date,Points,created_by,Completed;
    private TextView Repetitive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task);
     int   id =  getIntent().getIntExtra("Task",0);
        Title = findViewById(R.id.Title);
        Description = findViewById(R.id.Description);
        Limit_Date = findViewById(R.id.Limit_Date);
        Points = findViewById(R.id.Points);
        created_by = findViewById(R.id.created_by);
        Completed = findViewById(R.id.Completed);
        Repetitive = findViewById(R.id.Repetitive);

        GetTask(id);

    }

    private void GetTask(int id) {


        String uri = String.format(Constants.URL_GET_TASK_INFO+"?PID="+id);


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //    list = new ArrayList<>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray  = obj.getJSONArray("tasks");
                            final List<Long> Repeating_time = new ArrayList<>();

                            //  final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.recyclerview);


                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                Integer id =  dataobj.getInt("Id");
                                String name =  dataobj.getString("Name");
                                Integer points =  dataobj.getInt("Points");

                                String description =  dataobj.getString("Description");
                                Integer completed = dataobj.getInt("Completed");
                                String Created_by_name = dataobj.getString("Created_by_name");



                                String date_end = dataobj.getString("Date_end");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date_limit = sdf.parse(date_end);

                                String days = "";

                                String[] Repetitive_times_string = dataobj.getString("Repetitive_time").split(",");
                                Repeating_time.clear();
                                for(int j = 0; Repetitive_times_string.length > j; j++) {
                                    Repeating_time.add(Long.parseLong(Repetitive_times_string[j]));
                                }
                                if ( Repeating_time.get(0) != 0)  {
                                    for (int k = 0; k < Repeating_time.size(); k++ ){
                                        Repetitive_times_string[k] = (Instant.ofEpochSecond(Repeating_time.get(k)).atOffset(ZoneOffset.UTC).getDayOfWeek().toString()).substring(0,3);
                                       if ( k < Repeating_time.size() -1 ) {
                                           days = Repetitive_times_string[k] + ",";
                                       } else {
                                           days = Repetitive_times_string[k];
                                       }
                                    }
                                    String[] array = new HashSet<String>(Arrays.asList(Repetitive_times_string)).toArray(new String[0]);
                                    Repetitive.setText("Yes. Days: " + days);
                                } else {
                                    Repetitive.setText("No");
                                }

                                Title.setText(name);
                                Description.setText(description);
                                Limit_Date.setText(date_end);
                                Points.setText(String.valueOf(points));
                                created_by.setText(Created_by_name);
                                if (completed == 1 ){
                                     Completed.setText("Yes");}
                                else{
                                    Completed.setText("No");
                                    }


                            }



                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                }){


        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

}



