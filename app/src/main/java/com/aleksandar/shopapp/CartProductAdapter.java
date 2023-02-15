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

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> cart;

    public CartProductAdapter(Context context, ArrayList<String> cart) {
        this.context = context;
        this.cart = cart;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_product, parent, false);
        return new CartProductAdapter.MyViewHolder(itemView).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String data = cart.get(position);
        String[] titlePrice = data.split(",");
        holder.title.setText(titlePrice[0]);
        holder.price.setText(titlePrice[1]);
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView price;
        Button btnRemove;
        private CartProductAdapter adapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_cp);
            price = itemView.findViewById(R.id.price_cp);
            btnRemove = itemView.findViewById(R.id.remove_cp);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.cart.remove(getBindingAdapterPosition());
                    adapter.notifyItemRemoved(getBindingAdapterPosition());
                    SharedPreferences sharedPref = itemView.getContext().getSharedPreferences(
                            itemView.getContext().getString(R.string.cart_products),
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    HashSet<String> cartProducts = (HashSet<String>)sharedPref.getStringSet(
                            "cartProducts",
                            new HashSet<String>());
                    cartProducts.remove(title.getText().toString()+","+price.getText().toString());
                    editor.putStringSet("cartProducts", (Set<String>)cartProducts);
                    editor.apply();
                }
            });
        }

        public MyViewHolder linkAdapter(CartProductAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }


}
