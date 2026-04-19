package com.example.mr8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context c) {
        super(c, "med_app.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE profiles(id TEXT PRIMARY KEY, name TEXT)");

        db.execSQL("CREATE TABLE medicines(" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "time TEXT, " +
                "date TEXT, " +
                "food TEXT, " +
                "frequency TEXT, " +
                "profileId TEXT, " +
                "status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS profiles");
        db.execSQL("DROP TABLE IF EXISTS medicines");
        onCreate(db);
    }

    // ================= PROFILE =================

    public void insertProfile(Profile p) {
        ContentValues cv = new ContentValues();
        cv.put("id", p.id);
        cv.put("name", p.name);
        getWritableDatabase().insert("profiles", null, cv);
    }

    public void deleteProfile(String id) {
        getWritableDatabase().delete("profiles", "id=?", new String[]{id});
    }

    public ArrayList<Profile> getProfiles() {

        ArrayList<Profile> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM profiles", null);

        while (c.moveToNext()) {
            list.add(new Profile(
                    c.getString(0),
                    c.getString(1)
            ));
        }
        c.close();
        return list;
    }

    // ================= MEDICINE =================

    public void insertMedicine(Medicine m) {

        ContentValues cv = new ContentValues();
        cv.put("id", m.id);
        cv.put("name", m.name);
        cv.put("time", m.time);
        cv.put("date", m.date);
        cv.put("food", m.food);
        cv.put("frequency", m.frequency);
        cv.put("profileId", m.profileId);
        cv.put("status", m.status);

        getWritableDatabase().insert("medicines", null, cv);
    }

    public void updateMedicine(Medicine m) {

        ContentValues cv = new ContentValues();
        cv.put("name", m.name);
        cv.put("time", m.time);
        cv.put("date", m.date);
        cv.put("food", m.food);
        cv.put("frequency", m.frequency);
        cv.put("profileId", m.profileId);
        cv.put("status", m.status);

        getWritableDatabase().update("medicines", cv, "id=?", new String[]{m.id});
    }

    public void deleteMedicine(String id) {
        getWritableDatabase().delete("medicines", "id=?", new String[]{id});
    }

    public ArrayList<Medicine> getMedicines(String profileId) {

        ArrayList<Medicine> list = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM medicines WHERE profileId=?",
                new String[]{profileId}
        );

        while (c.moveToNext()) {
            list.add(parse(c));
        }

        c.close();
        return list;
    }

    // ✅ FIX FOR YOUR ERROR
    public Medicine getMedicineById(String id) {

        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM medicines WHERE id=?",
                new String[]{id}
        );

        if (c.moveToFirst()) {
            Medicine m = parse(c);
            c.close();
            return m;
        }

        c.close();
        return null;
    }

    private Medicine parse(Cursor c) {

        Medicine m = new Medicine();

        m.id = c.getString(0);
        m.name = c.getString(1);
        m.time = c.getString(2);
        m.date = c.getString(3);
        m.food = c.getString(4);
        m.frequency = c.getString(5);
        m.profileId = c.getString(6);
        m.status = c.getString(7);

        return m;
    }
}
