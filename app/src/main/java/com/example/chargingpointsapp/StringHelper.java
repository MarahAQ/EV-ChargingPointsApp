package com.example.chargingpointsapp;

public class StringHelper {

    // ✅ Improved email validation using regex
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}