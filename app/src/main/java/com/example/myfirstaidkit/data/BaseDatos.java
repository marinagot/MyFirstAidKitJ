package com.example.myfirstaidkit.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.example.myfirstaidkit.data.FirstAidKit.Relacion_db;
import com.example.myfirstaidkit.data.FirstAidKit.Medicines_db;
import com.example.myfirstaidkit.data.FirstAidKit.Users_db;
import com.example.myfirstaidkit.data.FirstAidKit.Treatments_db;

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
        String RELACION = "Relacion";
    }

    interface Referencias {
        String ID_USER = String.format("REFERENCES %s(%s) ON DELETE CASCADE", Tablas.USER, Users_db.ID);
        String ID_MEDICINE = String.format("REFERENCES %s(%s)", Tablas.MEDICINE, Medicines_db.ID);
        String ID_TREATMENT = String.format("REFERENCES %s(%s)", Tablas.TREATMENT, Treatments_db.ID);
        String ID_RELACION = String.format(" REFERENCES %s(%s)", Tablas.RELACION, Relacion_db.ID);

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
                "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL);",
                Tablas.USER, BaseColumns._ID, Users_db.USERNAME, Users_db.EMAIL,
                Users_db.PASSWORD, Users_db.BIRTHDAY, Users_db.AVATAR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL,%s TEXT NOT NULL,%s INTEGER NOT NULL,%s DATETIME NOT NULL," +
                "%s DATETIME NOT NULL);",
                Tablas.TREATMENT, BaseColumns._ID, Treatments_db.ID_USER, Treatments_db.NAME,
                Treatments_db.FRECUENCIA, Treatments_db.FECHAINICIO,Treatments_db.FECHAFINAL));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL ," +
                "%s DATETIME NOT NULL,%s INTEGER NOT NULL);",
                Tablas.MEDICINE, BaseColumns._ID, Medicines_db.MEDICINE_NAME,
                Medicines_db.ID_USER, Medicines_db.MEDICINE_TYPE, Medicines_db.EXPIRATION_DATE,
                Medicines_db.DOSE_NUMBER));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s INTEGER NOT NULL , %s INTEGER NOT NULL);",
                Tablas.RELACION, BaseColumns._ID, Relacion_db.ID_TRAT, Relacion_db.ID_MED));
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USER);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TREATMENT);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RELACION);

        onCreate(db);

    }
}
