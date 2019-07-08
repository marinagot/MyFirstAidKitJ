package com.example.myfirstaidkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myfirstaidkit.data.DataBaseOperations;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        home.OnFragmentInteractionListener,
        login.OnFragmentInteractionListener,
        treatments.OnFragmentInteractionListener,
        first_aid_kit.OnFragmentInteractionListener,
        settings.OnFragmentInteractionListener,
        account.OnFragmentInteractionListener,
        create_account.OnFragmentInteractionListener,
        treatment_edit.OnFragmentInteractionListener,
        medicine_edit.OnFragmentInteractionListener {

    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    DataBaseOperations us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getApplicationContext().getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
        edit = prefs.edit();
        us = DataBaseOperations.get_Instance(getApplicationContext());

        setTitle("Login");

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
                //getFragmentManager().beginTransaction().replace(R.id.content, new medicine_edit()).commit();
                //getActivity().setTitle("Home");

                //Fragment fragment = null;

                //setTitle("Edit medicine");
                //Class fragmentClass = medicine_edit.class;

                //try {
                //    fragment = (Fragment) fragmentClass.newInstance();
                //} catch (Exception e) {
                //    e.printStackTrace();
                //}

                // Insert the fragment by replacing any existing fragment
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragmentManager.getBackStackEntryCount()>0) {
            //fragmentManager.beginTransaction().replace(R.id.content, fragmentManager.findFragmentById(fragmentManager.getBackStackEntryAt(0).getId())).commit();
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        boolean logged = us.userIsLogged(prefs);

        if (!logged){
            fragmentClass = login.class;
            setTitle("Login");
        }
        else{
            if (id == R.id.action_settings) {
                fragmentClass = settings.class;
                setTitle(item.getTitle());
            } else if (id == R.id.action_account) {
                fragmentClass = account.class;
                setTitle(item.getTitle());
            } else if (id == R.id.action_logout) {
                edit.remove("username");
                edit.apply();
                fragmentClass = login.class;
                setTitle("Login");
            } else return super.onOptionsItemSelected(item);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        /*NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavController navController = navHost.getNavController();*/

        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();
        boolean logged = us.userIsLogged(prefs);
        if (!logged){
            fragmentClass = login.class;
            setTitle("Login");
        }
        else {
            if (id == R.id.nav_home) {
                fragmentClass = home.class;
            } else if (id == R.id.nav_treatments) {
                fragmentClass = treatments.class;
            } else if (id == R.id.nav_fak) {
                fragmentClass = first_aid_kit.class;
            }
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        item.setChecked(false);

        if (logged) {
            // Highlight the selected item has been done by NavigationView
            item.setChecked(true);
            // Set action bar title
            setTitle(item.getTitle());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }



}

