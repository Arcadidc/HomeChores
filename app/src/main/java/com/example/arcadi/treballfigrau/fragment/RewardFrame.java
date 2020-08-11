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
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.general.Reward;
import com.example.arcadi.treballfigrau.utils.RewardAdapter;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.activity.NewRewardActivity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RewardFrame extends Fragment {

    TextView current_points ;
    Button buttonAdd;
    ShowcaseView sv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (SharedPrefManager.getInstance(getActivity()).getAdmin() == 1) {
            setHasOptionsMenu(true);
        }
        View myView = inflater.inflate(R.layout.fragment_reward, container, false);
        current_points  = (TextView) myView.findViewById(R.id.your_points);
        int points = SharedPrefManager.getInstance(getActivity()).getPoints();
        current_points.setText("@string/Your_Points"+ " " + String.valueOf(points));
        buttonAdd= (Button) myView.findViewById(R.id.addButton);
        show_tutorial();
        return myView;

    }

    private void show_tutorial() {



        sv = new ShowcaseView.Builder(getActivity())
                .setTarget(Target.NONE)
                .setContentTitle(R.string.Title_tutorial7)
                .setContentText(R.string.Desc_tutorial7)
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(3)
                .build();
        sv.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_task, menu);
        if ( SharedPrefManager.getInstance(getActivity()).getAdmin() == 0){
            menu.getItem(1).setVisible(false);
        }
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addButton:
                startActivity(new Intent(getActivity(), NewRewardActivity.class));
                return true;
        }
        return false;
    }


    @Override
    public void onResume() {
        load_rewards();
        super.onResume();
    }

    private void load_rewards() {
        current_points.setText( getContext().getResources().getString((R.string.Your_Points)) + " " + String.valueOf(SharedPrefManager.getInstance(getActivity()).getPoints()));
        final ListView listView = (ListView) getView().findViewById(R.id.list_view_rewards);

        String id =  String.valueOf(SharedPrefManager.getInstance(getActivity()).getFamily());
        String uri = String.format(Constants.URL_GET_REWARDS+"?FAMILY="+id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            final ArrayList<Reward> arrayOfRewards = new ArrayList<Reward>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray  = obj.getJSONArray("Rewards");
                            final RewardAdapter adapter = new RewardAdapter(getActivity(), arrayOfRewards);
                            final ListView listView = (ListView) getView().findViewById(R.id.list_view_rewards);
                            listView.setAdapter(adapter);

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                String Name =  dataobj.getString("Name");
                                Integer Points =  dataobj.getInt("Points_needed");

                                String Description =  dataobj.getString("Description");

                                Integer Pid = dataobj.getInt("ID");




                                Reward newReward = new Reward (Pid,Name,Points,Description);
                                adapter.add(newReward);


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
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

    }










}


