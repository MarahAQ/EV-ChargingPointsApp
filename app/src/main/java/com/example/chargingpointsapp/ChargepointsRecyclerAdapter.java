package com.example.chargingpointsapp;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChargepointsRecyclerAdapter extends RecyclerView.Adapter<ChargepointsRecyclerAdapter.ViewHolder> {

    private final ArrayList<String> chargepointsList;
    private final DBHelper dbHelper;

    // ✅ Adapter Constructor
    public ChargepointsRecyclerAdapter(ArrayList<String> chargepointsList, DBHelper dbHelper) {
        this.chargepointsList = chargepointsList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_with_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String chargepoint = chargepointsList.get(position);
        holder.chargepointInfo.setText(chargepoint);

        // ✅ Extract reference ID safely
        final String[] lines = chargepoint.split("\n"); // Use final array
        final String referenceID;
        if (lines.length > 0 && lines[0].contains(":")) {
            referenceID = lines[0].split(":")[1].trim();
        } else {
            referenceID = ""; // Handle invalid format
        }

        // ✅ Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this chargepoint?\n\n" + chargepoint)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        boolean isDeleted = dbHelper.deleteChargepointByReferenceID(referenceID);
                        if (isDeleted) {
                            chargepointsList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, chargepointsList.size()); // ✅ Ensure smooth UI update
                            Toast.makeText(v.getContext(), "Chargepoint deleted!", Toast.LENGTH_SHORT).show();
                            System.out.println("Deleted Chargepoint: RefID=" + referenceID);
                        } else {
                            Toast.makeText(v.getContext(), "Failed to delete chargepoint!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return chargepointsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chargepointInfo;
        Button deleteButton;

        ViewHolder(View view) {
            super(view);
            chargepointInfo = view.findViewById(R.id.chargepoint_info);
            deleteButton = view.findViewById(R.id.delete_button);
        }
    }
}