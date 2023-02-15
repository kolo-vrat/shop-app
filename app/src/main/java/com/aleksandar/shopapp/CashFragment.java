package com.aleksandar.shopapp;

import android.content.Intent;
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

public class CashFragment extends Fragment {

    private EditText etName;
    private EditText etAddress;
    private EditText etCountry;
    private EditText etPhone;
    private Button btnSubmit;

    public CashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = getView().findViewById(R.id.full_name);
        etAddress = getView().findViewById(R.id.address);
        etCountry = getView().findViewById(R.id.country);
        etPhone = getView().findViewById(R.id.phone_number);
        btnSubmit = getView().findViewById(R.id.submit_details_cash);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmActivity.class);
                    intent.putExtra("name", etName.getText().toString());
                    intent.putExtra("address", etAddress.getText().toString());
                    intent.putExtra("country", etCountry.getText().toString());
                    intent.putExtra("phone", etPhone.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkInput() {
        if (etName.getText().toString().length() == 0 ||
            etAddress.getText().toString().length() == 0 ||
            etCountry.getText().toString().length() == 0 ||
            etPhone.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }
}