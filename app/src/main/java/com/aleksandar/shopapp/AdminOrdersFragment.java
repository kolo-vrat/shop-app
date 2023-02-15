package com.aleksandar.shopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AdminOrdersFragment extends Fragment {

    private DatabaseReference mDatabase;
    private ArrayList<Map.Entry<String, Order>> orders;
    private RecyclerView rvOrders;
    private String DATABASE_URL;

    public AdminOrdersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);
        DATABASE_URL = getActivity().getString(R.string.database_url);
        rvOrders = view.findViewById(R.id.admin_orders_rv);
        orders = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
        rvOrders.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getUserOrders(orders, view);

        return view;
    }

    private void getUserOrders(ArrayList<Map.Entry<String, Order>> orders, View view) {
        mDatabase.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    for (DataSnapshot anotherChild: child.getChildren()) {
                        orders.add(Map.entry(anotherChild.getKey(), anotherChild.getValue(Order.class)));
                    }
                }
                ArrayList<Map.Entry<String, Order>> newOrders = new ArrayList<>();
                for (int i = 0; i<orders.size(); i++) {
                    if (orders.get(i).getValue().getStatus().equals("0")) {
                        newOrders.add(orders.get(i));
                    }
                }
                AdminOrderAdapter adapter = new AdminOrderAdapter(view.getContext(), newOrders);
                rvOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}