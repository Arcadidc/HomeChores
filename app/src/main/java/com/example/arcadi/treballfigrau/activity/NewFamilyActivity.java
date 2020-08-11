package com.example.arcadi.treballfigrau.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewFamilyActivity extends AppCompatActivity implements View.OnClickListener  {
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    Calendar date;
    Button button_family;
    EditText familyname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_family);
        button_family = (Button) findViewById(R.id.button_family);
        button_family.setOnClickListener(this);
        familyname = (EditText) findViewById(R.id.Family_Name);
    }


    @Override
    public void onClick(View view) {
        if (view == button_family){
            final String Name = familyname.getText().toString().trim();
            final String id =  String.valueOf(SharedPrefManager.getInstance(this).getID());



            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_CREATE_FAMILY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getApplicationContext(), "Family registered successfully" ,  Toast.LENGTH_LONG).show();
                               SharedPrefManager.getInstance(NewFamilyActivity.this).setFamily(jsonObject.getInt("Family_id"));
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
                    params.put("Name", Name);
                    params.put("Id", id);
                    return params;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        }

    }



}





