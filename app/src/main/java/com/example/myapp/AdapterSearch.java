package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> {

    private List<Chamas> chamasList;
    private Context context;

    public AdapterSearch(List<Chamas>chamasList,Context context){
        this.chamasList=chamasList;
        this.context=context;
    }

    public void setChamasList(List<Chamas> chamasList) {
        this.chamasList = chamasList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chamas,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.chamaName.setText(chamasList.get(position).getChamaName());
        holder.chamaDescription.setText(chamasList.get(position).getChamaDescription());

        holder.btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    requestToJoinChama(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = chamasList.size();
        Log.d("AdapterSearch", "Chamas List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView chamaName,chamaDescription;
        Button btnRequest;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRequest= itemView.findViewById(R.id.requestButton);
            cardView=itemView.findViewById(R.id.cardJoinChama);
            chamaName= itemView.findViewById(R.id.chamaName);
            chamaDescription= itemView.findViewById(R.id.chamaDescription);
        }
    }

    private void requestToJoinChama(int position) {
        String chamaId = chamasList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REQUEST_JOIN_CHAMA+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error==false) {
                                sendJoinChamaNotification(position);
                                showToast(message,false);
                            } else {
                                showToast(message,true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error Occured",true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(),true);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chamaId", String.valueOf(chamaId));
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, null);
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, null);
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void sendJoinChamaNotification(int position) {
        String chamaId = chamasList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        String notificationContent = "User " + userId + " has requested to join Chama " + chamaId;
        String url = Constants.URL_REQUEST_JOIN_CHAMA+ "?chama_id=" + chamaId + "&user_id=" + userId;

        // Create an instance of NotificationSender
        NotificationSender notificationSender = new NotificationSender();

        // Call the sendNotification method
        notificationSender.sendNotification(context,String.valueOf(userId), notificationContent, url);


    }


}
