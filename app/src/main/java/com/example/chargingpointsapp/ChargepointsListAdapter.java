package com.example.chargingpointsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChargepointsListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> chargepointsList;
    private final DBHelper dbHelper;

    public ChargepointsListAdapter(Context context, ArrayList<String> chargepointsList, DBHelper dbHelper) {
        super(context, R.layout.list_item_with_delete, chargepointsList);
        this.context = context;
        this.chargepointsList = chargepointsList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_with_delete, parent, false);
        }

        TextView chargepointInfo = convertView.findViewById(R.id.chargepoint_info);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        String chargepoint = chargepointsList.get(position);
        chargepointInfo.setText(chargepoint);

        // Extract referenceID for deletion
        String referenceID = chargepoint.split(":")[1].split("\n")[0].trim();

        deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog before deletion
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this chargepoint?\n\n" + chargepoint)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        boolean isDeleted = dbHelper.deleteChargepointByReferenceID(referenceID);
                        if (isDeleted) {
                            Toast.makeText(context, "Chargepoint deleted successfully!", Toast.LENGTH_SHORT).show();
                            chargepointsList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Failed to delete chargepoint!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        return convertView;
    }
}
