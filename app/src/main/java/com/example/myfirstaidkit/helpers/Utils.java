package com.example.myfirstaidkit.helpers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

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
        midDay.set(Calendar.HOUR_OF_DAY, 12);
        return midDay.getTimeInMillis() + 86400000 - Calendar.getInstance().getTimeInMillis();
    }
}
