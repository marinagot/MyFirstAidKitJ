package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfirstaidkit.data.DataBase.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.MedicinesDb;
import com.example.myfirstaidkit.data.FirstAidKit.TreatmentsDb;
import com.example.myfirstaidkit.data.FirstAidKit.UsersDb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Clase auxiliar que implementa a {@link DataBase} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */

public final class DataBaseOperations {

    private static DataBase DataBase;

    public static  DataBaseOperations instance = new  DataBaseOperations();

    public  DataBaseOperations() {
    }

    public static  DataBaseOperations get_Instance(Context context) {
        if (DataBase == null) {
            DataBase = new DataBase(context);
        }
        return instance;
    }

    /* Aqui se hacen los CRUD : metodos Create, Read, Update y Delete de la base de datos */
    /* Es decir, se redactan aqui todas las interacciones con la base de datos*/

    /* Por ejemplo, obtener la informacion de un medicamento, dado su id*/

    public boolean loginData(String username, String password) {
        SQLiteDatabase db = DataBase.getReadableDatabase();
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

    public long insertUser(User user){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(UsersDb.PASSWORD,user.getPassword());
        values.put(UsersDb.USERNAME,user.getUsername());
        values.put(UsersDb.AVATAR,user.getAvatar());
        values.put(UsersDb.BIRTHDAY, user.getBirthday());
        values.put(UsersDb.EMAIL, user.getEmail());

        long idUser=db.insertOrThrow(Tablas.USER,null,values);
        db.close();

        return idUser;
    }

    public long insertMedicine(Medicine med){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        values.put(MedicinesDb.EXPIRATION_DATE, dateFormat.format(med.getExpiration_date()));
        values.put(MedicinesDb.DOSE_NUMBER, med.getDose_number());
        values.put(MedicinesDb.NAME, med.getName());
        values.put(MedicinesDb.TYPE, med.getType());
        values.put(MedicinesDb.ID_USER,med.getIdUser());


        long idMed = db.insertOrThrow(Tablas.MEDICINE, null,values);
        db.close();

        return idMed;
    }

    public long insertTreatment(Treatment treatment){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        values.put(TreatmentsDb.ID_USER, treatment.getIdUser());
        values.put(TreatmentsDb.NAME, treatment.getName());

        long idTreatment = db.insertOrThrow(Tablas.TREATMENT, null, values);

        db.close();
        return idTreatment;
    }


    public int deleteUser(User user){
        SQLiteDatabase db= DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", UsersDb.ID);
        final String[] whereArgs = {user.getId().toString()};

        int deleted =  db.delete(Tablas.USER,whereClause, whereArgs);

        return deleted;
    }

    public int deleteMedicine (Medicine medicine){
        SQLiteDatabase db= DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", MedicinesDb.ID);
        final String[] whereArgs = {medicine.getId().toString()};

        int deleted = db.delete(Tablas.MEDICINE,whereClause, whereArgs);

        return deleted;

    }

    public int deleteTreatment ( Treatment treatment){

       SQLiteDatabase db = DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", TreatmentsDb.ID);
        final String[] whereArgs = {treatment.getId().toString()};

        int deleted = db.delete(Tablas.TREATMENT, whereClause, whereArgs);

        return deleted;
    }



    public Treatment get_Treatment_treatmentName(String treatmentName) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.TREATMENT, TreatmentsDb.NAME);

        String[] selectionArgs = {treatmentName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Treatment treatment = new Treatment();

        if (c.moveToFirst() == true) {

            treatment.setId(c.getInt(0));
            treatment.setName(c.getColumnName(1));
            treatment.setIdUser(c.getInt(2));



        }
        db.close();
        return treatment;

    }

    public User get_User_Username(String username) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.USER, UsersDb.USERNAME);

        String[] selectionArgs = {username};
        Cursor c = db.rawQuery(sql, selectionArgs);

        User user = new User();

        if (c.moveToFirst() == true) {

            user.setId(c.getInt(0));
            user.setUsername(c.getString(1));
            user.setEmail(c.getString(2));
            user.setPassword(c.getString(3));
            user.setBirthday(c.getString(4));
        }
        db.close();
        return user;

    }

    public Medicine get_Medicine_medicineName(String medicineName) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, MedicinesDb.NAME);

        String[] selectionArgs = {medicineName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Medicine medicine = new Medicine();

        if (c.moveToFirst() == true) {

            medicine.setId(c.getInt(0));
            medicine.setName(c.getString(1));
            medicine.setIdUser(c.getInt(3));
            medicine.setType(c.getString(4));

            medicine.setDose_number(c.getInt(6));


            try {
                Date expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(5));
                medicine.setExpiration_date(expirationDate);

            } catch (ParseException e) {
                medicine.setExpiration_date(null);

            }


        }
        db.close();
        return medicine;

    }


}

