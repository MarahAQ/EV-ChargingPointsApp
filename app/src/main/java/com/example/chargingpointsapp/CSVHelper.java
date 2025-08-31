package com.example.chargingpointsapp;

import android.content.Context;

import com.example.chargingpointsapp.DBHelper;
import com.example.chargingpointsapp.R;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;

public class CSVHelper {

    public static void loadChargepointsFromCSV(Context context, DBHelper dbHelper) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(context.getResources().openRawResource(R.raw.sample_national_chargepoints)));
            String[] nextLine;

            // Skip the header row
            reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                String referenceID = nextLine[0];
                double latitude = Double.parseDouble(nextLine[1]);
                double longitude = Double.parseDouble(nextLine[2]);
                String town = nextLine[3];
                String county = nextLine[4];
                String postcode = nextLine[5];
                String status = nextLine[6];
                String connectorType = nextLine[7];

                // Insert into database
                dbHelper.insertChargepoint(referenceID, latitude, longitude, town, county, postcode, status, "", connectorType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}