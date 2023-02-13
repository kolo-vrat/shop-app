package com.aleksandar.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnAddProduct;
    private EditText inTitle;
    private EditText inPrice;
    private RadioGroup rgCategory;
    private String productTitle;
    private String category;
    private float price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        btnCancel = (Button) findViewById(R.id.cancel_add);
        btnAddProduct = (Button) findViewById(R.id.add_product);
        inTitle = (EditText) findViewById(R.id.product_title);
        inPrice = (EditText) findViewById(R.id.product_price);
        rgCategory = (RadioGroup) findViewById(R.id.choose_category);
        category = null;

        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_book:
                        category = "book";
                    case R.id.radio_movie:
                        category = "movie";
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProductActivity.this, AdminActivity.class));
                finish();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTitle = inTitle.getText().toString();
                try {
                    price = Float.parseFloat(inPrice.getText().toString());
                } catch (NumberFormatException exc) {
                    Toast.makeText(AddProductActivity.this, "Product price field must not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (productTitle.length() == 0) {
                    Toast.makeText(AddProductActivity.this, "Product title field must not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (category == null) {
                    Toast.makeText(AddProductActivity.this, "Please choose product category", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}