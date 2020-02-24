package com.example.myfirstaidkit.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;

import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.example.myfirstaidkit.jobScheduler.DailyJob;
import com.example.myfirstaidkit.jobScheduler.DoseJob;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final int MEDICINES_JOB_ID = 1;
    public static final int DOSE_JOB_ID = 2;

    public static boolean isJobServiceOn(Context context, int id) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);

        for (JobInfo jobInfo : scheduler.getAllPendingJobs())
            if (jobInfo.getId() == id)
                return true;

        return false;
    }

    public static void scheduleDaily(Context context, long intervalMillis) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        ComponentName componentName = new ComponentName(context, DailyJob.class);

        JobInfo.Builder builder = new JobInfo.Builder(MEDICINES_JOB_ID, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(intervalMillis);
        builder.setPersisted(true);
        builder.setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR);

        scheduler.schedule(builder.build());
    }

    public static void scheduleDose(Context context, PersistableBundle bundle) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        ComponentName componentName = new ComponentName(context, DoseJob.class);

        JobInfo.Builder builder = new JobInfo.Builder(bundle.getString("rel_id").hashCode(), componentName);
        builder.setMinimumLatency(bundle.getInt("rel_frequency") * 3600000); // ms
        builder.setPersisted(true);
        builder.setBackoffCriteria(500, JobInfo.BACKOFF_POLICY_LINEAR);
        builder.setExtras(bundle);

        scheduler.schedule(builder.build());
    }

    public static void removeSchedule(Context context, int id) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        scheduler.cancel(id);
    }

    public static void removeSchedule(Context context) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        scheduler.cancelAll();
    }

    // Calcular la hora actual del dia y restar lo que le falte para las 12 de la mañana del dia siguiente
    public static Long getMilisecondsToNextHour(int hour) {
        Calendar midDay = Calendar.getInstance();
        midDay.set(Calendar.HOUR_OF_DAY, hour);
        midDay.set(Calendar.MINUTE, 0);
        midDay.set(Calendar.SECOND, 0);
        Long now = Calendar.getInstance().getTimeInMillis();

        if (now < midDay.getTimeInMillis())
            return midDay.getTimeInMillis() - now;
        else
            return midDay.getTimeInMillis() + 86400000 - now;
    }

    public static void prepareDoseNotification(Context context, int NOTIFY_ID, Medicine med, Treatment treatment) {

        final String content_1 = "Es hora de que te tomes tu medicina para ";

        String content = content_1 + treatment.getName();
        int icon = android.R.drawable.ic_popup_reminder;

        fireNotification(context, NOTIFY_ID, content, med.getName(), icon);
    }

    public static void prepareNotification(Context context, int NOTIFY_ID, int NOTIFY_TYPE, Medicine med) {

        final String content_2 = "Parece que te estas quedando sin ";
        final String content_2_b = ". Te quedan ";
        final String content_2_c = ". Deberías pasar por una farmacia.";
        final String content_3 = "¡Vaya! Te has quedado sin ";
        final String content_3_b = " en pleno tratamiento. Deberías pasar por una farmacia.";
        final String content_4 = "Parece que esta caja está a punto de caducar. Caduca el día ";
        final String content_5 = "Parece que esta caja caducó el día ";
        final String content_5_b = ". Deberías reciclarla en una farmacia.";

        String content;
        int icon = android.R.drawable.ic_dialog_alert;
        String expiration_date = new SimpleDateFormat("dd MMM yyyy").format(new Date(med.getExpirationDate()));

        switch (NOTIFY_TYPE) {
            case 2:
                content = content_2 + med.getName() + content_2_b + med.getDoseNumber() + content_2_c;
                break;
            case 3:
                content = content_3 + med.getName() + content_3_b;
                break;
            case 4:
                content = content_4 + expiration_date + ".";
                break;
            case 5:
                content = content_5 + expiration_date + content_5_b;
                break;
            default: content = "";
        }

        fireNotification(context, NOTIFY_ID, content, med.getName(), icon);
    }

    public static void fireNotification(Context context, int NOTIFY_ID, String content, String title, int icon) {

        String channel_id = "default_notification_channel_id";          // default_channel_id
        String channel_title = "default_notification_channel_title";    // Default Channel

        NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(context, channel_id);
        builder.setContentTitle(title)          // required
                .setSmallIcon(icon)             // required
                .setContentText(content)        // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker(title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, channel_title, importance);
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

    @ColorInt
    public static int getColorByAttributeId(Context context, @AttrRes int attrIdForColor){
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attrIdForColor, typedValue, true);
        return typedValue.data;
    }

}
