package com.example.chargingpointsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private DBHelper dbHelper;
    private Spinner townSpinner;
    private ArrayList<String> townsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize Spinner
        townSpinner = findViewById(R.id.town_spinner);
        townsList = getTownsFromDatabase();
        populateTownSpinner();

        // Load the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.user_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Handle town selection
        townSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTown = townsList.get(position);
                updateMapForSelectedTown(selectedTown);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Populate the spinner with towns
    private void populateTownSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, townsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        townSpinner.setAdapter(adapter);
    }

    // Fetch unique towns from the database
    private ArrayList<String> getTownsFromDatabase() {
        ArrayList<String> towns = new ArrayList<>();
        Cursor cursor = dbHelper.getAllChargepoints();

        if (cursor.moveToFirst()) {
            do {
                String town = cursor.getString(cursor.getColumnIndexOrThrow("town"));
                if (!towns.contains(town)) {
                    towns.add(town);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return towns;
    }

    // Update map markers for the selected town
    private void updateMapForSelectedTown(String town) {
        if (gMap == null) return;

        gMap.clear(); // Clear existing markers

        LatLng location = null; // Location used to move the camera

        Cursor cursor = dbHelper.getAllChargepoints();
        if (cursor.moveToFirst()) {
            do {
                String dbTown = cursor.getString(cursor.getColumnIndexOrThrow("town"));
                if (dbTown.equals(town)) {
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                    String referenceID = cursor.getString(cursor.getColumnIndexOrThrow("referenceID"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("chargeDeviceStatus"));
                    String connectorType = cursor.getString(cursor.getColumnIndexOrThrow("connectorType"));

                    location = new LatLng(latitude, longitude);

                    // ✅ Add Marker with Info Window Data
                    Marker marker = gMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title("Chargepoint: " + referenceID)
                            .snippet("Status: " + status + "\nConnector Type: " + connectorType)
                    );

                    if (marker != null) {
                        marker.showInfoWindow(); // ✅ Show the info window immediately
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Move the camera to the first marker
        if (!town.isEmpty() && location != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12)); // Adjust default location as needed
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        Log.d("UserHomeActivity", "Map is ready");

        // ✅ Set Custom Info Window Adapter
        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null; // Use default Google Maps window style
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_map_info_box, null);

                // Find UI components
                TextView title = infoWindow.findViewById(R.id.info_title);
                TextView snippet = infoWindow.findViewById(R.id.info_snippet);

                // Set values
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // ✅ Set Marker Click Listener to Show Bottom Sheet
        gMap.setOnMarkerClickListener(marker -> {
            showPlaceInfoPopup(marker);
            return true; // Prevents default behavior
        });
    }

    private void showPlaceInfoPopup(Marker marker) {
        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_place_info, null);
        bottomSheetDialog.setContentView(popupView);

        // Set place details
        TextView placeName = popupView.findViewById(R.id.place_name);
        TextView placeInfo = popupView.findViewById(R.id.place_info);
        Button directionsButton = popupView.findViewById(R.id.directions_button);

        placeName.setText(marker.getTitle());
        placeInfo.setText(marker.getSnippet());

        // Open Google Maps for navigation
        directionsButton.setOnClickListener(v -> {
            LatLng position = marker.getPosition();
            String uri = "google.navigation:q=" + position.latitude + "," + position.longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        // Show the dialog
        bottomSheetDialog.show();
    }
}