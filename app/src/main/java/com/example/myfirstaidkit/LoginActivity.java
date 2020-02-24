package com.example.myfirstaidkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

        boolean isThemeEdited = prefs.getBoolean("isThemeEdited",false);
        boolean isThemeDark = prefs.getBoolean("isThemeDark",false);

        if (isThemeEdited) {
            if (isThemeDark) {
                setTheme(R.style.AppThemeDark);
            } else {
                setTheme(R.style.AppTheme);
            }
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.AppThemeDark);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.AppTheme);
                    break;
            }
        }

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
