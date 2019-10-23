package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Database;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.myfirstaidkit.data.DataBase.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.MedicinesDb;
import com.example.myfirstaidkit.data.FirstAidKit.TreatmentsDb;
import com.example.myfirstaidkit.data.FirstAidKit.UsersDb;
import com.example.myfirstaidkit.data.FirstAidKit.MedTretRelDb;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Clase auxiliar que implementa a {@link DataBase} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */

public final class DataBaseOperations {

    private static DataBase DataBase;
    private static RequestQueue queue;
    private static Context contexto;

    Gson gson = new Gson();

    private static String base_url ="http://192.168.1.46:8080";

    public static  DataBaseOperations instance = new  DataBaseOperations();

    public static  DataBaseOperations get_Instance(Context context) {
        contexto = context;
        if (DataBase == null) {
            DataBase = new DataBase(context);
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return instance;
    }


    /* Http operations */

    static JSONObject callApi(String url, int type) {
        return callApi(url, type, null);
    }

    static JSONObject callApi(String url, int type, JSONObject body) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (type, base_url + url, body, future, future);

        // Add the request to the RequestQueue.
        if (queue != null) {
            queue.add(jsonObjectRequest);
        }

        try {
            JSONObject response = future.get(5, TimeUnit.SECONDS); // this will block
            return response;
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            int i = 0;
            // exception handling
        } catch (TimeoutException e) {
            // exception handling
        }
        return null;
    }

    /* Aqui se hacen los CRUD : metodos Create, Read, Update y Delete de la base de datos */
    /* Es decir, se redactan aqui todas las interacciones con la base de datos*/

    /* Por ejemplo, obtener la informacion de un medicamento, dado su id*/

    public User loginData(String username, String password) {
        // Primero llama al back
        final JSONObject data = new JSONObject();

        try {
            data.put("email", username);
            data.put("password", password);
            //Falla
            String o = callApi("/users/login", Request.Method.POST, data).toString();
            return gson.fromJson(o, User.class);
        } catch (Exception e) {
            int i = 0;
            Log.e("", "error: ", e);
        }

        return null;
    }

    public String insertUser(User user){
        /*SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(UsersDb.PASSWORD,user.getPassword());
        values.put(UsersDb.USERNAME,user.getUsername());
        values.put(UsersDb.AVATAR,user.getAvatar());
        values.put(UsersDb.BIRTHDAY, user.getBirthday());
        values.put(UsersDb.EMAIL, user.getEmail());

        long idUser=db.insertOrThrow(Tablas.USER,null,values);
        db.close();

        return idUser;*/
        return null;
    }

    public String insertMedicine(Medicine med){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(MedicinesDb.EXPIRATION_DATE, dateFormat.format(med.getExpirationDate()));
        values.put(MedicinesDb.DOSE_NUMBER, med.getDoseNumber());
        values.put(MedicinesDb.NAME, med.getName());
        values.put(MedicinesDb.TYPE, med.getType());
        values.put(MedicinesDb.ID_USER,med.getIdUser());


        String idMed = String.valueOf(db.insertOrThrow(Tablas.MEDICINE, null,values));
        db.close();

        return idMed;
    }

    public String insertRelation(MedTretRel relation){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(MedTretRelDb.ID_MED, relation.getIdMedicine());
        values.put(MedTretRelDb.ID_TRAT, relation.getIdTreatment());
        values.put(MedTretRelDb.FREQUENCY, relation.getFrequency());
        values.put(MedTretRelDb.INITIAL_DATE, dateFormat.format(relation.getInitialDate()));
        values.put(MedTretRelDb.FINAL_DATE, dateFormat.format(relation.getFinalDate()));


        String idRel = String.valueOf(db.insertOrThrow(Tablas.RELATION_MED_TREATMENT, null, values));
        db.close();

        return idRel;
    }

    public String insertTreatment(Treatment treatment){
        SQLiteDatabase db= DataBase.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TreatmentsDb.ID_USER, treatment.getIdUser());
        values.put(TreatmentsDb.NAME, treatment.getName());

        String idTreatment = String.valueOf(db.insertOrThrow(Tablas.TREATMENT, null, values));

        db.close();

