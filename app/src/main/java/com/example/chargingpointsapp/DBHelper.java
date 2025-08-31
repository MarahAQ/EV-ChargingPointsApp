package com.example.chargingpointsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChargingPointsDB.db";
    private static final int DATABASE_VERSION = 7; // Incremented version for schema change

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ✅ Changed `username` to `email`
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +  // ✅ Changed username to email
                "password TEXT, " +
                "isAdmin INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE chargepoints (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "referenceID TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "town TEXT, " +
                "postcode TEXT, " +
                "county TEXT, " +
                "connectorID TEXT, " +
                "chargeDeviceStatus TEXT, " +
                "connectorType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS chargepoints");
        onCreate(db);
    }

    // ✅ Validate user login by email
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // ✅ Check if the user is an admin
    public boolean isAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT isAdmin FROM users WHERE email = ?", new String[]{email});
        boolean isAdmin = false;
        if (cursor != null && cursor.moveToFirst()) {
            isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow("isAdmin")) == 1;
            cursor.close();
        }
        return isAdmin;
    }

    // ✅ Check if an email already exists
    public boolean isUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ✅ Insert a new user (using email instead of username)
    public boolean insertUser(String email, String password, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email); // ✅ Changed from username
        values.put("password", password);
        values.put("isAdmin", isAdmin ? 1 : 0);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    // ✅ Insert admin user
    public boolean insertAdmin() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{"admin@admin.com"});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Admin already exists, no need to insert again
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("email", "admin@admin.com"); // ✅ Changed from username
        values.put("password", "1234");
        values.put("isAdmin", 1);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    // ✅ Delete chargepoint by referenceID
    public boolean deleteChargepointByReferenceID(String referenceID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("chargepoints", "referenceID = ?", new String[]{referenceID});
        return rowsAffected > 0;
    }

    // ✅ Insert new chargepoint
    public boolean insertChargepoint(String referenceID, double latitude, double longitude, String town, String county, String postcode, String status, String connectorID, String connectorType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("referenceID", referenceID);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("town", town);
        values.put("county", county);
        values.put("postcode", postcode);
        values.put("chargeDeviceStatus", status);
        values.put("connectorID", connectorID);
        values.put("connectorType", connectorType);

        long result = db.insert("chargepoints", null, values);
        return result != -1;
    }

    // ✅ Insert sample data for testing
    public void insertSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM chargepoints");

        ContentValues values = new ContentValues();
        values.put("referenceID", "CP001");
        values.put("latitude", 53.795984);
        values.put("longitude", -1.759398);
        values.put("town", "Bradford");
        values.put("county", "West Yorkshire");
        values.put("postcode", "BD1 1AA");
        values.put("chargeDeviceStatus", "Available");
        values.put("connectorID", "C001");
        values.put("connectorType", "Type 2");
        db.insert("chargepoints", null, values);

        values.clear();
        values.put("referenceID", "CP002");
        values.put("latitude", 51.509865);
        values.put("longitude", -0.118092);
        values.put("town", "London");
        values.put("county", "Greater London");
        values.put("postcode", "WC2N 5DU");
        values.put("chargeDeviceStatus", "In Use");
        values.put("connectorID", "C002");
        values.put("connectorType", "CHAdeMO");
        db.insert("chargepoints", null, values);
    }

    // ✅ Get all chargepoints
    public Cursor getAllChargepoints() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM chargepoints ORDER BY id DESC", null);
    }

    // ✅ Get chargepoints summary
    public ArrayList<String> getChargepointsSummary() {
        ArrayList<String> summaryList = new ArrayList<>();
        Cursor cursor = getAllChargepoints();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String referenceID = cursor.getString(cursor.getColumnIndexOrThrow("referenceID"));
                String town = cursor.getString(cursor.getColumnIndexOrThrow("town"));
                String postcode = cursor.getString(cursor.getColumnIndexOrThrow("postcode"));
                summaryList.add("Ref: " + referenceID + "\nTown: " + town + "\nPostcode: " + postcode);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return summaryList;
    }
}