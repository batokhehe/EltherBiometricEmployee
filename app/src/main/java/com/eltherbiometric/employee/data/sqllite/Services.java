package com.eltherbiometric.employee.data.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Services extends DBHandler {
    public Services(Context context) {
        super(context);
    }

    public com.eltherbiometric.employee.data.model.User FindUser(String nik) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = User.COLUMN_NIK + " = ?";
        String[] selectionArgs = { String.valueOf(nik) };

        Cursor cursor = db.query(
                User.TABLE_NAME,
                new String[]{User.COLUMN_NIK, User.COLUMN_NAME},
                selection,
                selectionArgs,
                null,
                null,
                null);
        com.eltherbiometric.employee.data.model.User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new com.eltherbiometric.employee.data.model.User();
            user.setNik(cursor.getString(cursor.getColumnIndex(User.COLUMN_NIK)));
            user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));

        }
        return user;
    }

    public com.eltherbiometric.employee.data.model.User FindUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = User.COLUMN_USERNAME + " = ? AND " + User.COLUMN_PASSWORD + " = ? ";
        String[] selectionArgs = {String.valueOf(username), String.valueOf(password)};

        Cursor cursor = db.query(
                User.TABLE_NAME,
                new String[]{User.COLUMN_NIK, User.COLUMN_NAME},
                selection,
                selectionArgs,
                null,
                null,
                null);
        com.eltherbiometric.employee.data.model.User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new com.eltherbiometric.employee.data.model.User();
            user.setNik(cursor.getString(cursor.getColumnIndex(User.COLUMN_NIK)));
            user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
            cursor.close();
        }
        return user;
    }

    public com.eltherbiometric.employee.data.model.Presence FindPresence(String nik) {
        SQLiteDatabase db = this.getReadableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date_ = dateFormat.format(date);

        String selection = Presence.COLUMN_NIK + " = ? AND " + Presence.COLUMN_DATE + " = ? ";
        String[] selectionArgs = {String.valueOf(nik), String.valueOf(date_)};

        Cursor cursor = db.query(
                Presence.TABLE_NAME,
                new String[]{Presence.COLUMN_NIK},
                selection,
                selectionArgs,
                null,
                null,
                null);
        com.eltherbiometric.employee.data.model.Presence presence = null;
        if (cursor != null && cursor.moveToFirst()) {
            presence = new com.eltherbiometric.employee.data.model.Presence();
            presence.setNik(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_NIK)));
            cursor.close();
        }
        return presence;
    }

    public void Save(String nik, String name, String username, String password, String division) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        try {
            contentValues.put(User.COLUMN_NIK, nik);
            contentValues.put(User.COLUMN_NAME, name);
            contentValues.put(User.COLUMN_USERNAME, username);
            contentValues.put(User.COLUMN_PASSWORD, password);
            contentValues.put(User.COLUMN_DIVISION, division);
            db.insert(User.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void Presence(String nik, String latitude, String longitude, String method) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String date_ = dateFormat.format(date);
        String time_ = timeFormat.format(date);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        try {
            contentValues.put(Presence.COLUMN_NIK, nik);
            contentValues.put(Presence.COLUMN_METHOD, method);
            contentValues.put(Presence.COLUMN_LATITUDE, latitude);
            contentValues.put(Presence.COLUMN_LONGITUDE, longitude);
            contentValues.put(Presence.COLUMN_DATE, date_);
            contentValues.put(Presence.COLUMN_TIME, time_);
            db.insert(Presence.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<com.eltherbiometric.employee.data.model.Presence> AllPresence() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date_ = dateFormat.format(date);

        List<com.eltherbiometric.employee.data.model.Presence> Datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_NIK + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_DATE + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_TIME + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_METHOD + ", " +
                User.TABLE_NAME + "." + User.COLUMN_NAME + " " +
                "FROM " +
                Presence.TABLE_NAME + " " +
                "JOIN " +
                User.TABLE_NAME + " " +
                "ON " + User.TABLE_NAME + "." + User.COLUMN_NIK + " = " + Presence.TABLE_NAME + "." + Presence.COLUMN_NIK + " " +
                "WHERE " + Presence.TABLE_NAME + "." + Presence.COLUMN_DATE + " = ? ";

        Cursor cursor = db.rawQuery(query,
                new String[] {date_});


        if (cursor != null && cursor.moveToFirst()) {
            do {
                com.eltherbiometric.employee.data.model.Presence presence = new com.eltherbiometric.employee.data.model.Presence();
                presence.setNik(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_NIK)));
                presence.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
                presence.setDate(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DATE)));
                presence.setTime(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_TIME)));
                presence.setMethod(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_METHOD)));

                Datas.add(presence);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return Datas;
    }

    public List<com.eltherbiometric.employee.data.model.Presence> AllAbsence() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date_ = dateFormat.format(date);

        List<com.eltherbiometric.employee.data.model.Presence> Datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                User.TABLE_NAME + "." + User.COLUMN_NIK + ", " +
                User.TABLE_NAME + "." + User.COLUMN_NAME + " " +
                "FROM " +
                User.TABLE_NAME + " " +
                "WHERE " + User.TABLE_NAME + "." + User.COLUMN_NIK + " NOT IN(" +
                "SELECT " + Presence.TABLE_NAME + "." + Presence.COLUMN_NIK + " " +
                "FROM " + Presence.TABLE_NAME + " " +
                "WHERE " + Presence.TABLE_NAME + "." + Presence.COLUMN_DATE + "  = ? )";

        Cursor cursor = db.rawQuery(query,
                new String[] {date_});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                com.eltherbiometric.employee.data.model.Presence presence = new com.eltherbiometric.employee.data.model.Presence();
                presence.setNik(cursor.getString(cursor.getColumnIndex(User.COLUMN_NIK)));
                presence.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));

                Datas.add(presence);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return Datas;
    }

    public List<com.eltherbiometric.employee.data.model.Presence> AllDatas() {

        List<com.eltherbiometric.employee.data.model.Presence> Datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_NIK + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_DATE + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_TIME + ", " +
                Presence.TABLE_NAME + "." + Presence.COLUMN_METHOD + ", " +
                User.TABLE_NAME + "." + User.COLUMN_NAME + " " +
                "FROM " +
                Presence.TABLE_NAME + " " +
                "JOIN " +
                User.TABLE_NAME + " " +
                "ON " + User.TABLE_NAME + "." + User.COLUMN_NIK + " = " + Presence.TABLE_NAME + "." + Presence.COLUMN_NIK + " ";

        Cursor cursor = db.rawQuery(query, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                com.eltherbiometric.employee.data.model.Presence presence = new com.eltherbiometric.employee.data.model.Presence();
                presence.setNik(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_NIK)));
                presence.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME)));
                presence.setDate(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DATE)));
                presence.setTime(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_TIME)));
                presence.setMethod(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_METHOD)));

                Datas.add(presence);
            } while (cursor.moveToNext());
        }
        assert cursor != null;
        cursor.close();
        return Datas;
    }
}
