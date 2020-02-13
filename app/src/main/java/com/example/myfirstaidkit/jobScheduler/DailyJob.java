package com.example.myfirstaidkit.jobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;

import java.util.Calendar;

import static com.example.myfirstaidkit.helpers.Utils.fireNotification;
import static com.example.myfirstaidkit.helpers.Utils.schedule;

public class DailyJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        fireNotification(
                getApplicationContext(),
                2,
                new Medicine("1", "Primperán", "Me", "Doses", 3, Calendar.getInstance().getTimeInMillis()),
                new Treatment("2", "3", "Jaquecas")
        );

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