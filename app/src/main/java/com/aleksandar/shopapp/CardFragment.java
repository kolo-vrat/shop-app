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

public class CardFragment extends Fragment {

    private EditText etName;
    private EditText etAddress;
    private EditText etCountry;
    private EditText etPhone;
    private EditText etCardNumber;
    private EditText etCVV;
    private EditText etMonth;
    private EditText etYear;
    private Button btnSubmit;

    public CardFragment() {
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
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = getView().findViewById(R.id.full_name_card);
        etAddress = getView().findViewById(R.id.address_card);
        etCountry = getView().findViewById(R.id.country_card);
        etPhone = getView().findViewById(R.id.phone_number_card);
        etCardNumber = getView().findViewById(R.id.card_number);
        etCVV = getView().findViewById(R.id.card_cvv);
        etMonth = getView().findViewById(R.id.card_month);
        etYear = getView().findViewById(R.id.card_year);
        btnSubmit = getView().findViewById(R.id.submit_details_card);
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
            etPhone.getText().toString().length() == 0 ||
            etCardNumber.getText().toString().length() == 0 ||
            etCVV.getText().toString().length() == 0 ||
            etMonth.getText().toString().length() == 0 ||
            etYear.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }
}