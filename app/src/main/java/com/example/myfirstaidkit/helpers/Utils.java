package com.example.myfirstaidkit.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.example.myfirstaidkit.jobScheduler.DailyJob;

import java.util.Calendar;

public class Utils {

    private static final int JOB_ID = 1;

    public static boolean isJobServiceOn( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        boolean hasBeenScheduled = false ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == 1 ) {
                hasBeenScheduled = true ;
                break ;
            }
        }

        return hasBeenScheduled ;
    }

    public static void schedule(Context context, long intervalMillis) {

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        ComponentName componentName =
                new ComponentName(context, DailyJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(intervalMillis);
        builder.setPersisted(true);
        builder.setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR);
        jobScheduler.schedule(builder.build());
    }

    // Calcular la hora actual del dia y restar lo que le falte para las 12 de la mañana del dia siguiente
    public static Long getMilisecondsToNextThreePM() {
        Calendar midDay = Calendar.getInstance();
        midDay.set(Calendar.HOUR_OF_DAY, 18);
        midDay.set(Calendar.MINUTE, 21);
        midDay.set(Calendar.SECOND, 0);
        Long now = Calendar.getInstance().getTimeInMillis();

        if (now < midDay.getTimeInMillis())
            return midDay.getTimeInMillis() - now;
        else
            return midDay.getTimeInMillis() + 86400000 - now;
    }

    public static void fireNotification(Context context, int NOTIFY_ID, Medicine med, Treatment treatment) {

        final String content_1 = "Es hora de que te tomes tu medicina para ";
        final String content_2 = "Parece que te estas quedando sin ";
        final String content_2_b = ". Te quedan ";
        final String content_2_c = ". Deberías pasar por una farmacia";
        final String content_3 = "¡Vaya! Te has quedado sin ";
        final String content_3_b = " en pleno tratamiento. Deberías pasar por una farmacia";
        final String content_4 = "Parece que esta caja está a punto de caducar. Caduca el día ";
        final String content_5 = "Parece que esta caja caducó el día ";
        final String content_5_b = ". Deberías reciclarla en una farmacia";

        String content;
        int icon = android.R.drawable.ic_dialog_alert;
        switch (NOTIFY_ID) {
            case 1:
                content = content_1 + treatment.getName();
                icon = android.R.drawable.ic_popup_reminder;
                break;
            case 2:
                content = content_2 + med.getName() + content_2_b + med.getDoseNumber() + content_2_c;
                break;
            case 3:
                content = content_3 + med.getName() + content_3_b;
                break;
            case 4:
                content = content_4 + med.getExpirationDate();
                break;
            case 5:
                content = content_5 + med.getExpirationDate() + content_5_b;
                break;
            default: content = "";
        }
        String channel_id = "default_notification_channel_id"; // default_channel_id
        String title = "default_notification_channel_title"; // Default Channel

        NotificationCompat.Builder builder;
        NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(context, channel_id);
        builder.setContentTitle(med.getName())                        // required
                .setSmallIcon(icon)                                   // required
                .setContentText(content)                              // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker(med.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, title, importance);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
        }
        else {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content)).build();
        notifManager.notify(NOTIFY_ID, notification);
    }
}
