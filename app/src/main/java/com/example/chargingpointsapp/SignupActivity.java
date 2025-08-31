package com.example.chargingpointsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText emailField, passwordField, confirmPasswordField;
    private Button signUpButton, backButton;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailField = findViewById(R.id.registeremail);
        passwordField = findViewById(R.id.registerpassword);
        confirmPasswordField = findViewById(R.id.registerrepassword);
        signUpButton = findViewById(R.id.registerbutsignup);
        backButton = findViewById(R.id.registerbutback);

        db = new DBHelper(this);

        signUpButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(SignupActivity.this, "Invalid email format! Please enter a valid email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.isUserExists(email)) {
                Toast.makeText(SignupActivity.this, "Email already exists! Please log in.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = db.insertUser(email, password, false);
            if (isInserted) {
                Toast.makeText(SignupActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });
    }

    // ✅ Email Validation Method
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}