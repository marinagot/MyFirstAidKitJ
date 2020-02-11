package com.example.myfirstaidkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.myfirstaidkit.jobScheduler.DailyJob;

import java.util.Calendar;

import static com.example.myfirstaidkit.helpers.Utils.getMilisecondsToNextThreePM;
import static com.example.myfirstaidkit.helpers.Utils.isJobServiceOn;
import static com.example.myfirstaidkit.helpers.Utils.schedule;

/**
 * Created by azem on 11/3/17.
 */

public class BootReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {

        // Your code to execute when Boot Completd



        // Completamente innecesario
        if (!isJobServiceOn(context))
            schedule(context, getMilisecondsToNextThreePM());

        /*
        Intent i = new Intent(context, AlarmBootService.class);
        context.startService(i);
        */

        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

    }

}
