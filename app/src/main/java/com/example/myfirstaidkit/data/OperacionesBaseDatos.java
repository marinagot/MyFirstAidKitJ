package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.myfirstaidkit.data.BaseDatos.Tablas;
import com.example.myfirstaidkit.data.FirstAidKit.Treatment;
import com.example.myfirstaidkit.data.FirstAidKit.Kit;
import com.example.myfirstaidkit.data.FirstAidKit.User;
import com.example.myfirstaidkit.data.FirstAidKit.Medicine;


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

    public Cursor obtenerDetallesPorIdMedicina(String idMedicine) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.MEDICINE, Kit.ID_MED);

        String[] selectionArgs = {idMedicine};

        return db.rawQuery(sql, selectionArgs);

    }

    public long insertarUser(com.example.myfirstaidkit.data.User user){
        SQLiteDatabase db= baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();


        valores.put(User.PASSWORD,user.getPassword());
        valores.put(User.USERNAME,user.getUsername());
        valores.put(User.AVATAR,user.getAvatar());
        valores.put(User.BIRTHDAY, user.getBirthday());
        valores.put(User.EMAIL, user.getEmail());

        long idUser=db.insertOrThrow(Tablas.USER,null,valores);

        return idUser;
    }

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
}

