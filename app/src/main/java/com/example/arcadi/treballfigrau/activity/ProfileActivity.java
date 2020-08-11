package com.example.arcadi.treballfigrau.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arcadi.treballfigrau.fragment.ProfileFrame;
import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.fragment.RewardFrame;
import com.example.arcadi.treballfigrau.utils.IOnBackPressed;
import com.example.arcadi.treballfigrau.utils.SharedPrefManager;
import com.example.arcadi.treballfigrau.fragment.TaskFrame;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class ProfileActivity extends AppCompatActivity{

    ShowcaseView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new TaskFrame()).commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new ProfileFrame();
                        break;
                    case R.id.navigation_dashboard:
                        selectedFragment = new TaskFrame();
                        break;
                    case R.id.navigation_notifications:
                        selectedFragment = new RewardFrame();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addButton:
                return false;


        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}








