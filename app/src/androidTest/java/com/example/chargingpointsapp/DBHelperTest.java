package com.example.chargingpointsapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DBHelperTest {

    private DBHelper dbHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new DBHelper(context);

        // ✅ Clears database before each test
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM users");
        db.execSQL("DELETE FROM chargepoints");
    }

    @Test
    public void testInsertUser() {
        boolean result = dbHelper.insertUser("test@example.com", "password123", false);
        assertTrue("User should be inserted successfully", result);
    }

    @Test
    public void testValidateUser() {
        dbHelper.insertUser("test@example.com", "password123", false);
        boolean result = dbHelper.validateUser("test@example.com", "password123");
        assertTrue("User login should be validated successfully", result);
    }

    @Test
    public void testInvalidLogin() {
        boolean result = dbHelper.validateUser("wrong@example.com", "wrongpassword");
        assertFalse("Login should fail for incorrect credentials", result);
    }

    @Test
    public void testAdminStatus() {
        dbHelper.insertAdmin();
        boolean isAdmin = dbHelper.isAdmin("admin@admin.com");
        assertTrue("Admin user should be recognized", isAdmin);
    }

    @Test
    public void testInsertChargepoint() {
        boolean result = dbHelper.insertChargepoint("CP003", 52.123, -1.456, "Test Town", "Test County", "T123AB", "Available", "C003", "Type 2");
        assertTrue("Chargepoint should be inserted successfully", result);
    }

    @Test
    public void testFetchChargepoints() {
        dbHelper.insertSampleData();
        Cursor cursor = dbHelper.getAllChargepoints();
        assertNotNull("Chargepoints data should be available", cursor);
        assertTrue("Chargepoints should have data", cursor.getCount() > 0);
        cursor.close();
    }
}