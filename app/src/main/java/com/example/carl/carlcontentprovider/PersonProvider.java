package com.example.carl.carlcontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 操作perseon表的类
 * */
public class PersonProvider extends ContentProvider {
    private String TAG = "PersonProvider";
    //用来存放所有合肥的URI的容器
    private static UriMatcher mathcer = new UriMatcher(UriMatcher.NO_MATCH);
    //保存一些合法的uri
    //content://com.example.carl.carlcontentprovider.personprovider/person 不根据id操作
    //content://com.example.carl.carlcontentprovider.personprovider/person/66 根据id操作
    static {
        mathcer.addURI("com.example.carl.carlcontentprovider.personprovider","/person",1);
        mathcer.addURI("com.example.carl.carlcontentprovider.personprovider","person/#",66);//#匹配任一数字

    }
    private DBHelper dbHelper;
    public PersonProvider() {
        Log.i(TAG,"PersonProvider");
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG,"onCreate....");
        dbHelper = new DBHelper(getContext());
        return false;
    }

    //content://com.example.carl.carlcontentprovider.personprovider/person 不根据id操作
    //content://com.example.carl.carlcontentprovider.personprovider/person/66 根据id操作
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(TAG,"query....");
        Cursor cursor;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //1.匹配uri，返回code
        int code = mathcer.match(uri);
        Log.i(TAG,"code:"+code);
        //如果合法，进行查询
        if (code  == 1){
            cursor = database.query("person",projection,selection,selectionArgs,null,null,null);
        }else if (code == 66){
            //得到id
            long id = ContentUris.parseId(uri);
            cursor = database.query("person",projection,"_id=?",new String[]{id+""},null,null,null);
            //查询
        }else {//如果不合法，抛出异常
            throw new RuntimeException("uri不合法");
        }
        return cursor;
    }

    //content://com.example.carl.carlcontentprovider.personprovider/person 插入
    //content://com.example.carl.carlcontentprovider.personprovider/person/66 （不能）根据id插入
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.i(TAG,"insert....");
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //1.匹配uri，返回code
        int code = mathcer.match(uri);
        Log.i(TAG,"code:"+code);
        //如果合法，进行查询
        if (code  == 1){
            long id = database.insert("person",null,values);
            uri = ContentUris.withAppendedId(uri,id);
            database.close();
        }else {//如果不合法，抛出异常
            database.close();
            throw new RuntimeException("uri不合法");
        }

        return uri;
    }
    //content://com.example.carl.carlcontentprovider.personprovider/person 删除
    //content://com.example.carl.carlcontentprovider.personprovider/person/66 根据id删除
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG,"delete....");
        int deleteCount = -1;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //1.匹配uri，返回code
        int code = mathcer.match(uri);
        Log.i(TAG,"code:"+code);
        //如果合法，进行查询
        if (code  == 1){
            deleteCount = database.delete("person",selection,selectionArgs);
        }else if (code == 66){
            //得到id
            long id = ContentUris.parseId(uri);
            deleteCount = database.delete("person","_id=?",new String[]{id+""});
            //查询
        }else {//如果不合法，抛出异常
            database.close();
            throw new RuntimeException("uri不合法");
        }
        database.close();
        return deleteCount;
    }
    //content://com.example.carl.carlcontentprovider.personprovider/person 更新
    //content://com.example.carl.carlcontentprovider.personprovider/person/66 根据id更新
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i(TAG,"update....");
        int updateCount = -1;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //1.匹配uri，返回code
        int code = mathcer.match(uri);
        Log.i(TAG,"code:"+code);
        //如果合法，进行查询
        if (code  == 1){
            updateCount = database.update("person",values,selection,selectionArgs);
        }else if (code == 66){
            //得到id
            long id = ContentUris.parseId(uri);
            updateCount = database.update("person",values,"_id=?",new String[]{id+""});
            //查询
        }else {//如果不合法，抛出异常
            database.close();
            throw new RuntimeException("uri不合法");
        }
        database.close();


        return updateCount;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
