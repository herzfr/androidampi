package com.ampi.registrasi.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ampi.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ANGGOTA = "anggota";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nama";
    private static final String KEY_NO_REGISTRASI= "noRegistrasi";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TIME = "time";


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_ANGGOTA + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " TEXT," +
                KEY_NO_REGISTRASI + " TEXT UNIQUE,"  +
                KEY_IMAGE + " BLOB,"  +
                KEY_STATUS + " TEXT,"  +
                KEY_TIME + " INTEGER"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANGGOTA);
        onCreate(db);
    }

    public void insertData(String name, String noreg, byte[] image, String status, int time){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_ANGGOTA + " VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, noreg);
        statement.bindBlob(3, image);
        statement.bindString(4, status);
        statement.bindLong(5, time);

        statement.executeInsert();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }
}
