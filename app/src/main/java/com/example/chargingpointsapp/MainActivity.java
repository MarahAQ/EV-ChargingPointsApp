package com.example.chargingpointsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private Button loginButton, signUpButton;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        db.insertAdmin();
        CSVHelper.loadChargepointsFromCSV(this, db);

        // ✅ Fix: Use TextInputEditText instead of TextInputLayout
        emailField = findViewById(R.id.Main_email); // ✅ Make sure this ID exists in XML
        passwordField = findViewById(R.id.Main_password);
        loginButton = findViewById(R.id.Main_login_button);
        signUpButton = findViewById(R.id.button_signup);

        loginButton.setOnClickListener(view -> handleLogin());
        signUpButton.setOnClickListener(view -> startActivity(new Intent(this, SignupActivity.class)));
    }

    private void handleLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.validateUser(email, password)) {
            boolean isAdmin = db.isAdmin(email);
            Intent intent = isAdmin ? new Intent(this, AdminHomeActivity.class) : new Intent(this, UserHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
        }
    }
}