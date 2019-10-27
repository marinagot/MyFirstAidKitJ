package com.example.myfirstaidkit.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.example.myfirstaidkit.data.FirstAidKit.MedicinesDb;
import com.example.myfirstaidkit.data.FirstAidKit.MedTretRelDb;
import com.example.myfirstaidkit.data.FirstAidKit.TreatmentsDb;
import com.example.myfirstaidkit.data.FirstAidKit.UsersDb;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FirstAidKit.db";
    private static final int ACTUAL_VERSION = 1;
    private final Context context;

    interface Tablas {
        String USER = "user";
        String MEDICINE = "medicine";
        String TREATMENT = "treatment";
        String RELATION_MED_TREATMENT = "relation";
    }

    interface Referencias {
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, UsersDb.ID);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, MedicinesDb.ID);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, TreatmentsDb.ID);
        String ID_RELATION = String.format(" REFERENCES %s(%s)", Tablas.RELATION_MED_TREATMENT, MedTretRelDb.ID);

    }

    public DataBase(Context context){
        super(context, DATABASE_NAME, null, ACTUAL_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){


        /*db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s TEXT NOT NULL,%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL,",
                Tablas.USER, UsersDb.ID, UsersDb.USERNAME, UsersDb.EMAIL,
                UsersDb.PASSWORD));*/

        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s INTEGER NOT NULL,%s TEXT NOT NULL);",
                Tablas.TREATMENT, UsersDb.ID, TreatmentsDb.ID_USER, TreatmentsDb.NAME));

        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s TEXT NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL ," +
                "%s TIMESTAMP NOT NULL,%s INTEGER NOT NULL);",
                Tablas.MEDICINE, UsersDb.ID, MedicinesDb.NAME,
                MedicinesDb.ID_USER, MedicinesDb.TYPE, MedicinesDb.EXPIRATION_DATE,
                MedicinesDb.DOSE_NUMBER));
        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                "%s INTEGER NOT NULL , %s INTEGER NOT NULL,%s INTEGER NOT NULL,%s TIMESTAMP NOT NULL," +
                "%s TIMESTAMP NOT NULL);",
                Tablas.RELATION_MED_TREATMENT, UsersDb.ID, MedTretRelDb.ID_TRAT,
                MedTretRelDb.ID_MED, MedTretRelDb.FREQUENCY, MedTretRelDb.INITIAL_DATE,
                MedTretRelDb.FINAL_DATE));

        /*db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL," +
                "%s TEXT NOT NULL,%s TEXT NOT NULL);",
                Tablas.USER, BaseColumns._ID, UsersDb.USERNAME, UsersDb.EMAIL,
                UsersDb.PASSWORD, UsersDb.BIRTHDAY, UsersDb.AVATAR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL,%s TEXT NOT NULL);",
                Tablas.TREATMENT, BaseColumns._ID, TreatmentsDb.ID_USER, TreatmentsDb.NAME));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL ," +
                "%s DATETIME NOT NULL,%s INTEGER NOT NULL);",
                Tablas.MEDICINE, BaseColumns._ID, MedicinesDb.NAME,
                MedicinesDb.ID_USER, MedicinesDb.TYPE, MedicinesDb.EXPIRATION_DATE,
                MedicinesDb.DOSE_NUMBER));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL , %s INTEGER NOT NULL,%s INTEGER NOT NULL,%s DATETIME NOT NULL," +
                "%s DATETIME NOT NULL);",
                Tablas.RELATION_MED_TREATMENT, BaseColumns._ID, MedTretRelDb.ID_TRAT,
                MedTretRelDb.ID_MED, MedTretRelDb.FREQUENCY, MedTretRelDb.INITIAL_DATE,
                MedTretRelDb.FINAL_DATE));*/
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USER);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RELATION_MED_TREATMENT);

        onCreate(db);

    }
}
