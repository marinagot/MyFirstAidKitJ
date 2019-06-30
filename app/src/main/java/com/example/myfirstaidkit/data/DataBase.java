package com.example.myfirstaidkit.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.example.myfirstaidkit.data.FirstAidKit.Medicines_db;
import com.example.myfirstaidkit.data.FirstAidKit.Med_Tret_Rel_db;
import com.example.myfirstaidkit.data.FirstAidKit.Treatments_db;
import com.example.myfirstaidkit.data.FirstAidKit.Users_db;

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
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, Users_db.ID);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, Medicines_db.ID);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, Treatments_db.ID);
        String ID_RELATION = String.format(" REFERENCES %s(%s)", Tablas.RELATION_MED_TREATMENT, Med_Tret_Rel_db.ID);

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

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL);",
                Tablas.USER, BaseColumns._ID, Users_db.USERNAME, Users_db.EMAIL,
                Users_db.PASSWORD, Users_db.BIRTHDAY, Users_db.AVATAR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL,%s TEXT NOT NULL,%s INTEGER NOT NULL,%s DATETIME NOT NULL," +
                "%s DATETIME NOT NULL);",
                Tablas.TREATMENT, BaseColumns._ID, Treatments_db.ID_USER, Treatments_db.NAME,
                Treatments_db.FREQUENCY, Treatments_db.INITIALDATE,Treatments_db.FINALDATE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL ," +
                "%s DATETIME NOT NULL,%s INTEGER NOT NULL);",
                Tablas.MEDICINE, BaseColumns._ID, Medicines_db.NAME,
                Medicines_db.ID_USER, Medicines_db.TYPE, Medicines_db.EXPIRATION_DATE,
                Medicines_db.DOSE_NUMBER));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL , %s INTEGER NOT NULL);",
                Tablas.RELATION_MED_TREATMENT, BaseColumns._ID, Med_Tret_Rel_db.ID_TRAT, Med_Tret_Rel_db.ID_MED));
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
