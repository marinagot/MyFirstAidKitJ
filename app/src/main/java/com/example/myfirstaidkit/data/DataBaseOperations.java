package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.myfirstaidkit.data.DataBase.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.MedicinesDb;
import com.example.myfirstaidkit.data.FirstAidKit.TreatmentsDb;
import com.example.myfirstaidkit.data.FirstAidKit.MedTretRelDb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private static SharedPreferences prefs;

    Gson gson = new Gson();

    private static String base_url ="http://192.168.1.36:3000";
    /*private static String base_url ="http://jdserver.ddns.net:3000";*/


    public static  DataBaseOperations instance = new  DataBaseOperations();

    public static  DataBaseOperations get_Instance(Context context) {
        prefs = context.getSharedPreferences("UserLogged", Context.MODE_PRIVATE);
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
            JSONObject response = future.get(10, TimeUnit.SECONDS); // this will block
            return response;
        } catch (InterruptedException e) {
            int i = 0;
            // exception handling
        } catch (ExecutionException e) {
            int i = 0;
            // exception handling
        } catch (TimeoutException e) {
            int i = 0;
            // exception handling
        }
        return null;
    }

    /* Http operations */

    /* SYNC operations */


    public boolean ping() {

        if(callApi("/", Request.Method.GET) == null)
            return false;
        return true;

        /*String status = "active";
        switch (status){
            case "active":
                status = "secondactive";
                return true;
            case "secondactive":
                status = "inactive";
                return true;
            case "inactive":
                status = "active";
                return false;
            default:
                return false;
        }*/
    }

    public String syncDabtabase(String id, String syncId) {
        // Llamada a la api
        JSONObject response = callApi("/sync/" + id + "?sync_id=" + syncId, Request.Method.GET);

        // Si va bien
        if (response != null) {
            String syncIdResp = null;
            JSONArray medicines = null;
            JSONArray treatments = null;
            JSONArray relations = null;
            try {
                syncIdResp = response.getString("sync_id");
            } catch (JSONException e) { }

            // Comprueba que el SyncId sea distinto
            // Si lo es, significa que hay discrepancias en las bases de datos
            if (!syncId.equals(syncIdResp)) {
                try {
                    medicines = response.getJSONArray("medicines");
                    treatments = response.getJSONArray("treatments");
                    relations = response.getJSONArray("relations");

                } catch (JSONException e) { }

                Type medicineType = new TypeToken<List<Medicine>>(){}.getType();
                List<Medicine> medicineList = gson.fromJson(medicines.toString(), medicineType);
                Type treatmentType = new TypeToken<List<Treatment>>(){}.getType();
                List<Treatment> treatmentList = gson.fromJson(treatments.toString(), treatmentType);
                Type relationType = new TypeToken<List<MedTretRel>>(){}.getType();
                List<MedTretRel> relationList = gson.fromJson(relations.toString(), relationType);

                // Actualiza las tablas
                syncTables(medicineList, treatmentList, relationList);
            }
            return syncIdResp;
        }
        return null;
    }

    private void syncTables(List<Medicine> medicineList, List<Treatment> treatmentList, List<MedTretRel> relationList ) {
        SQLiteDatabase db = DataBase.getWritableDatabase();

        // Medicinas
        db.delete(Tablas.MEDICINE, null, null);
        for (Medicine medicine: medicineList) {

            ContentValues values = new ContentValues();
            values.put(MedicinesDb.ID, medicine.getId());
            values.put(MedicinesDb.ID_USER, medicine.getIdUser());
            values.put(MedicinesDb.DOSE_NUMBER, medicine.getDoseNumber());
            values.put(MedicinesDb.EXPIRATION_DATE, medicine.getExpirationDate());
            values.put(MedicinesDb.NAME, medicine.getName());
            values.put(MedicinesDb.TYPE, medicine.getType());

            String whereClause = String.format("%s=?", MedicinesDb.ID);
            final String[] whereArgs = { medicine.getId() };

            if (db.insertOrThrow(Tablas.MEDICINE, null, values) == 0) {
                values.remove(MedicinesDb.ID);
                db.update(Tablas.MEDICINE, values, whereClause, whereArgs);
            }
        }
        db.execSQL("delete from "+ Tablas.TREATMENT);
        // Tratamientos
        for (Treatment treatment: treatmentList) {

            ContentValues values = new ContentValues();
            values.put(TreatmentsDb.ID, treatment.getId());
            values.put(TreatmentsDb.ID_USER, treatment.getIdUser());
            values.put(TreatmentsDb.NAME, treatment.getName());

            String whereClause = String.format("%s=?", TreatmentsDb.ID);
            final String[] whereArgs = { treatment.getId() };

            if (db.insertOrThrow(Tablas.TREATMENT, null, values) == 0) {
                values.remove(TreatmentsDb.ID);
                db.update(Tablas.TREATMENT, values, whereClause, whereArgs);
            }
        }
        db.execSQL("delete from "+ Tablas.RELATION_MED_TREATMENT);
        // Relaciones
        for (MedTretRel relation: relationList) {

            ContentValues values = new ContentValues();
            values.put(MedTretRelDb.ID, relation.getId());
            values.put(MedTretRelDb.ID_MED, relation.getIdMedicine());
            values.put(MedTretRelDb.ID_TRAT, relation.getIdTreatment());
            values.put(MedTretRelDb.FREQUENCY, relation.getFrequency());
            values.put(MedTretRelDb.INITIAL_DATE, relation.getInitialDate());
            values.put(MedTretRelDb.FINAL_DATE, relation.getFinalDate());

            String whereClause = String.format("%s=?", MedTretRelDb.ID);
            final String[] whereArgs = { relation.getId() };

            if (db.insertOrThrow(Tablas.RELATION_MED_TREATMENT, null, values) == 0) {
                values.remove(MedTretRelDb.ID);
                db.update(Tablas.RELATION_MED_TREATMENT, values, whereClause, whereArgs);
            }
        }
    }

    public void setSyncIdLogged(String syncId) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("sync_id", syncId);
        edit.apply();
    }

    public void resetSyncId(JSONObject res) {
        String syncId = "";
                try {
            syncId = res.get("sync_id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (syncId != null)
            setSyncIdLogged(syncId);
    }
    /* SYNC operations */

    /* USER operations */

    public User loginData(String username, String password) {
        // Primero llama al back
        final JSONObject data = new JSONObject();

        try {
            data.put("email", username);
            data.put("password", password);
            String response = callApi("/users/login", Request.Method.POST, data).toString();
            User user = gson.fromJson(response, User.class);
            return user;
        } catch (Exception e) {}

        return null;
    }

    public User insertUser(User user){
        JSONObject data = null;

        try {
            data = new JSONObject(gson.toJson(user));
        } catch (Exception e) {}

        String response = callApi("/users/register", Request.Method.POST, data).toString();
        if (response != null)
            return gson.fromJson(response, User.class);
        return null;
    }

    public User getUser_Email(String email) {
        try {
            return gson.fromJson(callApi("/user/?email=" + email, Request.Method.GET).toString(), User.class);
        } catch (Exception e) {}

        return null;
    }

    public String updateUserPassword(String email, String old_password, String new_password){
        final JSONObject data = new JSONObject();

        try {
            data.put("email", email);
            data.put("old_password", old_password);
            data.put("new_password", new_password);
            return callApi("/users/password", Request.Method.POST, data).getString("_id");
        } catch (Exception e) {}

        return null;
    }

    public boolean userIsLogged() {
        return prefs.getString("username", null) != null;
    }

    public String getUsernameLogged() {
        return prefs.getString("username", null);
    }

    public String getIdLogged() {
        return prefs.getString("id", null);
    }

    public String getEmailLogged () {
        return prefs.getString("email", null);
    }

    public String getSyncIdLogged() {
        return prefs.getString("sync_id", null);
    }

    public boolean deleteUser(String id, String password){
        final JSONObject data = new JSONObject();

        try {
            data.put("password", password);
            JSONObject res = callApi("/users/" + id, Request.Method.PUT, data);
            if (res != null)
                return true;
        } catch (Exception e) { }

        return false;
    }

    /* USER operations */

    /* MEDICINE operations */

    public String insertMedicine(Medicine med){

        JSONObject data = null;
        try {
            data = new JSONObject(gson.toJson(med));
        } catch (JSONException e) { }

        JSONObject res = callApi("/medicines/new", Request.Method.POST, data);

        if (res != null) {
            resetSyncId(res);

            try {
                med.setId(res.getString("_id"));
            } catch (JSONException e) { }

            SQLiteDatabase db = DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedicinesDb.ID, med.getId());
            values.put(MedicinesDb.EXPIRATION_DATE, med.getExpirationDate());
            values.put(MedicinesDb.DOSE_NUMBER, med.getDoseNumber());
            values.put(MedicinesDb.NAME, med.getName());
            values.put(MedicinesDb.TYPE, med.getType());
            values.put(MedicinesDb.ID_USER, med.getIdUser());

            db.insertOrThrow(Tablas.MEDICINE, null, values);
            db.close();

            return med.getId();
        }

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
            medicine.setExpirationDate(c.getLong(4));
            medicine.setDoseNumber(c.getInt(5));
        }
        c.close();
        db.close();
        return medicine;
    }

    public Medicine getMedicine_medicineId(String medicineId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, "id");
        String param = medicineId;
        String[] selectionArgs = {param};

        Cursor c = db.rawQuery(sql, selectionArgs);

        Medicine medicine = new Medicine();

        if (c.moveToFirst()) {

            medicine.setId(c.getString(0));
            medicine.setName(c.getString(1));
            medicine.setIdUser(c.getString(2));
            medicine.setType(c.getString(3));
            medicine.setExpirationDate(c.getLong(4));
            medicine.setDoseNumber(c.getInt(5));
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
            medicine.setExpirationDate(c.getLong(4));
            medicine.setDoseNumber(c.getInt(5));

            medicines.add(medicine);
        }
        c.close();
        db.close();
        return medicines;

    }

    public String updateMedicine(Medicine med){

        JSONObject data = null;
        try {
            data = new JSONObject(gson.toJson(med));
        } catch (JSONException e) { }

        JSONObject res = callApi("/medicines/" + med.getId(), Request.Method.POST, data);

        if (res != null) {
            resetSyncId(res);

            SQLiteDatabase db = DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedicinesDb.ID_USER, med.getIdUser());
            values.put(MedicinesDb.DOSE_NUMBER, med.getDoseNumber());
            values.put(MedicinesDb.EXPIRATION_DATE, med.getExpirationDate());
            values.put(MedicinesDb.NAME, med.getName());
            values.put(MedicinesDb.TYPE, med.getType());

            String whereClause = String.format("%s=?", MedicinesDb.ID);
            final String[] whereArgs = { med.getId() };

            db.update(Tablas.MEDICINE, values, whereClause, whereArgs);

            return med.getId();
        }

        return null;
    }

    public String deleteMedicine (Medicine med){

        JSONObject res = callApi("/medicines/" + med.getId(), Request.Method.PUT);

        if (res != null) {
            resetSyncId(res);

            try {
                med.setId(res.getString("_id"));
            } catch (JSONException e) { }

            SQLiteDatabase db= DataBase.getWritableDatabase();

            String whereClause = String.format("%s=?", MedicinesDb.ID);
            String[] whereArgs = {String.valueOf(med.getId())};
            db.delete(Tablas.MEDICINE, whereClause, whereArgs);
            db.close();

            return med.getId();
        }

        return null;
    }

    /* MEDICINE operations */

    /* TREATMENT operations */

    public String insertTreatment(Treatment treatment){

        JSONObject data = null;
        try {
            data = new JSONObject(gson.toJson(treatment));
        } catch (JSONException e) { }

        JSONObject res = callApi("/treatments/new", Request.Method.POST, data);

        if (res != null) {
            try {
                treatment.setId(res.getString("_id"));
            } catch (JSONException e) { }

            SQLiteDatabase db= DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedicinesDb.ID, treatment.getId());
            values.put(TreatmentsDb.ID_USER, treatment.getIdUser());
            values.put(TreatmentsDb.NAME, treatment.getName());

            db.insertOrThrow(Tablas.TREATMENT, null, values);
            db.close();

            return treatment.getId();
        }

        return null;
    }

    public String updateTreatment(Treatment treatment, List<MedTretRel> removedRelations){
        JSONObject data = new JSONObject();
        try {
            data.put("treatment", new JSONObject(gson.toJson(treatment)));
            data.put("removed_relations", new JSONArray(gson.toJson(removedRelations)));
        } catch (JSONException e) { }

        JSONObject res = callApi("/treatments/" + treatment.getId(), Request.Method.POST, data);

        if (res != null) {
            resetSyncId(res);

            // Tratamiento
            SQLiteDatabase db = DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedicinesDb.NAME, treatment.getName());

            String whereClause = String.format("%s=?", TreatmentsDb.ID);
            final String[] whereArgs = {treatment.getId()};

            db.update(Tablas.TREATMENT, values, whereClause, whereArgs);

            // Relaciones

            return treatment.getId();
        }

        return null;
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

    // TODO: Logica (o no)
    public String deleteTreatment (Treatment treatment){

        JSONObject res = callApi("/treatments/" + treatment.getId(), Request.Method.PUT);

        if (res != null) {
            resetSyncId(res);

            SQLiteDatabase db= DataBase.getWritableDatabase();

            String whereClause = String.format("%s=?", TreatmentsDb.ID);
            String[] whereArgs = {String.valueOf(treatment.getId())};
            db.delete(Tablas.TREATMENT, whereClause, whereArgs);
            db.close();

            return treatment.getId();
        }

        return null;
    }

    /* TREATMENT operations */

    /* RELATION operations */

    public String insertRelation(MedTretRel relation){

        JSONObject data = null;
        try {
            data = new JSONObject(gson.toJson(relation));
        } catch (JSONException e) { }

        JSONObject res = callApi("/relations/new", Request.Method.POST, data);

        if (res != null) {
            try {
                relation.setId(res.getString("_id"));
            } catch (JSONException e) { }

            SQLiteDatabase db= DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedicinesDb.ID, relation.getId());
            values.put(MedTretRelDb.ID_MED, relation.getIdMedicine());
            values.put(MedTretRelDb.ID_TRAT, relation.getIdTreatment());
            values.put(MedTretRelDb.FREQUENCY, relation.getFrequency());
            values.put(MedTretRelDb.INITIAL_DATE, relation.getInitialDate());
            values.put(MedTretRelDb.FINAL_DATE, relation.getFinalDate());

            db.insertOrThrow(Tablas.RELATION_MED_TREATMENT, null, values);
            db.close();

            return relation.getId();
        }
        return null;
    }

    public String updateRelation(MedTretRel relation){
        JSONObject data = new JSONObject();
        try {
            data.put("relation", new JSONObject(gson.toJson(relation)));
        } catch (JSONException e) { }

        JSONObject res = callApi("/relations/" + relation.getId(), Request.Method.POST, data);

        if (res != null) {
            resetSyncId(res);

            SQLiteDatabase db = DataBase.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(MedTretRelDb.FINAL_DATE, relation.getFinalDate());
            values.put(MedTretRelDb.ID_MED, relation.getIdMedicine());
            values.put(MedTretRelDb.FREQUENCY, relation.getFrequency());

            String whereClause = String.format("%s=?", MedTretRelDb.ID);
            final String[] whereArgs = {relation.getId()};

            db.update(Tablas.RELATION_MED_TREATMENT, values, whereClause, whereArgs);

            return relation.getId();
        }

        return null;
    }

    public List<MedTretRel> getRelations_treatmentId(String treatmentId) {
        SQLiteDatabase db = DataBase.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.RELATION_MED_TREATMENT, MedTretRelDb.ID_TRAT);

        String[] selectionArgs = {String.valueOf(treatmentId)};
        Cursor c = db.rawQuery(sql, selectionArgs);

        List<MedTretRel> relations = new ArrayList<>();

        while (c.moveToNext()) {
            MedTretRel relation = new MedTretRel();

            relation.setId(c.getString(0));
            relation.setIdTreatment(c.getString(1));
            relation.setIdMedicine(c.getString(2));
            relation.setFrequency(c.getInt(3));
            relation.setInitialDate(c.getLong(4));
            relation.setFinalDate(c.getLong(5));

            relations.add(relation);

        }
        c.close();
        db.close();
        return relations;
    }

    public String deleteRelation(MedTretRel relation){

        JSONObject res = callApi("/relations/" + relation.getId(), Request.Method.PUT);

        if (res != null) {
            resetSyncId(res);

            SQLiteDatabase db= DataBase.getWritableDatabase();

            String whereClause = String.format("%s=?", MedTretRelDb.ID);
            String[] whereArgs = { String.valueOf(relation.getId()) };
            db.delete(Tablas.RELATION_MED_TREATMENT, whereClause, whereArgs);
            db.close();

            return relation.getId();
        }

        return null;
    }

    /* RELATION operations */
}

