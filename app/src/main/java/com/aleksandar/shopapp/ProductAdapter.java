package com.aleksandar.shopapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product> productData;

    public ProductAdapter(Context context, ArrayList<Product> productData) {
        this.context = context;
        this.productData = productData;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView category;
        TextView price;
        Button addToCart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title_rv);
            category = itemView.findViewById(R.id.product_category_rv);
            price = itemView.findViewById(R.id.product_price_rv);
            addToCart = itemView.findViewById(R.id.add_to_cart);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        holder.title.setText(productData.get(position).getTitle());
        holder.category.setText(productData.get(position).getCategory());
        holder.price.setText(Float.toString(productData.get(position).getPrice()));
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.cart_products),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                HashSet<String> cartProducts = (HashSet<String>)sharedPref.getStringSet(
                        "cartProducts",
                        new HashSet<String>());
                cartProducts.add(productData.get(position).getTitle()+","+Float.toString(productData.get(position).getPrice()));
                editor.putStringSet("cartProducts", (Set<String>)cartProducts);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

}
