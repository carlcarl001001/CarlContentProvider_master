package com.example.carl.carlcontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "carl.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG","on create.");
        //建表
        db.execSQL("create table person(_id integer primary key autoincrement,name varchar)");
        //插入初始化数据
        db.execSQL("insert into person (name) values ('tom')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
