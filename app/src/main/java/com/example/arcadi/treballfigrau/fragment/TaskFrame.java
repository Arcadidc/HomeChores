package com.example.arcadi.treballfigrau.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.activity.CalendarActivity;
import com.example.arcadi.treballfigrau.databaseHelpers.VolleyCallBack;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.utils.CustomComparator;
import com.example.arcadi.treballfigrau.utils.IOnBackPressed;
import com.example.arcadi.treballfigrau.utils.RecyclerAdapter;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.general.Task;
import com.example.arcadi.treballfigrau.utils.TasksAdapter;
import com.example.arcadi.treballfigrau.activity.NewTaskActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

public class TaskFrame extends Fragment implements IOnBackPressed  {

    Button buttonAdd;

    private ListView ListViewTasks;

    ArrayList<HashMap<String, String>> tasklist;

    public static List<Task> tasks_list;
    private static final String KEY_DESCR = "desc";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMPLETED = "completed";
    String[] elementos = new String[1];
    ArrayList<String> str = new ArrayList<String>();
    ArrayList Tasks;
    Fragment fr;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    List<Task> list;
    RecyclerAdapter adapter;
    ShowcaseView sv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View myView = inflater.inflate(R.layout.recycler_tasks, container, false);
        buttonAdd= (Button) myView.findViewById(R.id.addButton);
        tasks_list = new ArrayList<Task>();
        recyclerView = myView.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        Fragment fr = (this);
       // load_tasks();
//        adapter.closeMenu();

        ViewTarget target = new ViewTarget(recyclerView);
        sv = new ShowcaseView.Builder(getActivity())
                .setTarget(target)
                .setContentTitle(R.string.Title_tutorial3)
                .setContentText(R.string.Desc_tutorial3)
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(2)
                .build();
        sv.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
        sv.overrideButtonClick(new View.OnClickListener() {
            int count;
            @Override
            public void onClick(View view) {
                count++;
                switch (count){
                    case 1:

                        ViewTarget target = new ViewTarget(buttonAdd);
                        sv.setTarget(Target.NONE);
                        sv.setContentText(getString(R.string.Desc_tutorial4));
                        sv.setContentTitle(getString(R.string.Title_tutorial4));
                        sv.forceTextPosition(ShowcaseView.RIGHT_OF_SHOWCASE);
                        sv.show();
                        break;
                    case 2:
                        sv.hide();
                        break;
                }

            }
        });












        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                adapter.closeMenu();
            }
        });

        return myView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_task, menu);
        if ( SharedPrefManager.getInstance(getActivity()).getAdmin() == 0) {
            menu.getItem(1).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.addButton:
                startActivity(new Intent(getActivity(), NewTaskActivity.class));
                return true;
            case R.id.CalendarButton:
                startActivity(new Intent(getActivity(), CalendarActivity.class));
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


    }

    @Override
    public void onResume() {
        load_tasks();
        super.onResume();
    }


    private RecyclerAdapter GetAdapter(){
        return adapter;
    }


    private HashMap<String, String>createTask(String name,String number){
        HashMap<String, String> Tasks = new HashMap<String, String>();
        Tasks.put(name, number);
        return Tasks;
    }



private void load_tasks(){

    String id =  String.valueOf(SharedPrefManager.getInstance(getActivity()).getID());
    String uri = String.format(Constants.URL_GET_TASKS+"?PID="+id);
    tasks_list.clear();
    String Assigned_to = SharedPrefManager.getInstance(getActivity()).getUsername();

    StringRequest stringRequest = new StringRequest(Request.Method.GET,
            uri,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                    //    list = new ArrayList<>();
                        JSONObject obj = new JSONObject(response);
                        JSONArray dataArray  = obj.getJSONArray("Task");

                      //  final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.recyclerview);


                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject dataobj = dataArray.getJSONObject(i);

                            Integer id =  dataobj.getInt("Id");
                            String name =  dataobj.getString("Name");
                            Integer points =  dataobj.getInt("Points");

                            String description =  dataobj.getString("Description");
                            Integer completed = dataobj.getInt("Completed");
                            String Created_by_name = dataobj.getString("Created_by_name");

                            Task newtask = new Task (id,name,points,description);
                            newtask.setCompleted ( completed);

                            String date_end = dataobj.getString("Date_end");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date_limit = sdf.parse(date_end);
                            newtask.setDate_end(date_limit);


                          Integer Repetitive = dataobj.getInt("Repetitive");
                          if ( Repetitive == 1 ){
                              newtask.setRepetitive(true);
                          }

                            String Assigned_to = SharedPrefManager.getInstance(getActivity()).getUsername();
                            newtask.setCreated_by_name(Created_by_name);
                            newtask.setAssigned_to_name(SharedPrefManager.getInstance(getActivity()).getUsername());

                            tasks_list.add(newtask);
                            // TaskObject.setName(name);
                            //  TaskObject.setId(Integer.parseInt(id));
                            //  listOfTaskObject.add(TaskObject);
                            //         elementos[i] = dataobj.getString("Name");
                            Collections.sort(tasks_list, new CustomComparator());
                            str.add("ID: "+ id + "\nTitle:" + name);

                        }
                        adapter = new RecyclerAdapter(getActivity(), tasks_list);
                     //   recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);



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
    RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
}

    private void change_completed (final int pid, final int Points){

        final String id_user = String.valueOf(SharedPrefManager.getInstance(getActivity()).getID());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.COMPLETE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("PID", String.valueOf(pid));
                params.put("Id_User", id_user);
                params.put("Points", String.valueOf(Points));
                return params;
            }
        };
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onBackPressed() {
        if (adapter.isMenuShown()) {
            adapter.closeMenu();
            return true;
        } else {
            return false;
        }
    }
}










