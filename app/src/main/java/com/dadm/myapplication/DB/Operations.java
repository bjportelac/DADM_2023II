package com.dadm.myapplication.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dadm.myapplication.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class Operations {

    public static final String TAG = "DEVCONT_MNGMNT_SYS";

    @SuppressWarnings("SpellCheckingInspection")
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            Handler.COLUMN_ID,
            Handler.COLUMN_NAME,
            Handler.COLUMN_URL,
            Handler.COLUMN_PHONE,
            Handler.COLUMN_EMAIL,
            Handler.COLUMN_DESCRIPTION,
            Handler.COLUMN_CONSULTING,
            Handler.COLUMN_DEVELOPMENT,
            Handler.COLUMN_SOFTWARE_FACTORY
    };

    public Operations(Context context){
        dbhandler = new Handler(context);
    }

    public void openConnection(){
        Log.i(TAG,"DB connection opened successfully");
        database = dbhandler.getWritableDatabase();
    }

    public void closeConnection(){
        Log.i(TAG,"DB connection closed successfully");
        dbhandler.close();
    }

    public Contact addContact(@NonNull Contact contact){
        ContentValues values  = new ContentValues();
        values.put(Handler.COLUMN_NAME, contact.getName());
        values.put(Handler.COLUMN_URL, contact.getUrl());
        values.put(Handler.COLUMN_PHONE, contact.getPhone());
        values.put(Handler.COLUMN_EMAIL, contact.getEmail());
        values.put(Handler.COLUMN_DESCRIPTION, contact.getDescription());
        values.put(Handler.COLUMN_CONSULTING, contact.getIsConsulting());
        values.put(Handler.COLUMN_DEVELOPMENT, contact.getIsDevelopment());
        values.put(Handler.COLUMN_SOFTWARE_FACTORY, contact.getIsSoftwareFactory());

        long insertId = database.insert(Handler.TABLE_CONTACTS, null, values);
        contact.setConId(insertId);

        return contact;
    }

    public Contact getContact(long id) {
        Cursor cursor = database.query(Handler.TABLE_CONTACTS, allColumns, Handler.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8));

        cursor.close();

        return contact;
    }

    @SuppressLint("Range")
    public List<Contact> getAllContacts() {

        Cursor cursor = database.query(Handler.TABLE_CONTACTS, allColumns, null, null, null, null, null);

        List<Contact> contacts = new ArrayList<>();
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Contact contact = new Contact();

                contact.setConId(cursor.getLong(cursor.getColumnIndex(Handler.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_NAME)));
                contact.setUrl(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_URL)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_PHONE)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_EMAIL)));
                contact.setDescription(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_DESCRIPTION)));
                contact.setIsConsulting(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_CONSULTING)));
                contact.setIsDevelopment(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_DEVELOPMENT)));
                contact.setIsSoftwareFactory(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_SOFTWARE_FACTORY)));

                contacts.add(contact);
            }
        }
        cursor.close();

        return contacts;
    }

    @SuppressLint("Range")
    public List<Contact> getFilteredContacts(boolean isConsulting, boolean isDeveloper, boolean isSoftwareFactory) {

        String restriction = "";
        List<String> restrictionValues = new ArrayList<>();

        if(isConsulting) {
            restriction += Handler.COLUMN_CONSULTING + "=?";
            restrictionValues.add(String.valueOf(1));
        }
        if(isDeveloper) {
            if(!restriction.isEmpty()){
                restriction += " OR ";
            }
            restriction += Handler.COLUMN_DEVELOPMENT + "=?";
            restrictionValues.add(String.valueOf(1));
        }
        if(isSoftwareFactory) {
            if(!restriction.isEmpty()) {
                restriction += " OR ";
            }
            restriction += Handler.COLUMN_SOFTWARE_FACTORY + "=?";
            restrictionValues.add(String.valueOf(1));
        }

        Cursor cursor = database.query(Handler.TABLE_CONTACTS,
                allColumns,
                restriction,
                restrictionValues.toArray(new String[]{}), null, null, null, null);

        List<Contact> contacts = new ArrayList<>();
        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Contact contact = new Contact();
                contact.setConId(cursor.getLong(cursor.getColumnIndex(Handler.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_NAME)));
                contact.setUrl(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_URL)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_PHONE)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_EMAIL)));
                contact.setDescription(cursor.getString(cursor.getColumnIndex(Handler.COLUMN_DESCRIPTION)));
                contact.setIsConsulting(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_CONSULTING)));
                contact.setIsDevelopment(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_DEVELOPMENT)));
                contact.setIsSoftwareFactory(cursor.getInt(cursor.getColumnIndex(Handler.COLUMN_SOFTWARE_FACTORY)));
                contacts.add(contact);
            }
        }

        cursor.close();

        return contacts;
    }

    public int updateContact(@NonNull Contact contact) {
        ContentValues values = new ContentValues();
        values.put(Handler.COLUMN_NAME, contact.getName());
        values.put(Handler.COLUMN_URL, contact.getUrl());
        values.put(Handler.COLUMN_PHONE, contact.getPhone());
        values.put(Handler.COLUMN_EMAIL, contact.getEmail());
        values.put(Handler.COLUMN_DESCRIPTION, contact.getDescription());
        values.put(Handler.COLUMN_CONSULTING, contact.getIsConsulting());
        values.put(Handler.COLUMN_DEVELOPMENT, contact.getIsDevelopment());
        values.put(Handler.COLUMN_SOFTWARE_FACTORY, contact.getIsSoftwareFactory());

        return database.update(Handler.TABLE_CONTACTS, values,
                Handler.COLUMN_ID + "=?", new String[] { String.valueOf(contact.getConId())});
    }

    public void removeContact(@NonNull Contact employee) {
        database.delete(Handler.TABLE_CONTACTS, Handler.COLUMN_ID + "=" + employee.getConId(), null);
    }

}

