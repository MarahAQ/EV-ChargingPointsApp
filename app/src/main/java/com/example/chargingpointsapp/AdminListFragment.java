package com.example.chargingpointsapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminListFragment extends Fragment {

    private RecyclerView chargepointsRecyclerView;
    private ChargepointsRecyclerAdapter chargepointsRecyclerAdapter;
    private DBHelper dbHelper;
    private ArrayList<String> chargepoints;
    private TextView emptyView;

    public AdminListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_list, container, false);

        chargepointsRecyclerView = view.findViewById(R.id.chargepoints_recyclerview);
        emptyView = view.findViewById(R.id.empty_view);
        chargepointsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());
        chargepoints = new ArrayList<>();

        loadChargepoints();
        return view;
    }

    private void loadChargepoints() {
        chargepoints.clear();
        Cursor cursor = dbHelper.getAllChargepoints();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String referenceID = cursor.getString(cursor.getColumnIndexOrThrow("referenceID"));
                String town = cursor.getString(cursor.getColumnIndexOrThrow("town"));
                String postcode = cursor.getString(cursor.getColumnIndexOrThrow("postcode"));

                chargepoints.add(0, "Ref: " + referenceID + "\nTown: " + town + "\nPostcode: " + postcode);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (chargepoints.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            chargepointsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            chargepointsRecyclerView.setVisibility(View.VISIBLE);
        }

        chargepointsRecyclerAdapter = new ChargepointsRecyclerAdapter(chargepoints, dbHelper);
        chargepointsRecyclerView.setAdapter(chargepointsRecyclerAdapter);
    }

    // ✅ Ensure RecyclerView Updates When a New Chargepoint is Added
    public void addChargepointToTop(String referenceID, String town, String postcode) {
        if (chargepointsRecyclerAdapter != null) {
            String newChargepoint = "Ref: " + referenceID + "\nTown: " + town + "\nPostcode: " + postcode;

            chargepoints.add(0, newChargepoint);
            chargepointsRecyclerAdapter.notifyItemInserted(0);
            chargepointsRecyclerView.scrollToPosition(0);

            // ✅ Hide Empty View when a chargepoint is added
            emptyView.setVisibility(View.GONE);
            chargepointsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}