package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myfirstaidkit.data.BaseDatos.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.Kits_db;
import com.example.myfirstaidkit.data.FirstAidKit.Users_db;
import com.example.myfirstaidkit.data.FirstAidKit.Medicines_db;
import com.example.myfirstaidkit.data.FirstAidKit.Treatments_db;

import java.text.SimpleDateFormat;


/**
 * Clase auxiliar que implementa a {@link BaseDatos} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */

public final class OperacionesBaseDatos {

    private static BaseDatos baseDatos;

    public static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    public OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatos(contexto);
        }
        return instancia;
    }

    /* Aqui se hacen los CRUD : metodos Create, Read, Update y Delete de la base de datos */
    /* Es decir, se redactan aqui todas las interacciones con la base de datos*/

    /* Por ejemplo, obtener la informacion de un medicamento, dado su id*/

    public boolean loginData(String username, String password) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        boolean exist = false;
        Cursor c = db.rawQuery("SELECT USERNAME,PASSWORD FROM " +
                Tablas.USER + " WHERE " + "USERNAME='" +
                username + "' AND PASSWORD='" + password + "'", null);
        if (c.moveToFirst() == true) {

            String un = c.getString(0);
            String pw = c.getString(1);

            if (username.equals(un) && password.equals(pw)) {
                exist = true;
            }
        }
        db.close();
        return exist;
    }

    public long insertarUser(User user){
        SQLiteDatabase db= baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();


        valores.put(Users_db.PASSWORD,user.getPassword());
        valores.put(Users_db.USERNAME,user.getUsername());
        valores.put(Users_db.AVATAR,user.getAvatar());
        valores.put(Users_db.BIRTHDAY, user.getBirthday());
        valores.put(Users_db.EMAIL, user.getEmail());

        long idUser=db.insertOrThrow(Tablas.USER,null,valores);
        db.close();

        return idUser;
    }

    public long insertarMedicina(Medicine med){
        SQLiteDatabase db= baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        valores.put(Medicines_db.ID_KIT, med.getIdKit());
        valores.put(Medicines_db.EXPIRATION_DATE, dateFormat.format(med.getExpiration_date()));
        valores.put(Medicines_db.DOSE_NUMBER, med.getDose_number());
        valores.put(Medicines_db.MEDICINE_NAME, med.getMedicine_name());
        valores.put(Medicines_db.MEDICINE_TYPE, med.getMedicine_type());

        long idMed = db.insertOrThrow(Tablas.MEDICINE, null,valores);
        db.close();

        return idMed;
    }

    public long insertarTreatment(Treatment treatment){
        SQLiteDatabase db= baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        valores.put(Treatments_db.FRECUENCIA, treatment.getFrecuencia());
        valores.put(Treatments_db.FECHAFINAL,dateFormat.format(treatment.getFechaFinal()));
        valores.put(Treatments_db.FECHAINICIO, dateFormat.format(treatment.getFechaInicio()));
        valores.put(Treatments_db.ID_USER, treatment.getIdUser());

        long idTreatment = db.insertOrThrow(Tablas.TREATMENT, null, valores);

        db.close();
        return idTreatment;
    }


    public int deleteUser(User user){
        SQLiteDatabase db= baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Users_db.USERNAME);
        final String[] whereArgs = {user.getId().toString()};

        int deleted =  db.delete(Tablas.USER,whereClause, whereArgs);

        return deleted;
    }

    public int deleteMedicine (Medicine medicine){
        SQLiteDatabase db= baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Medicines_db.ID);
        final String[] whereArgs = {medicine.};

        int deleted = db.delete(Tablas.USER,whereClause, whereArgs);

        return deleted;

    }

    public Cursor obtenerDetallesPorIdMedicina(String idMedicine) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, Kits_db.ID_MED);

        String[] selectionArgs = {idMedicine};

        return db.rawQuery(sql, selectionArgs);

    }




}

