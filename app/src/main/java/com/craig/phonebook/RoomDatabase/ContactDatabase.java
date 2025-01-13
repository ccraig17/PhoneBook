package com.craig.phonebook.RoomDatabase;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1) //entities = the table(s) in the database which is of the Class Contact, version = the version of the database
public abstract class ContactDatabase extends RoomDatabase {
    private static ContactDatabase instance;   // Singleton instance of the database obj to access the db
    public abstract ContactDAO contactDAO();   // DAO for accessing the database methods (CRUD) notice its an abstract method since the DAO class is an interface

    public static synchronized ContactDatabase getInstance(Context context){
        if(instance == null){ //if there is no instance of the database, create one using Room.databaseBuilder
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactDatabase.class,
                    "contact_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
