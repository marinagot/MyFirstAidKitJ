package com.example.myfirstaidkit.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.example.myfirstaidkit.data.FirstAidKit.Kit;
import com.example.myfirstaidkit.data.FirstAidKit.Medicine;
import com.example.myfirstaidkit.data.FirstAidKit.User;
import com.example.myfirstaidkit.data.FirstAidKit.Treatment;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */

public class BaseDatos extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "FirstAidKit.db";
    private static final int VERSION_ACTUAL = 1;
    private final Context contexto;

    interface Tablas {
        String USER = "user";
        String MEDICINE = "medicine";
        String TREATMENT = "treatment";
        String KIT = "kit";
    }

    interface Referencias {
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, User.ID);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, Medicine.ID);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, Treatment.ID);
        String ID_KIT = String.format(" REFERENCES %s(%s)", Tablas.KIT, Kit.ID);

    }

    public BaseDatos (Context contexto){
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
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
                "%s INTEGER UNIQUE NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tablas.USER, BaseColumns._ID, User.ID, User.USERNAME, User.EMAIL,
                User.PASSWORD, User.BIRTHDAY, User.AVATAR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                "%s TEXT NOT NULL %s)",
                Tablas.TREATMENT, BaseColumns._ID, Treatment.ID, Treatment.IDUSER,
                Treatment.FRECUENCIA, Treatment.FECHAINICIO,Treatment.FECHAFINAL));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                "%s TEXT NOT NULL %s)",
                Tablas.MEDICINE, BaseColumns._ID, Medicine.ID, Medicine.MEDICINE_NAME,
                Medicine.IDKIT, Medicine.MEDICINE_TYPE, Medicine.EXPIRATION_DATE,
                Medicine.DOSE_NUMBER));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL,%s TEXT NOT NULL %s," +
                "%s TEXT NOT NULL %s)",
                Tablas.KIT, BaseColumns._ID, Kit.ID, Kit.ID_TRAT, Kit.ID_MED));

    }

    @Override

    public void onUpgrade (SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USER);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.KIT);

        onCreate(db);

    }
}
