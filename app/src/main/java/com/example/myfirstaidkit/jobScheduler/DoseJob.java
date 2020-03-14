package com.example.myfirstaidkit.jobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.TakeHours;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.myfirstaidkit.helpers.Utils.fireNotification;
import static com.example.myfirstaidkit.helpers.Utils.getNextDose;
import static com.example.myfirstaidkit.helpers.Utils.prepareDoseNotification;
import static com.example.myfirstaidkit.helpers.Utils.prepareNotification;
import static com.example.myfirstaidkit.helpers.Utils.scheduleDose;

public class DoseJob extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        DataBaseOperations dbo = DataBaseOperations.get_Instance(getApplicationContext());

        PersistableBundle bundle = jobParameters.getExtras();

        String id = jobParameters.getExtras().getString("rel_id");
        String medicineId = jobParameters.getExtras().getString("rel_med_id");
        String treatmentId = jobParameters.getExtras().getString("rel_treat_id");
        Long endDate = jobParameters.getExtras().getLong("rel_end_date");

        Medicine med = dbo.getMedicine_medicineId(medicineId);
        Treatment treatment = dbo.getTreatment_treatmentId(treatmentId);

        if(Calendar.getInstance().getTimeInMillis() < endDate)
            prepareDoseNotification(
                getApplicationContext(),
                id.hashCode(),
                med,
                treatment
            );

        // aqui cosas
        jobFinished(jobParameters, true);
        scheduleDose(getApplicationContext(), bundle);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //true para reschedulearlo
        //false para dejarlo morir
        return false;
    }
}