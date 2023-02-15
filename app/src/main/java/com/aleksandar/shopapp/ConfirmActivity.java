package com.aleksandar.shopapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity {

    private FirebaseUser mAuth;
    private DatabaseReference mDatabase;
    private TextView orderStatus;
    private Button btnConfirm;
    private String userId;
    private String DATABASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle bundle = getIntent().getExtras();

        orderStatus = (TextView) findViewById(R.id.order_status);
        btnConfirm = (Button) findViewById(R.id.confirm_order_ok);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        userId = mAuth.getUid();
        DATABASE_URL = this.getString(R.string.database_url);
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
        Map<String, String> products = new HashMap<>();
        addProductData(products);
        Order order = new Order(
                bundle.getString("name"),
                bundle.getString("address"),
                bundle.getString("country"),
                bundle.getString("phone"),
                "0",
                products
        );
        placeOrder(order);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    public void addProductData(Map<String, String> products) {
        int counter = 0;
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.cart_products),
                Context.MODE_PRIVATE);
        HashSet<String> cartProducts = (HashSet<String>)sharedPref.getStringSet(
                "cartProducts",
                new HashSet<String>());
        for (String s : cartProducts) {
            products.put("ID"+Integer.toString(counter++), s);
        }
    }

    private void placeOrder(Order order) {
        mDatabase.child("orders").child(userId).push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    orderStatus.setText("Success");
                    orderStatus.setTextColor(getResources().getColor(R.color.light_green));
                    SharedPreferences sharedPref = ConfirmActivity.this.getSharedPreferences(
                            ConfirmActivity.this.getString(R.string.cart_products),
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putStringSet("cartProducts", new HashSet<String>());
                    editor.apply();
                } else {
                    orderStatus.setText("Failed");
                    orderStatus.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });
    }
}