package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
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

public class AdapterAllFines extends RecyclerView.Adapter<AdapterAllFines.MyViewHolder>{
    private List<Fines> finesList;
    private Context context;

    public AdapterAllFines(List<Fines>finesList,Context context){
        this.finesList=finesList;
        this.context=context;
    }

    public void setFinesList(List<Fines> finesList) {
        this.finesList = finesList;

    }

    @Override
    public AdapterAllFines.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fines,parent,false);
        return new AdapterAllFines.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAllFines.MyViewHolder holder, int position) {
        Fines fines = finesList.get(position);

        holder.bind(fines);
        holder.txtFineId.setText(fines.getFineId());
        holder.txtFineStatus.setText(fines.getFineStatus());
        holder.txtFineReason.setText(fines.getFineReason());
        holder.txtFineAmount.setText(fines.getFineAmount());
        holder.txtDate.setText(fines.getDateFined());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFine(holder.getAdapterPosition());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFine editFine = new EditFine();
                Bundle bundle = new Bundle();
                bundle.putString("fineId", fines.getFineId());
                editFine.setArguments(bundle);
                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, editFine)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        int itemCount = finesList.size();
        Log.d("Adapter", "Fines List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualFines;
        TextView txtFineId,txtFineStatus,txtFineAmount,txtFineReason,txtDate;
        Button edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            edit= itemView.findViewById(R.id.editButton);
            delete= itemView.findViewById(R.id.deleteButton);
            txtFineAmount= itemView.findViewById(R.id.txtFineAmount);
            txtFineReason= itemView.findViewById(R.id.txtFineReason);
            cardIndividualFines=itemView.findViewById(R.id.cardIndividualFine);
            txtFineId= itemView.findViewById(R.id.txtFineId);
            txtFineStatus= itemView.findViewById(R.id.txtFineStatus);
            txtDate= itemView.findViewById(R.id.txtDateFined);
        }
        public void bind(Fines fines) {
            if (fines.getFineStatus().equals("Not Paid") ) {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            } else {
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }

        }
    }

    private void deleteFine(int position) {
        String fineId = finesList.get(position).getFineId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_FINE+ "?fine_id=" + fineId , new Response.Listener<String>() {
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
                params.put("fine_id", fineId);
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



