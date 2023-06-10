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

public class AdapterLoanRequests extends RecyclerView.Adapter<AdapterLoanRequests.MyViewHolder>{
    private List<Loans> loansList;
    private Context context;

    public AdapterLoanRequests(List<Loans>loansList,Context context){
        this.loansList=loansList;
        this.context=context;
    }

    public void setLoansList(List<Loans> loansList) {
        this.loansList = loansList;

    }

    @Override
    public AdapterLoanRequests.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loanrequests,parent,false);
        return new AdapterLoanRequests.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterLoanRequests.MyViewHolder holder, int position) {
        Loans loans = loansList.get(position);

        holder.txtLoanId.setText(loans.getLoanId());
        holder.txtLoanStatus.setText(loans.getLoanStatus());
        holder.txtLoanAmount.setText(loans.getLoanAmount());
        holder.txtAmountPayable.setText(loans.getAmountPayable());
        holder.txtDueAt.setText(loans.getDueAt());

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
        int itemCount = loansList.size();
        Log.d("Adapter", "Loans List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardIndividualLoan;
        TextView txtLoanId,txtLoanStatus,txtAmountPayable,txtLoanAmount,txtDueAt;
        Button btnApprove,btnReject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnApprove= itemView.findViewById(R.id.approve);
            btnReject= itemView.findViewById(R.id.reject);
            txtAmountPayable= itemView.findViewById(R.id.txtAmountPayable);
            txtLoanAmount= itemView.findViewById(R.id.txtloanAmount);
            txtDueAt= itemView.findViewById(R.id.txtloanDueAt);
            cardIndividualLoan=itemView.findViewById(R.id.cardIndividualLoan);
            txtLoanId= itemView.findViewById(R.id.txtLoanId);
            txtLoanStatus= itemView.findViewById(R.id.txtloanStatus);
        }



    }

    private void approveLoan(int position) {
        String loanId = loansList.get(position).getLoanId();
        String chamaId = loansList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_APPROVE_LOAN+ "?chama_id=" + chamaId + "&user_id=" + userId + "&loan_id=" + loanId, new Response.Listener<String>() {
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
                params.put("loan_id", loanId);
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void rejectLoan(int position) {
        String loanId = loansList.get(position).getLoanId();
        String chamaId = loansList.get(position).getChamaId();
        int userId = Integer.parseInt(SharedPrefManager.getInstance(context).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REJECT_LOAN+ "?chama_id=" + chamaId + "&user_id=" + userId + "&loan_id=" + loanId, new Response.Listener<String>() {
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
                params.put("loan_id", loanId);
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



