package com.craig.phonebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.widget.Toast;

import com.craig.phonebook.Activities.ContactListActivity;
import com.google.android.material.snackbar.Snackbar;

public class DatabaseAccess extends SQLiteOpenHelper {
    private static final String CONTACTS_LIST = "phonebook.db";
    private static final int VERSION = 2;
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String PHONE = "phone";
    private static final String EMAIL= "email";
    private static final String IMAGE= "image";
    private SQLiteDatabase db;
    public DatabaseAccess(Context context) {
        super(context, CONTACTS_LIST, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String sql = "CREATE TABLE table_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, email TEXT, image TEXT)";
        String sql2 = "CREATE TABLE " +TABLE_NAME+ "(" +COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +NAME+ " TEXT, " +TITLE+ " TEXT, " + PHONE+ " TEXT, " +EMAIL+ " TEXT, " +IMAGE+ " TEXT )";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
        }
    }


    public boolean insert(String name, String title, String phone, String email, byte[] image){
        db = this.getWritableDatabase();
        ContentValues valuesAdded = new ContentValues();
        valuesAdded.put(NAME, name);
        valuesAdded.put(TITLE, title);
        valuesAdded.put(PHONE, phone);
        valuesAdded.put(EMAIL, email);
        valuesAdded.put(IMAGE, image);
       long result = db.insert(TABLE_NAME, null, valuesAdded);
       db.close();
        return result != -1;
    }
    //update using contact id from db NOT NAME; there could be two contacts with the same name. ** FIX!
    public boolean update(String name, String title, String phone, String email, String image){
        db = this.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        String sqlUpdate = "SELECT * FROM " + TABLE_NAME + " WHERE " + "NAME" + "=" + "?";
        valuesUpdate.put(NAME, name);
        valuesUpdate.put(TITLE, title);
        valuesUpdate.put(PHONE, phone);
        valuesUpdate.put(EMAIL, email);
        valuesUpdate.put(IMAGE, image);
        long result = db.update(TABLE_NAME, valuesUpdate, sqlUpdate, new String[]{name});
        db.close();
        return result != -1;
    };
    public boolean delete(String contactName){
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " = ? ", new String[]{contactName});
        if(cursor.getCount() > 0){
            long result = db.delete(TABLE_NAME, NAME + " = ? ", new String[]{contactName});
            return result != -1;
        }else{
            return false;
        }
    };

    public Cursor readAllData(){
        db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + NAME + " ASC ";
        Cursor cursor = null;
        if(db != null){
           cursor = db.rawQuery(sql, null);
        }
        return cursor;
    }
    public void open(){
        db = this.getWritableDatabase();
    }
    public void close(){
        db.close();
    }
}














