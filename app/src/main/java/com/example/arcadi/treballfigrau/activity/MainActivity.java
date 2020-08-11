package com.example.arcadi.treballfigrau.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.arcadi.treballfigrau.general.Constants;
import com.example.arcadi.treballfigrau.general.Family;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.databaseHelpers.RequestHandler;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.utils.SpinAdapter;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private ProgressDialog progressDialog;
    private TextView textViewLogin;
    private Spinner SpinnerFamilies;
    private ArrayList<Family> Families;
    final HashMap<Integer,String> spinnerMap = new HashMap<>();
    private SpinAdapter adapter;
    private Family family1;
    private CheckBox CheckboxAdmin;
    private Integer Admin;
    ShowcaseView sv;
    ShowcaseView sv1;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        context = this;

        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextUsername =  findViewById(R.id.editTextUsername);
        editTextPassword =  findViewById(R.id.editTextPassword);
        textViewLogin =  findViewById(R.id.textViewLogin);
        buttonRegister =  findViewById(R.id.buttonRegister);
        SpinnerFamilies =  findViewById(R.id.spinner_family);
        CheckboxAdmin =  findViewById(R.id.checkBox1);
        ViewTarget target = new ViewTarget(SpinnerFamilies);
        sv = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(R.string.Title_tutorial1)
                .setContentText(R.string.Desc_tutorial1)
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(4)
                .build();
        sv.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
        sv.overrideButtonClick(new View.OnClickListener() {
            int count;
            @Override
            public void onClick(View view) {
                count++;
                switch (count){
                    case 1:
                        ViewTarget target = new ViewTarget(buttonRegister);
                        sv.hide();
                        sv1 = new ShowcaseView.Builder(MainActivity.this)
                                .setTarget(target)
                                .setContentTitle(R.string.Title_tutorial2)
                                .setContentText(R.string.Desc_tutorial2)
                                .setStyle(R.style.CustomShowcaseTheme)
                                .build();
                        sv1.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                        break;
                    case 2:
                        sv1.hide();
                        break;
                }

            }
        });

        progressDialog = new ProgressDialog(this);
        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);


        populate_spinner();

    }

    private void populate_spinner() {
        SpinnerFamilies =  findViewById(R.id.spinner_family);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.URL_ALL_FAMILIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Families = new ArrayList<>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray  = obj.getJSONArray("family");

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataobj = dataArray.getJSONObject(i);

                                spinnerMap.put(dataobj.getInt("Family_id"),dataobj.getString("Name"));
                                Families.add(new Family(dataobj.getInt("Family_id"), dataobj.getString("Name")));

                            }
                            adapter = new SpinAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,
                                    Families);
                            SpinnerFamilies.setAdapter(adapter);
                            SpinnerFamilies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    // Here you get the current item (a User object) that is selected by its position
                                     family1 = adapter.getItem(position);
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
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final Integer family_id = family1.getId();


        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1){
                            Toast.makeText(getApplicationContext(), "User registered successfully" ,  Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));}
                            else{
                                Toast.makeText(getApplicationContext(),"There's been an error. Email already in use?",Toast.LENGTH_LONG).show();
                            }
                       

                            } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            return;
                        }
                    }
                    },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),"There's been an error. Email already in use?",Toast.LENGTH_LONG).show();
                   //    finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<>();
                params.put("Name", username);
                params.put("Email", email);
                params.put("Password", password);
                params.put("Family", String.valueOf(family_id));
                params.put("Admin",String.valueOf(Admin));
                return params;
            }
        };
        if (isValidEmail(email)){

            RequestHandler.getInstance(this).addToRequestQueue(stringRequest); }
        else{
            progressDialog.hide();
            Toast.makeText(getApplicationContext(),"Invalid Email.",Toast.LENGTH_LONG).show(); }

    }




    @Override
    public void onClick(View view){
    if (view == buttonRegister){

        if (CheckboxAdmin.isChecked()){
            Admin  = 1 ;
        }

        registerUser();
      }
    if (view == textViewLogin){
        startActivity(new Intent(this, LoginActivity.class));
    }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
