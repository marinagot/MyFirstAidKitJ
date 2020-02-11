package com.example.myfirstaidkit.jobScheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.myfirstaidkit.R;

import static com.example.myfirstaidkit.helpers.Utils.schedule;

public class DailyJob extends JobService {
    private NotificationManager notifManager;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        final int NOTIFY_ID = 0; // ID of notification
        String id = "default_notification_channel_id"; // default_channel_id
        String title = "default_notification_channel_title"; // Default Channel

        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            builder.setContentTitle("Title")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(getApplicationContext().getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker("Title")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(getApplicationContext(), id);
            builder.setContentTitle("Title")                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(getApplicationContext().getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setTicker("Title")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);


        // aqui cosas
        jobFinished(jobParameters, true);
        schedule(getApplicationContext(), 86400000);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //true para reschedulearlo
        //false para dejarlo morir
        return false;
    }
}