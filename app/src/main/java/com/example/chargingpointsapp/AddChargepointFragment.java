package com.example.chargingpointsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddChargepointFragment extends Fragment {

    private EditText referenceIdInput, townInput, postcodeInput;
    private Button addChargepointButton;
    private DBHelper dbHelper;

    public AddChargepointFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_chargepoint, container, false);

        referenceIdInput = view.findViewById(R.id.input_reference_id);
        townInput = view.findViewById(R.id.input_town);
        postcodeInput = view.findViewById(R.id.input_postcode);
        addChargepointButton = view.findViewById(R.id.button_add_chargepoint);
        dbHelper = new DBHelper(requireContext());

        addChargepointButton.setOnClickListener(v -> {
            String referenceId = referenceIdInput.getText().toString().trim();
            String town = townInput.getText().toString().trim();
            String postcode = postcodeInput.getText().toString().trim();

            if (referenceId.isEmpty() || town.isEmpty() || postcode.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = dbHelper.insertChargepoint(referenceId, 0, 0, town, "", postcode, "Available", "", "Type 2");

            if (isInserted) {
                Toast.makeText(getContext(), "Chargepoint added successfully!", Toast.LENGTH_SHORT).show();
                System.out.println("Chargepoint Added: RefID=" + referenceId + ", Town=" + town + ", Postcode=" + postcode);

                // ✅ Update AdminListFragment dynamically
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("ADMIN_LIST_FRAGMENT");

                if (fragment instanceof AdminListFragment) {
                    AdminListFragment adminListFragment = (AdminListFragment) fragment;
                    adminListFragment.addChargepointToTop(referenceId, town, postcode);
                    System.out.println("AdminListFragment updated with new chargepoint.");
                }

                // ✅ Clear input fields after adding a chargepoint
                referenceIdInput.setText("");
                townInput.setText("");
                postcodeInput.setText("");

                // ✅ Navigate back to AdminListFragment
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Failed to add chargepoint!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}