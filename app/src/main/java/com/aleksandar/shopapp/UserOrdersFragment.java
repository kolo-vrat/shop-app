package com.aleksandar.shopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOrdersFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<Order> orders;
    private RecyclerView rvOrders;
    private String DATABASE_URL;
    private String userId;

    public UserOrdersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_orders, container, false);
        DATABASE_URL = getActivity().getString(R.string.database_url);
        rvOrders = view.findViewById(R.id.user_orders_rv);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        orders = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
        rvOrders.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getUserOrders(orders, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getUserOrders(ArrayList<Order> orders, View view) {
        mDatabase.child("orders").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> allOrders = (Map<String, Object>) snapshot.getValue();
                if (allOrders != null) {
                    for (Map.Entry<String, Object> entry : allOrders.entrySet()) {
                        Order singleOrder = Order.toOrder((HashMap<String, Object>) entry.getValue());
                        orders.add(singleOrder);
                    }
                    UserOrderAdapter adapter = new UserOrderAdapter(view.getContext(), orders);
                    rvOrders.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}