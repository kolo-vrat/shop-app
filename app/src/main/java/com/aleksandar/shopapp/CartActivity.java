package com.aleksandar.shopapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashSet;

public class CartActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private RecyclerView rvCart;
    private Button btnCheckout;
    private ArrayList<String> listProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listProducts = new ArrayList<>();
        addProductData(listProducts);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.cart);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(CartActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(CartActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cart:
                        return true;
                }
                return false;
            }
        });

        rvCart = findViewById(R.id.cart_rv);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        CartProductAdapter adapter = new CartProductAdapter(this, listProducts);
        rvCart.setAdapter(adapter);

        btnCheckout = findViewById(R.id.cart_checkout);
        if (listProducts.size() == 0) {
            btnCheckout.setVisibility(View.GONE);
        } else {
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                    finish();
                }
            });
        }
    }

    public void addProductData(ArrayList<String> listProducts) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.cart_products),
                Context.MODE_PRIVATE);
        HashSet<String> cartProducts = (HashSet<String>)sharedPref.getStringSet(
                "cartProducts",
                new HashSet<String>());
        for (String s : cartProducts) {
            listProducts.add(s);
        }
    }
}