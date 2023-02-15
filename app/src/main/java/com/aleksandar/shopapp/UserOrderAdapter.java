package com.aleksandar.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<Order> orders;

    public UserOrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public UserOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_order, parent, false);
        return new UserOrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> products = (HashMap<String, String>) orders.get(position).getProducts();
        String status = orders.get(position).getStatus();
        String[] itemsArray = new String[products.values().size()];
        products.values().toArray(itemsArray);
        String[] withoutPrice = new String[itemsArray.length];
        int i = 0;
        for (String s : itemsArray) {
            String[] words = s.split(",");
            withoutPrice[i++] = words[0];
        }
        String items = String.join(", ", withoutPrice);
        holder.tvItems.setText(items);
        if (status.equals("0")) {
            holder.ivStatus.setImageResource(R.drawable.bad);
        } else {
            holder.ivStatus.setImageResource(R.drawable.check);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivStatus;
        TextView tvItems;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStatus = itemView.findViewById(R.id.status_icon);
            tvItems = itemView.findViewById(R.id.order_items);
        }
    }
}
