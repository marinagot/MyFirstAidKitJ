package com.example.myfirstaidkit.jobScheduler;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

public class DailyJob extends JobService {
    private static final int JOB_ID = 1;

    public static void schedule(Context context, long intervalMillis) {

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        ComponentName componentName =
                new ComponentName(context, DailyJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        // builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(intervalMillis);
        //builder.setPersisted(true);
        jobScheduler.schedule(builder.build());

        /*JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setPeriodic(intervalMillis).build();
        jobScheduler.schedule(jobInfo);*/
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Toast.makeText(getApplicationContext(), "Alarm received!", Toast.LENGTH_LONG).show();

        // aqui cosas
        jobFinished(jobParameters, false);
        return false;
    }
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //true para reschedulearlo
        //false para dejarlo morir
        return false;
    }
}