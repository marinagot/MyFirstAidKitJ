package com.example.myfirstaidkit.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfirstaidkit.data.FirstAidKit.MedicinesDb;
import com.example.myfirstaidkit.data.FirstAidKit.MedTretRelDb;
import com.example.myfirstaidkit.data.FirstAidKit.TreatmentsDb;
import com.example.myfirstaidkit.data.FirstAidKit.UsersDb;
import com.example.myfirstaidkit.data.FirstAidKit.HoursDb;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FirstAidKit.db";
    private static final int ACTUAL_VERSION = 4;
    private final Context context;

    interface Tablas {
        String USER = "user";
        String MEDICINE = "medicine";
        String TREATMENT = "treatment";
        String RELATION_MED_TREATMENT = "relation";
        String TAKE_HOURS = "hours";
    }

    interface Referencias {
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, UsersDb.ID);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, MedicinesDb.ID);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, TreatmentsDb.ID);
        String ID_RELATION = String.format(" REFERENCES %s(%s)", Tablas.RELATION_MED_TREATMENT, MedTretRelDb.ID);

    }

    DataBase(Context context){
        super(context, DATABASE_NAME, null, ACTUAL_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s INTEGER NOT NULL, %s TEXT NOT NULL);",
                Tablas.TREATMENT, TreatmentsDb.ID, TreatmentsDb.ID_USER, TreatmentsDb.NAME));

        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s TEXT NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL," +
                "%s TIMESTAMP NOT NULL, %s INTEGER NOT NULL);",
                Tablas.MEDICINE, MedicinesDb.ID, MedicinesDb.NAME,
                MedicinesDb.ID_USER, MedicinesDb.TYPE, MedicinesDb.EXPIRATION_DATE,
                MedicinesDb.DOSE_NUMBER));

        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER, %s TIMESTAMP NOT NULL," +
                "%s TIMESTAMP NOT NULL);",
                Tablas.RELATION_MED_TREATMENT, MedTretRelDb.ID, MedTretRelDb.ID_TRAT,
                MedTretRelDb.ID_MED, MedTretRelDb.FREQUENCY, MedTretRelDb.INITIAL_DATE,
                MedTretRelDb.FINAL_DATE));

        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                        "%s INTEGER NOT NULL, %s TIMESTAMP NOT NULL);",
                Tablas.TAKE_HOURS, HoursDb.ID, HoursDb.ID_REL, HoursDb.HOUR));
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USER);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RELATION_MED_TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TAKE_HOURS);

        onCreate(db);

        SharedPreferences prefs = context.getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("sync_id", "0");
        edit.apply();

    }
}
