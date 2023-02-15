package com.aleksandar.shopapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private String DATABASE_URL;
    private BottomNavigationView bottomNavigation;
    private DatabaseReference mDatabase;
    private RecyclerView rvBooks;
    private RecyclerView rvMovies;
    private ArrayList<Product> booksList;
    private ArrayList<Product> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        booksList = new ArrayList<>();
        moviesList = new ArrayList<>();
        DATABASE_URL = this.getString(R.string.database_url);
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
        rvBooks = (RecyclerView) findViewById(R.id.books_rv);
        rvMovies = (RecyclerView) findViewById(R.id.movies_rv);
        rvBooks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.home);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.cart:
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

        mDatabase.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectProductData((Map<String, Object>) snapshot.getValue());
                ProductAdapter booksAdapter = new ProductAdapter(HomeActivity.this, booksList);
                ProductAdapter moviesAdapter = new ProductAdapter(HomeActivity.this, moviesList);
                rvBooks.setAdapter(booksAdapter);
                rvMovies.setAdapter(moviesAdapter);
                booksAdapter.notifyDataSetChanged();
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void collectProductData(Map<String, Object> products) {
        for(Map.Entry<String, Object> entry : products.entrySet()) {
            Map singleProduct = (Map) entry.getValue();
            if (((String)singleProduct.get("category")).equals("book")) {
                booksList.add(new Product((String) singleProduct.get("title"),
                        ((Long) singleProduct.get("price")).floatValue(),
                        (String) singleProduct.get("category")));
            } else {
                moviesList.add(new Product((String) singleProduct.get("title"),
                        ((Long) singleProduct.get("price")).floatValue(),
                        (String) singleProduct.get("category")));
            }
        }
    }
}