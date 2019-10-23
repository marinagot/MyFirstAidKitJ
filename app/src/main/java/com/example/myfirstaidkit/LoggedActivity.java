package com.example.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myfirstaidkit.data.DataBaseOperations;

public class LoggedActivity extends AppCompatActivity
        implements home.OnFragmentInteractionListener,
        treatments.OnFragmentInteractionListener,
        first_aid_kit.OnFragmentInteractionListener,
        settings.OnFragmentInteractionListener,
        account.OnFragmentInteractionListener,
        treatment_edit.OnFragmentInteractionListener,
        medicine_edit.OnFragmentInteractionListener {


    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    DataBaseOperations us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fak, R.id.nav_account, R.id.nav_treatments)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        /*NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/
        NavigationUI.setupWithNavController(navView, navController);

        prefs = getApplicationContext().getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
        edit = prefs.edit();
        us = DataBaseOperations.get_Instance(getApplicationContext());

        setTitle("Home");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;


        if (id == R.id.action_settings) {
            fragmentClass = settings.class;
            setTitle(item.getTitle());
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_account) {
            fragmentClass = account.class;
            setTitle(item.getTitle());
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_logout) {
            edit.remove("username");
            edit.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else return super.onOptionsItemSelected(item);

        if (fragmentClass != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        }

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);


        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}