        return idTreatment;
    }

    public int deleteUser(String password){
        SQLiteDatabase db = DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", UsersDb.PASSWORD);
        final String[] whereArgs = {String.valueOf(password)};

        int deleted = db.delete(Tablas.USER,whereClause, whereArgs);
        db.close();
        return  deleted;
    }

    public int deleteMedicine (Medicine medicine){
        SQLiteDatabase db= DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", MedicinesDb.ID);
        final String[] whereArgs = {String.valueOf(medicine.getId())};

        int deleted = db.delete(Tablas.MEDICINE,whereClause, whereArgs);
        db.close();
        return  deleted;
    }

    public int deleteTreatment ( Treatment treatment){

       SQLiteDatabase db = DataBase.getWritableDatabase();

        String whereClause = String.format("%s=?", TreatmentsDb.ID);
        final String[] whereArgs = {String.valueOf(treatment.getId())};

        int deleted = db.delete(Tablas.TREATMENT, whereClause, whereArgs);
        db.close();
        return  deleted;
    }

    public Treatment getTreatment_treatmentName(String treatmentName) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.TREATMENT, TreatmentsDb.NAME);

        String[] selectionArgs = {treatmentName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Treatment treatment = new Treatment();

        if (c.moveToFirst()) {

            treatment.setId(c.getString(0));
            treatment.setName(c.getColumnName(1));
            treatment.setIdUser(c.getString(2));
        }
        c.close();
        db.close();
        return treatment;

    }

    public List<Treatment> getTreatment_userId(String userId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.TREATMENT, TreatmentsDb.ID_USER);

        String[] selectionArgs = {userId};
        Cursor c = db.rawQuery(sql, selectionArgs);

        List<Treatment> treatments = new ArrayList<>();

        while (c.moveToNext()) {
            Treatment treatment = new Treatment();

            treatment.setId(c.getString(0));
            treatment.setIdUser(c.getString(1));
            treatment.setName(c.getString(2));

            treatments.add(treatment);

        }
        c.close();
        db.close();
        return treatments;

    }

    public User getUser_Email(String email) {
        final JSONObject data = new JSONObject();

        try {
            data.put("email", email);
            User response = gson.fromJson(callApi("/user/?email=" + email, Request.Method.GET).toString(), User.class);
            return response;
        } catch (Exception e) {}

        return null;
    }

    public Medicine getMedicine_medicineName(String medicineName) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, MedicinesDb.NAME);

        String[] selectionArgs = {medicineName};
        Cursor c = db.rawQuery(sql, selectionArgs);

        Medicine medicine = new Medicine();

        if (c.moveToFirst()) {

            medicine.setId(c.getString(0));
            medicine.setName(c.getString(1));
            medicine.setIdUser(c.getString(2));
            medicine.setType(c.getString(3));

            medicine.setDoseNumber(c.getInt(5));


            try {
                Date expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(4));
                medicine.setExpirationDate(expirationDate);

            } catch (ParseException e) {
                medicine.setExpirationDate(null);

            }
        }
        c.close();
        db.close();
        return medicine;
    }

    public Medicine getMedicine_medicineId(String medicineId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, "_id");
        String param = medicineId;
        String[] selectionArgs = {param};

        Cursor c = db.rawQuery(sql, selectionArgs);

        Medicine medicine = new Medicine();

        if (c.moveToFirst()) {

            medicine.setId(c.getString(0));
            medicine.setName(c.getString(1));
            medicine.setIdUser(c.getString(2));
            medicine.setType(c.getString(3));

            medicine.setDoseNumber(c.getInt(5));


            try {
                Date expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(4));
                medicine.setExpirationDate(expirationDate);

            } catch (ParseException e) {
                medicine.setExpirationDate(null);

            }
        }
        c.close();
        db.close();
        return medicine;
    }

    public List<Medicine> getMedicine_userId(String userId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, MedicinesDb.ID_USER);

        String[] selectionArgs = {String.valueOf(userId)};
        Cursor c = db.rawQuery(sql, selectionArgs);

        List<Medicine> medicines = new ArrayList<>();

        while (c.moveToNext()) {
            Medicine medicine = new Medicine();

            medicine.setId(c.getString(0));
            medicine.setName(c.getString(1));
            medicine.setIdUser(c.getString(2));
            medicine.setType(c.getString(3));

            medicine.setDoseNumber(c.getInt(5));


            try {
                Date expirationDate = new SimpleDateFormat("dd/MM/yyyy").parse(c.getString(4));
                medicine.setExpirationDate(expirationDate);

            } catch (ParseException e) {
                medicine.setExpirationDate(null);

            }
            medicines.add(medicine);

        }
        c.close();
        db.close();
        return medicines;

    }

    public List<MedTretRel> getRelations_treatmentId(String treatmentId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.RELATION_MED_TREATMENT, MedTretRelDb.ID_TRAT);

        String[] selectionArgs = {String.valueOf(treatmentId)};
        Log.d("Valor", String.valueOf(selectionArgs));
        Cursor c = db.rawQuery(sql, selectionArgs);

        List<MedTretRel> relations = new ArrayList<>();

        while (c.moveToNext()) {
            MedTretRel relation = new MedTretRel();

            relation.setIdTreatment(c.getString(1));
            relation.setIdMedicine(c.getString(2));
            relation.setFrequency(c.getInt(3));

            try {
                Date initialDate = new SimpleDateFormat("yyyy-MM-dd HH:ms:ss").parse(c.getString(4));
                relation.setInitialDate(initialDate);
            } catch (ParseException e) {
                relation.setInitialDate(null);
            }
            try {
                Date finalDate = new SimpleDateFormat("yyyy-MM-dd HH:ms:ss").parse(c.getString(5));
                relation.setFinalDate(finalDate);
            } catch (ParseException e) {
                relation.setFinalDate(null);
            }

            relations.add(relation);

        }
        c.close();
        db.close();
        return relations;
    }

    public int updateUserPassword(String old_password, String new_password){
        SQLiteDatabase db = DataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersDb.PASSWORD, new_password);

        String whereClause = String.format("%s=?", UsersDb.PASSWORD);
        final String[] whereArgs = {old_password};

        int updated = db.update(Tablas.USER, values, whereClause, whereArgs);
        db.close();
        return updated;

    }

    public boolean userIsLogged(SharedPreferences prefs) {
        return prefs.getString("username", null) != null;
    }

    public String getUserLogged (SharedPreferences prefs) {
        return prefs.getString("username", null);
    }
}

