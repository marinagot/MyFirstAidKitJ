package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myfirstaidkit.data.BaseDatos.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.Relacion_db;
import com.example.myfirstaidkit.data.FirstAidKit.Users_db;
import com.example.myfirstaidkit.data.FirstAidKit.Medicines_db;
import com.example.myfirstaidkit.data.FirstAidKit.Treatments_db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;


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


        valores.put(Medicines_db.EXPIRATION_DATE, dateFormat.format(med.getExpiration_date()));
        valores.put(Medicines_db.DOSE_NUMBER, med.getDose_number());
        valores.put(Medicines_db.MEDICINE_NAME, med.getMedicine_name());
        valores.put(Medicines_db.MEDICINE_TYPE, med.getMedicine_type());
        valores.put(Medicines_db.ID_USER,med.getIdUser());


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
        valores.put(Treatments_db.NAME, treatment.getName());

        long idTreatment = db.insertOrThrow(Tablas.TREATMENT, null, valores);

        db.close();
        return idTreatment;
    }


    public int deleteUser(User user){
        SQLiteDatabase db= baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Users_db.ID);
        final String[] whereArgs = {user.getId().toString()};

        int deleted =  db.delete(Tablas.USER,whereClause, whereArgs);

        return deleted;
    }

    public int deleteMedicine (Medicine medicine){
        SQLiteDatabase db= baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Medicines_db.ID);
        final String[] whereArgs = {medicine.getId().toString()};

        int deleted = db.delete(Tablas.MEDICINE,whereClause, whereArgs);

        return deleted;

    }

    public int deleteTreatment ( Treatment treatment){

       SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Treatments_db.ID);
        final String[] whereArgs = {treatment.getId().toString()};

        int deleted = db.delete(Tablas.TREATMENT, whereClause, whereArgs);

        return deleted;
    }



    public Treatment get_Treatment_treatmentName(String treatmentName) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.TREATMENT, Treatments_db.NAME);

        String[] selectionArgs = {treatmentName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Treatment treatment = new Treatment();

        if (c.moveToFirst() == true) {

            treatment.setId(c.getInt(0));
            treatment.setName(c.getColumnName(1));
            treatment.setIdUser(c.getInt(2));
            treatment.setFrecuencia(c.getInt(3));

            try {
                Date fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(4));
                Date fechaFinal = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(5));
                treatment.setFechaInicio(fechaInicio);
                treatment.setFechaFinal(fechaFinal);
            } catch (ParseException e) {
                treatment.setFechaInicio(null);
                treatment.setFechaFinal(null);
            }


        }
        db.close();
        return treatment;

    }

    public User get_User_Username(String username) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.USER, Users_db.USERNAME);

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
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, Medicines_db.MEDICINE_NAME);

        String[] selectionArgs = {medicineName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Medicine medicine = new Medicine();

        if (c.moveToFirst() == true) {

            medicine.setId(c.getInt(0));
            medicine.setMedicine_name(c.getString(1));
            medicine.setIdUser(c.getInt(3));
            medicine.setMedicine_type(c.getString(4));

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

