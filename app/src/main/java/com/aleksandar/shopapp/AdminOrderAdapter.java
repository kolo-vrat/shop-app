package com.aleksandar.shopapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<Map.Entry<String, Order>> orders;

    public AdminOrderAdapter(Context context, ArrayList<Map.Entry<String, Order>> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public AdminOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.admin_order, parent, false);
        return new AdminOrderAdapter.MyViewHolder(itemView).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderAdapter.MyViewHolder holder, int position) {
        HashMap<String, String> products = (HashMap<String, String>) orders.get(position).getValue().getProducts();
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
        holder.tvName.setText(orders.get(position).getValue().getName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvItems;
        ImageButton ibSend;
        private AdminOrderAdapter adapter;
        private DatabaseReference mDatabase;
        private String DATABASE_URL;
        final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
        final private String contentType = "application/json";
        final private String NOTIFICATION_TITLE = "Success";
        final private String NOTIFICATION_MESSAGE = "Your order has been sent";
        private String serverKey = "key=";
        private String TOPIC;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_order);
            tvItems = itemView.findViewById(R.id.admin_order_items);
            ibSend = itemView.findViewById(R.id.send_order);
            serverKey += itemView.getContext().getString(R.string.api_key);
            ibSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map.Entry<String, Order> order = adapter.orders.get(getBindingAdapterPosition());
                    DATABASE_URL = itemView.getContext().getString(R.string.database_url);
                    mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference().child("orders");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                for (DataSnapshot forchild : child.getChildren()) {
                                    if (forchild.getKey().equals(order.getKey())) {
                                        DatabaseReference ref = forchild.getRef();
                                        ref.child("status").setValue("1");
                                        adapter.orders.remove(getBindingAdapterPosition());
                                        adapter.notifyItemRemoved(getBindingAdapterPosition());
                                        TOPIC = "/topics/"+child.getKey();
                                        JSONObject notification = new JSONObject();
                                        JSONObject notificationBody = new JSONObject();
                                        try {
                                            notificationBody.put("title", NOTIFICATION_TITLE);
                                            notificationBody.put("message", NOTIFICATION_MESSAGE);

                                            notification.put("to", TOPIC);
                                            notification.put("data", notificationBody);
                                        } catch (JSONException e) {
                                            Log.e("JSON_CREATE", " " + e.getMessage() );
                                        }
                                        sendNotification(notification, itemView);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }

        private void sendNotification(JSONObject notification, View view) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("RESPONSE", " " + response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(view.getContext(), "Request error", Toast.LENGTH_LONG).show();
                            Log.i("RESPONSE", "onErrorResponse: Didn't work");
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type", contentType);
                    return params;
                }
            };
            MySingleton.getInstance(view.getContext()).addToRequestQueue(jsonObjectRequest);
        }

        public AdminOrderAdapter.MyViewHolder linkAdapter(AdminOrderAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

    }
}
