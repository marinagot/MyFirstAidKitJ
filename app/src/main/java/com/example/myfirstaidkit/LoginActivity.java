package com.example.myfirstaidkit;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity
        implements login.OnFragmentInteractionListener,
        create_account.OnFragmentInteractionListener,
        home.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}
