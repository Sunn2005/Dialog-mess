package com.example.dialogmess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1; // Phiên bản cơ sở dữ liệu
    private static final String TABLE_USERS = "Users"; // Tên bảng

    private static final String COLUMN_NAME = "name"; // Tên cột cho tên người dùng
    private static final String COLUMN_IMAGE = "image"; // Tên cột cho hình ảnh người dùng

// database

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }
    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_IMAGE + " TEXT" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, user.getImage());

        db.update(TABLE_USERS, values, COLUMN_NAME + " = ?", new String[]{user.getName()});
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Check", "User name:" + user.getName());// kiểm tra giá trị biến
        db.delete(TABLE_USERS, COLUMN_NAME + " = ?", new String[]{user.getName()});
        db.close();
    }
}
