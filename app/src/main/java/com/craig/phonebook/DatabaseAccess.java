package com.craig.phonebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.CaseMap;
import android.net.Uri;

public class DatabaseAccess extends SQLiteOpenHelper {
    private static final String CONTACTS_LIST = "phonebook.db";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String NAME = "name";
    private static final String TITLE = "title";
    private static final String EMAIL= "email";
    private static final String IMAGE= "image";
    private SQLiteDatabase db;
    public DatabaseAccess(Context context) {
        super(context, CONTACTS_LIST, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String sql = "CREATE TABLE table_name (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, email TEXT, image TEXT)";
        String sql2 = "CREATE TABLE " +TABLE_NAME+ "(" +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +NAME+ " TEXT, " +TITLE+" TEXT, "+EMAIL+" TEXT, "+IMAGE+" TEXT )";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
    }

    public boolean insert(String name, String title, String email, byte[] image){
        db = this.getWritableDatabase();
        ContentValues valuesAdded = new ContentValues();
        valuesAdded.put(NAME, name);
        valuesAdded.put(TITLE, title);
        valuesAdded.put(EMAIL, email);
        valuesAdded.put(IMAGE, image);
       long result = db.insert(TABLE_NAME, null, valuesAdded);
        return (result !=-1);
    }
    public boolean update(String name, String title, String email, String image){
        db = this.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        String sqlUpdate = "SELECT * FROM " + TABLE_NAME + " WHERE " + "NAME" + "=" + "?";
        valuesUpdate.put(NAME, name);
        valuesUpdate.put(TITLE, title);
        valuesUpdate.put(EMAIL, email);
        valuesUpdate.put(String.valueOf(IMAGE), image);
        long result = db.update(TABLE_NAME, valuesUpdate, sqlUpdate, new String[]{name});
        return result != -1;
    };
    public boolean delete(String name){
        db = this.getWritableDatabase();
        String sqlDlete = "SELECT * FROM " + TABLE_NAME + "WHERE " + "NAME" + "=" + "?";
        long result = db.delete(TABLE_NAME, sqlDlete, new String[]{name});
        return(result != -1);
    };

    public Cursor returnData(){
        db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME,  new String[]{NAME+" ASC"});
        //return db.rawQuery("SELECT * FROM " + TABLE_NAME, null); Option if you DON'T want to sort the data
    }
    public void open(){
        db = this.getWritableDatabase();
    }
    public void close(){
        db.close();
    }
}














