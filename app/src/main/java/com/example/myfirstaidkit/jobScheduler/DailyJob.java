package com.example.myfirstaidkit.jobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;

import java.util.Calendar;
import java.util.List;

import static com.example.myfirstaidkit.helpers.Utils.prepareNotification;
import static com.example.myfirstaidkit.helpers.Utils.schedule;

public class DailyJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        DataBaseOperations dbo = DataBaseOperations.get_Instance(getApplicationContext());
        List<Medicine> meds = dbo.getMedicine_userId(dbo.getIdLogged());

        for(int i = 0; i < meds.size(); i++) {
            int notifType = 0;
            if (meds.get(i).getExpirationDate() <= Calendar.getInstance().getTimeInMillis()) {
                notifType = 5;
            } else if (meds.get(i).getExpirationDate() <= Calendar.getInstance().getTimeInMillis() + 432000000) {
                notifType = 4;
            } else if (meds.get(i).getDoseNumber() <= 0) {
                notifType = 3;
            } else if (meds.get(i).getDoseNumber() <= 5) {
                notifType = 2;
            }
            if (notifType != 0){
                prepareNotification(
                        getApplicationContext(),
                        i,
                        notifType,
                        meds.get(i)
                );
            }
        }

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