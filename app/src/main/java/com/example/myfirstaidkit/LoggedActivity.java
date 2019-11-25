package com.example.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
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
    final AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getApplicationContext().getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
        edit = prefs.edit();
        us = DataBaseOperations.get_Instance(getApplicationContext());

        if (prefs.getString("id", null) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {

            setContentView(R.layout.activity_logged);
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.first_aid_kit, R.id.treatments, R.id.home)
                    .build();
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/

            navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    switch (id) {
                        case R.id.nav_fak:
                            Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.first_aid_kit);
                            break;
                        case R.id.nav_treatments:
                            Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.treatments);
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem syncMenuItem = menu.findItem(R.id.action_refresh);
        FrameLayout rootView = (FrameLayout) syncMenuItem.getActionView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(syncMenuItem);
            }
        });
        syncMenuItem.getActionView().animate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.settings);
                break;
            case R.id.action_account:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.account);
                break;
            case R.id.action_logout:
                edit.clear();
                edit.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.action_refresh:
                // Animar el icono para que gire
                item.getActionView().clearAnimation();
                Animation animation = new RotateAnimation(360.0f, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                animation.setRepeatCount(-1);
                animation.setDuration(2000);
                item.getActionView().setAnimation(animation);

                // Hacer la llamada a base de datos
                new ApiCallThread<String>(new AsyncResponse<String>(){
                    MenuItem item;

                    @Override
                    public String apiCall(Object... params) {
                        item = (MenuItem) params[3];
                        return us.syncDabtabase((String) params[1], (String) params[2]);
                    }

                    @Override
                    public void processFinish(View v, String result){
                       us.setSyncIdLogged(prefs, result);
                        // Recarga la página
                        if (result == null)
                            item.getActionView().clearAnimation();
                        else {
                            Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.first_aid_kit);
                        }
                        // recreate();
                    }
                }).execute(null, us.getIdLogged(prefs), us.getSyncIdLogged(prefs), item);
            default:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
                return false;
        }

        // Highlight the selected item has been done by NavigationView
        /*item.setChecked(true);*/

        return true;
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}
