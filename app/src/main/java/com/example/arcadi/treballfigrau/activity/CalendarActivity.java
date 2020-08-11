package com.example.arcadi.treballfigrau.activity;

import android.app.ProgressDialog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.databaseHelpers.VolleyCallBack;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.general.Family;
import com.example.arcadi.treballfigrau.general.Task;
import com.example.arcadi.treballfigrau.utils.CustomComparator;
import com.example.arcadi.treballfigrau.utils.EventDecorator;
import com.example.arcadi.treballfigrau.utils.MyAdapterCalendar;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.utils.SpinAdapter;
import com.example.arcadi.treballfigrau.utils.TasksAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity implements OnMonthChangedListener {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private Calendar cal;
    com.prolificinteractive.materialcalendarview.MaterialCalendarView mCalendarView;
    private List<Task> taskList = new ArrayList<>();
    private ListView listView ;
    private List<CalendarDay> events = new ArrayList<>();

    private MyAdapterCalendar adapter;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



    private ArrayList<Task> arrayOfTasks = new ArrayList<Task>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        mCalendarView = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) findViewById(R.id.calendarView);
        final Calendar calendar = Calendar.getInstance();
        listView = (ListView) findViewById(R.id.calendar_list_view);
        adapter = new MyAdapterCalendar(this, taskList);
        mCalendarView.setOnMonthChangedListener(this);
        mCalendarView.addDecorator(new OneDayDecorator());
        try {

            // events =  get_all_tasks(LocalDateTime.now());
            get_all_tasks(LocalDateTime.now(), new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    EventDecorator eventDecorator = new EventDecorator(Color.RED, events);
                    mCalendarView.addDecorator(eventDecorator);
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void get_all_tasks(LocalDateTime Current_Date, final VolleyCallBack callBack) throws ParseException {
        // private List<CalendarDay> events1 = new ArrayList<>();
        int year = Current_Date.getYear();
        int month = Current_Date.getMonthValue();
        int day = Current_Date.getDayOfMonth();

        String intent1 = Constants.URL_ALL_TASKS + "?DATE=" + year + "-" + month + "-" + day +"&FAMILY="+ SharedPrefManager.getInstance(this).getFamily();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                intent1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        try {


                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray = obj.getJSONArray("tasks");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                final List<Long> Repeating_time = new ArrayList<>();
                                Date date = simpleDateFormat.parse(dataobj.getString("Date_end"));



                                final TasksAdapter adapter_task = new TasksAdapter(getApplicationContext(), arrayOfTasks);
                                listView.setAdapter(adapter_task);

                                Integer id =  dataobj.getInt("Id");
                                String name =  dataobj.getString("Name");
                                Integer points =  dataobj.getInt("Points");

                                String description =  dataobj.getString("Description");
                                Integer completed = dataobj.getInt("Completed");


                                Task newtask = new Task (id,name,points,description);
                                newtask.setCompleted ( completed);

                                String[] Repetitive_times_string = dataobj.getString("Repetitive_time").split(",");
                                    Repeating_time.clear();
                                for(int j = 0; Repetitive_times_string.length > j; j++) {
                                    Repeating_time.add(Long.parseLong(Repetitive_times_string[j]));
                                }
                                if ( Repeating_time.get(0) != 0)  {
                                    for (int k = 0; k < Repeating_time.size(); k++ ){
                                        Repetitive_times_string[k] = (Instant.ofEpochSecond(Repeating_time.get(k)).atOffset(ZoneOffset.UTC).getDayOfWeek().toString()).substring(0,3);
                                        newtask.setRepetitive(true);
                                    }
                                   String[] array = new HashSet<String>(Arrays.asList(Repetitive_times_string)).toArray(new String[0]);
                                newtask.setDays(array);
                                }

                                String Assigned_to_name = dataobj.getString("Assigned_to_name");
                                String Created_by_name = dataobj.getString("Created_by_name");

                                String date_end = dataobj.getString("Date_end");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date_limit = sdf.parse(date_end);
                                newtask.setDate_end(date_limit);
                                newtask.setAssigned_to_name(Assigned_to_name);
                                newtask.setCreated_by_name(Created_by_name);
                                adapter_task.add(newtask);


                                String day1 = (String) DateFormat.format("dd", date);
                                String monthNumber = (String) DateFormat.format("MM", date);
                                String year = (String) DateFormat.format("yyyy", date);
                                CalendarDay day = CalendarDay.from(Integer.parseInt(year), Integer.parseInt(monthNumber), Integer.parseInt(day1));

                                events.add(day);
                                Collections.sort(arrayOfTasks, new CustomComparator());
                                callBack.onSuccess();
                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        //  return events1;
    }


    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        try {

            // events =  get_all_tasks(LocalDateTime.now());
            String date1;
            date1 = FORMATTER.format(date.getDate());
            date1 = date1 + " 00:00";

            LocalDateTime datetime  = LocalDateTime.parse(date1, formatter2);
            arrayOfTasks.clear();
            final TasksAdapter adapter_task = new TasksAdapter(getApplicationContext(), arrayOfTasks);
            listView.setAdapter(adapter_task);



            get_all_tasks(datetime, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    EventDecorator eventDecorator = new EventDecorator(Color.RED, events);
                    mCalendarView.addDecorator(eventDecorator);
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

