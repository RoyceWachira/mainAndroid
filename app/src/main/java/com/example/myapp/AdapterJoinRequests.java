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

public class AdapterJoinRequests extends RecyclerView.Adapter<AdapterJoinRequests.MyViewHolder>{
    private List<Requests> requestsList;
    private Context context;

    public AdapterJoinRequests(List<Requests>requestsList,Context context){
        this.requestsList=requestsList;
        this.context=context;
    }

    public void setRequestsList(List<Requests> requestsList) {
        this.requestsList = requestsList;

    }

    @Override
    public AdapterJoinRequests.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.joinrequests,parent,false);
        return new AdapterJoinRequests.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterJoinRequests.MyViewHolder holder, int position) {
        Requests requests = requestsList.get(position);

        holder.txtreqId.setText(requests.getRequestId());
        holder.txtreqStatus.setText(requests.getJoinStatus());
        holder.txtreqAt.setText(requests.getRequestedAt());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveLoan(holder.getAdapterPosition());
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectLoan(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = requestsList.size();
        Log.d("Adapter", "Requests List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView requests;
        TextView txtreqId,txtreqStatus,txtreqAt;
        Button btnApprove,btnReject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnApprove= itemView.findViewById(R.id.approveReq);
            btnReject= itemView.findViewById(R.id.rejectReq);
            txtreqAt= itemView.findViewById(R.id.txtRequestAt);
            txtreqId= itemView.findViewById(R.id.txtRequestId);
            txtreqStatus= itemView.findViewById(R.id.txtjoinStatus);
            requests= itemView.findViewById(R.id.requests);
        }



    }

    private void approveLoan(int position) {
        String requestId = requestsList.get(position).getRequestId();
        String chamaId = requestsList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ACCEPT_REQUEST+ "?chama_id=" + chamaId + "&user_id=" + userId + "&request_id=" + requestId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chama_id", chamaId);
                params.put("request_id", requestId);
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void rejectLoan(int position) {
        String requestId = requestsList.get(position).getRequestId();
        String chamaId = requestsList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REJECT_REQUEST+ "?chama_id=" + chamaId + "&user_id=" + userId + "&request_id=" + requestId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chama_id", chamaId);
                params.put("request_id", requestId);
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

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
}



