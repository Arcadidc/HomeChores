package com.example.arcadi.treballfigrau.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewRewardActivity extends AppCompatActivity implements View.OnClickListener  {


        private Button  buttonSubmit;
        EditText editRewardTitle , editRewardDescription , EditRewardPoints ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_reward);

            buttonSubmit = (Button) findViewById(R.id.buttonSubmitReward);
            buttonSubmit.setOnClickListener(this);

            editRewardTitle = (EditText) findViewById(R.id.editNameReward);
            editRewardDescription = (EditText) findViewById(R.id.editDescriptionReward);
            EditRewardPoints = (EditText) findViewById(R.id.Points_reward);



        }

        @Override
        public void onClick(View view) {
            if (view == buttonSubmit){
                final String Title = editRewardTitle.getText().toString().trim();
                final String Description = editRewardDescription.getText().toString().trim();
                final String Points = EditRewardPoints.getText().toString().trim();


                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_CREATE_REWARD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(), "Reward registered successfully" ,  Toast.LENGTH_LONG).show();
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
                        params.put("Points", Points);
                        params.put("Family", String.valueOf(SharedPrefManager.getInstance(getApplication()).getFamily()));
                        return params;
                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

            }

        }
    }










