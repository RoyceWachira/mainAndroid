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

public class AdapterIndividualLoans extends RecyclerView.Adapter<AdapterIndividualLoans.MyViewHolder>{
    private List<Loans> loansList;
    private Context context;

    public AdapterIndividualLoans(List<Loans>loansList,Context context){
        this.loansList=loansList;
        this.context=context;
    }

    public void setLoansList(List<Loans> loansList) {
        this.loansList = loansList;

    }

    @Override
    public AdapterIndividualLoans.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.loans,parent,false);
        return new AdapterIndividualLoans.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterIndividualLoans.MyViewHolder holder, int position) {
        Loans loans = loansList.get(position);

        holder.bind(loans);
        holder.updel(loans);
        holder.txtLoanId.setText(loans.getLoanId());
        holder.txtLoanStatus.setText(loans.getLoanStatus());
        holder.txtLoanAmount.setText(loans.getLoanAmount());
        holder.txtAmountPayable.setText(loans.getAmountPayable());
        holder.txtDueAt.setText(loans.getDueAt());

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayLoanFragment payLoanFragment = new PayLoanFragment();
                Bundle bundle = new Bundle();
                bundle.putString("loanId", loans.getLoanId());
                bundle.putString("chamaId", loans.getChamaId());
                payLoanFragment.setArguments(bundle);
                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, payLoanFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLoan(holder.getAdapterPosition());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditLoan editLoan = new EditLoan();
                Bundle bundle = new Bundle();
                bundle.putString("loanId", loans.getLoanId());
                bundle.putString("chamaId", loans.getChamaId());
                editLoan.setArguments(bundle);
                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, editLoan)
                        .addToBackStack(null)
                        .commit();
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
        Button pay,edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            edit= itemView.findViewById(R.id.editButton);
            delete= itemView.findViewById(R.id.deleteButton);
            pay= itemView.findViewById(R.id.btnPay);
            txtAmountPayable= itemView.findViewById(R.id.txtAmountPayable);
            txtLoanAmount= itemView.findViewById(R.id.txtloanAmount);
            txtDueAt= itemView.findViewById(R.id.txtloanDueAt);
            cardIndividualLoan=itemView.findViewById(R.id.cardIndividualLoan);
            txtLoanId= itemView.findViewById(R.id.txtLoanId);
            txtLoanStatus= itemView.findViewById(R.id.txtloanStatus);
        }
        public void bind(Loans loans) {
            if (loans.getLoanStatus().equals("verified") ) {
                pay.setVisibility(View.VISIBLE);
            } else {
                pay.setVisibility(View.GONE);
            }

        }

        public void updel(Loans loans) {
            if (loans.getLoanStatus().equals("pending")) {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            } else {
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }
    }

    private void deleteLoan(int position) {
        String loanId = loansList.get(position).getLoanId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_LOAN+ "?loan_id=" + loanId , new Response.Listener<String>() {
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
                params.put("loan_id", loanId);
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



