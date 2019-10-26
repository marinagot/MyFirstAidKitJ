package com.example.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        /*if (prefs.getString("id", null) != null) {
            Intent intent = new Intent(this, LoggedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {*/
            setContentView(R.layout.activity_login);
        /*}*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}
