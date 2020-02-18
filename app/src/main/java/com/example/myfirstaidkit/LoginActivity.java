package com.example.myfirstaidkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.myfirstaidkit.helpers.Utils.MEDICINES_JOB_ID;
import static com.example.myfirstaidkit.helpers.Utils.removeSchedule;


public class LoginActivity extends AppCompatActivity
        implements login.OnFragmentInteractionListener,
        create_account.OnFragmentInteractionListener,
        home.OnFragmentInteractionListener {

    //Preferencias de la aplicación
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_login);

        // Elimina todos los jobs
        removeSchedule(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}
