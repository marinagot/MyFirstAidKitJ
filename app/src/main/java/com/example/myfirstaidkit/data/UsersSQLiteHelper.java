package com.example.myfirstaidkit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfirstaidkit.data.UserUtilities.*;

import java.util.Date;

public class UsersSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Users.db";

    public UsersSQLiteHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UserEntry.USER_TABLE + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserEntry.USERNAME + " TEXT NOT NULL,"
                + UserEntry.EMAIL + " TEXT NOT NULL,"
                + UserEntry.BIRTHDATE + " DATE NOT NULL,"
                + UserEntry.AVATAR + " TEXT,"
                + UserEntry.PASSWORD + " TEXT NOT NULL,"
                + UserEntry.CONFIRM_PASSWORD + " TEXT NOT NULL" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.USER_TABLE);
        onCreate(db);
    }

    public void insertData(String username, String email, String birthday, String avatar, String password, String confirmPassword) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(UserEntry.USERNAME, username);
        cv.put(UserEntry.EMAIL, email);
        cv.put(UserEntry.BIRTHDATE, birthday);
        cv.put(UserEntry.AVATAR, avatar);
        cv.put(UserEntry.PASSWORD, password);
        cv.put(UserEntry.CONFIRM_PASSWORD, confirmPassword);


        db.insert(UserEntry.USER_TABLE,null,cv);
        db.close();
    }

    public boolean loginData(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exist = false;
        Cursor c = db.rawQuery("SELECT USERNAME,PASSWORD FROM " +
                UserUtilities.UserEntry.USER_TABLE + " WHERE " + "USERNAME='" +
                username + "' AND PASSWORD='" + password + "'", null);

        if (c.moveToFirst() == true) {

            String un = c.getString(0);
            String pw = c.getString(1);

            if (username.equals(un) && password.equals(pw)) {
                exist = true;
            }
        }
        return exist;
    }
}
