package com.example.myfirstaidkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by azem on 11/3/17.
 */

public class BootReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {

        // Your code to execute when Boot Completd

        Intent i = new Intent(context, AlarmBootService.class);
        context.startService(i);

        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

    }

}
