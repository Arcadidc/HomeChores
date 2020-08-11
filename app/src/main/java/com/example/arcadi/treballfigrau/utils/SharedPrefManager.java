package com.example.arcadi.treballfigrau.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager minstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_FAMILY = "userfamily";
    private static final String KEY_USER_POINTS = "userpoints";
    private static final String KEY_ADMIN = "useradmin";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (minstance == null) {
            minstance = new SharedPrefManager(context);
        }
        return minstance;
    }

    public boolean userLogin(int id, String username, String email, int family, int points, int admin){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_USER_FAMILY, family);
        editor.putInt(KEY_USER_POINTS, points);
        editor.putInt(KEY_ADMIN, admin);
        editor.apply();

        return true;


    }

    public int getID(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_USER_ID, 0);
        return id;
    }

    public void setFamily(int family){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_FAMILY, family);
        editor.apply();
    }


    public int getFamily(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_USER_FAMILY, 0);
        return id;
    }

    public String getUsername(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        return username;
    }

    public String getEmail(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        return email;
    }

    public void addPoints(int i){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int points = sharedPreferences.getInt(KEY_USER_POINTS, 0) + i;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_POINTS, points);
        editor.apply();

    }

    public int getPoints(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_USER_POINTS, 0);
        return id;
    }

    public int getAdmin(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt(KEY_ADMIN, 0);
        return id;
    }


    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                if (sharedPreferences.getString(KEY_USERNAME , null) != null) {
            return true;

                }
            return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;

    }

    public void subPoints(Integer i) {
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int points = sharedPreferences.getInt(KEY_USER_POINTS, 0) - i;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_POINTS, points);
        editor.apply();
    }
}