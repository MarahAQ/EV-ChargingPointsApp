package com.example.chargingpointsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminHomeActivity extends AppCompatActivity {

    private Button viewListButton, addChargepointButton, logoutButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        viewListButton = findViewById(R.id.button_view_list);
        addChargepointButton = findViewById(R.id.button_add_chargepoint);
        logoutButton = findViewById(R.id.button_logout);

        dbHelper = new DBHelper(this);

        Cursor cursor = dbHelper.getAllChargepoints();
        if (cursor == null || cursor.getCount() == 0) {
            System.out.println("Database is EMPTY, inserting sample data...");
            dbHelper.insertSampleData();
        }

        viewListButton.setOnClickListener(v -> loadFragment(new AdminListFragment(), "ADMIN_LIST_FRAGMENT"));
        addChargepointButton.setOnClickListener(v -> loadFragment(new AddChargepointFragment(), "ADD_CHARGEPOINT_FRAGMENT"));
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_admin, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
        System.out.println("Loading Fragment: " + tag);
    }
}