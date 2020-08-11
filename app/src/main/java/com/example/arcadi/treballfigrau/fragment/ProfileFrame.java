package com.example.arcadi.treballfigrau.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.utils.FamilyAdapter;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.general.User;
import com.example.arcadi.treballfigrau.activity.MainActivity;
import com.example.arcadi.treballfigrau.activity.NewFamilyActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProfileFrame extends Fragment implements View.OnClickListener {
    Button buttonLogOut;
    ArrayList<String> str = new ArrayList<String>();
    Button button_new_family;
    TextView Family_text;
    ShowcaseView sv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);
        buttonLogOut= (Button) myView.findViewById(R.id.Logout);
        button_new_family = (Button) myView.findViewById(R.id.button_new_family);
        button_new_family.setOnClickListener(this);
        if ( SharedPrefManager.getInstance(getActivity()).getAdmin() == 0) {
            button_new_family.setEnabled(false);
        }
        Family_text = (TextView) myView.findViewById(R.id.Family_text);
        show_tutorial();
        return myView;
    }

    private void show_tutorial() {


        ViewTarget target = new ViewTarget(button_new_family);
        sv = new ShowcaseView.Builder(getActivity())
                .setTarget(target)
                .setContentTitle(R.string.Title_tutorial5)
                .setContentText(R.string.Desc_tutorial5)
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(1)
                .build();
        sv.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
        sv.overrideButtonClick(new View.OnClickListener() {
            int count;
            @Override
            public void onClick(View view) {
                count++;
                switch (count){
                    case 1:
                        ViewTarget target = new ViewTarget(Family_text);
                        sv.setTarget(Target.NONE);
                        sv.setContentText(getString(R.string.Desc_tutorial6));
                        sv.setContentTitle(getString(R.string.Title_tutorial6));
                        sv.forceTextPosition(ShowcaseView.RIGHT_OF_SHOWCASE);
                        sv.show();
                        break;
                    case 2:
                        sv.hide();
                        break;
                }

            }
        });






    }

    public TextView getTextView()
    {

        return Family_text;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Logout:
                SharedPrefManager.getInstance(getActivity()).logout();
                startActivity(new Intent(getActivity(), MainActivity.class));

        }
        return false;
    }

    @Override
    public void onResume() {
        fill_data();
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);



        fill_data();


    }

    private void fill_data() {
        final ListView listView = (ListView) getView().findViewById(R.id.list_view_family);

        String id =  String.valueOf(SharedPrefManager.getInstance(getActivity()).getFamily());
        String id_user =  String.valueOf(SharedPrefManager.getInstance(getActivity()).getID());
        String uri = String.format(Constants.URL_GET_FAMILY+"?FAMILY="+id+"&ID_user="+id_user);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            final ArrayList<User> arrayOfUsers = new ArrayList<User>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray  = obj.getJSONArray("Family");
                            final FamilyAdapter adapter = new FamilyAdapter(getActivity(), arrayOfUsers);
                            final ListView listView = (ListView) getView().findViewById(R.id.list_view_family);
                            listView.setAdapter(adapter);

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                String Name =  dataobj.getString("Name");
                                Integer Points =  dataobj.getInt("Points");
                                String Email =  dataobj.getString("Email");

                                String Surname =  dataobj.getString("Surname");

                                Integer Pid = dataobj.getInt("PID");
                                String family_name = dataobj.getString("Family_name");
                                if ( family_name != null) {
                                    Family_text.setText(family_name);
                                } else {
                                    Family_text.setText("No family");
                                }
                                if ( SharedPrefManager.getInstance(getActivity()).getID() == Pid){


                                    TextView name_textview = (TextView)  getView().findViewById(R.id.Name_User);
                                    // TextView surname_textview = (TextView)  getView().findViewById(R.id.Surname_user);
                                    TextView Points_textview = (TextView)  getView().findViewById(R.id.Points_user);
                                    TextView Email_textview = (TextView)  getView().findViewById(R.id.Email_user);

                                    name_textview.setText(Name);
                                    // surname_textview.setText(Surname);
                                    Points_textview.setText(String.valueOf(Points));
                                    Email_textview.setText(Email);


                                } else {

                                    User newuser = new User (Pid,Name,Surname,Points,Email);
                                    adapter.add(newuser);
                                }


                            }


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    //change_completed(arrayOfTasks.get(position).getId());
                                    // Toast.makeText(getActivity(),"Task Completed! Reload to see the changes applied.",Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (JSONException e) {
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

    @Override
    public void onClick(View view){
        if (view == button_new_family){
            //Toast.makeText(getActivity(),"Task Completed! Reload to see the changes applied.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), NewFamilyActivity.class));
        }
    }


}


