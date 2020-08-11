package com.example.arcadi.treballfigrau.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.general.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener  {
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    Calendar date;
    Spinner SpinnerUsers;
    private ArrayList<String> Users;
    final HashMap<String,Integer> spinnerMap = new HashMap <String,Integer>();
    private ArrayAdapter adapter;
    private User user1;
    private Button  buttonSubmit;
    EditText editTaskTitle , editTextDescription , editText1 , Points;
    ToggleButton d1;
    ToggleButton d2;
    ToggleButton d3;
    ToggleButton d4;
    ToggleButton d5;
    ToggleButton d6;
    ToggleButton d7;
    LocalDate Ld;
    ZoneId zoneId;
    private ArrayList<Long> Repeat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        eText = (EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(eText);
            }
        });
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(this);

        d1 = (ToggleButton) findViewById(R.id.M);
        d2 = (ToggleButton) findViewById(R.id.Tu);
        d3 = (ToggleButton) findViewById(R.id.W);
        d4 = (ToggleButton) findViewById(R.id.Th);
        d5 = (ToggleButton) findViewById(R.id.F);
        d6 = (ToggleButton) findViewById(R.id.Sa);
        d7 = (ToggleButton) findViewById(R.id.Su);
        editTaskTitle = (EditText) findViewById(R.id.editTaskTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editText1 = (EditText) findViewById(R.id.editText1);
        Points = (EditText) findViewById(R.id.Points);


        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);
        d5.setOnClickListener(this);
        d6.setOnClickListener(this);
        d7.setOnClickListener(this);

        Ld = LocalDate.now();
        zoneId = ZoneId.systemDefault();

        populate_spinner();

    }


    public void showDateTimePicker(final EditText e) {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        e.setText(format1.format(date.getTime()));

                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();


    }


    private void populate_spinner() {
        SpinnerUsers = (Spinner) findViewById(R.id.spinner_users);

        String id =  String.valueOf(SharedPrefManager.getInstance(this).getFamily());
        String id_user =  String.valueOf(SharedPrefManager.getInstance(this).getID());
        String uri = String.format(Constants.URL_GET_FAMILY+"?FAMILY="+id+"&ID_user="+id_user);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Users = new ArrayList<>();

                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray  = obj.getJSONArray("Family");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                //spinnerMap.put(dataobj.getInt("Family_id"),dataobj.getString("Name"));
                                Users.add(dataobj.getString("Name"));
                                spinnerMap.put( dataobj.getString("Name"),dataobj.getInt("PID") );

                            }
                            Collections.reverse(Users);
                            adapter = new ArrayAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,
                                    Users);
                            SpinnerUsers.setAdapter(adapter);
                            SpinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    // Here you get the current item (a User object) that is selected by its position
                                   // user1 = adapter.getItem(position);
                                    // Here you can do the action you want to...

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapter) {  }
                            });

                            //  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), simple_spinner_item, Families);
                            //     spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            //   SpinnerFamilies.setAdapter(spinnerArrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonSubmit:

                final String Title = editTaskTitle.getText().toString().trim();
                final String Description = editTextDescription.getText().toString().trim();
                final String Date_end = editText1.getText().toString().trim();
                final String Points1 = Points.getText().toString().trim();
                final String created_by = String.valueOf(SharedPrefManager.getInstance(this).getID());
                final String assigned_to =  String.valueOf(spinnerMap.get(SpinnerUsers.getSelectedItem().toString()));

                populate_repeat();

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_CREATE_TASK,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(), "Task registered successfully" ,  Toast.LENGTH_LONG).show();
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("Name", Title);
                        params.put("Description", Description);
                        params.put("Date_end", Date_end);
                        params.put("Created_by", created_by);
                        params.put("Assigned_to", assigned_to);
                        params.put("Points", Points1);
                        for (int i=0; i<Repeat.size();i++){
                            params.put("Repeat"+i, String.valueOf((Repeat.get(i))));
                        }
                        return params;
                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

                break;

        }

        }

    private void populate_repeat() {

        if (d1.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d2.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d3.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d4.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d5.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d6.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }
        if (d7.isChecked()){
            Ld = Ld.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            long epoch = Ld.atStartOfDay(zoneId).toEpochSecond();
            Repeat.add(epoch);
            Ld = LocalDate.now();
        }






    }

}

