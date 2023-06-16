package com.example.myapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EditLoan extends Fragment {

    private EditText txtloanAmount;
    private Button btnupdateLoan;
    String chamaId,loanId;
    String[] loanRepayPeriod = {String.valueOf(30),String.valueOf(60)};
    AutoCompleteTextView autoComplete_txtPeriod;
    ArrayAdapter<String> adapterPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_loan, container, false);

        txtloanAmount= view.findViewById(R.id.loanAmount);
        btnupdateLoan=view.findViewById(R.id.editRequestLoan);
        autoComplete_txtPeriod= view.findViewById(R.id.autoComplete_txtPeriod);

        Bundle arguments= getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            loanId= arguments.getString("loanId");
        }

        fetchLoanData();

        adapterPeriod = new ArrayAdapter<>(getContext(), R.layout.list_gender, loanRepayPeriod);
        autoComplete_txtPeriod.setAdapter(adapterPeriod);
        autoComplete_txtPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loanperiod = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Loan Repayment Period:" + loanperiod, Toast.LENGTH_SHORT).show();
            }
        });

        btnupdateLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount= txtloanAmount.getText().toString().trim();
                String period= autoComplete_txtPeriod.getText().toString().trim();
                editLoan(amount,period);
            }
        });
        return view;
    }

    private void fetchLoanData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_LOAN+ "?loan_id=" + loanId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error==false) {
                        JSONObject meetingObject = jsonObject.getJSONObject("loan");

                        String lAmount = meetingObject.getString("loan_amount");
                        txtloanAmount.setText(lAmount);
                        String period = meetingObject.getString("loan_repayment_period");
                        autoComplete_txtPeriod.setText(period);

                    } else {
                        // User data retrieval failed
                        showToast("Error", true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error occurred: " + error.getMessage(), true);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void editLoan( String loanAmount, String loanPeriod) {

        if (TextUtils.isEmpty(loanAmount) || TextUtils.isEmpty(loanPeriod) ) {
            showToast("Please fill in all the fields",true);
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT_LOAN+ "?loan_id=" + loanId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");
                    if (error==false) {
                        showToast(message, false);
                    } else {
                        showToast(message, true); // Show error message with red background color
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error occurred: " + e.getMessage(), true); // Show error message with red background color
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error occurred: " + error.getMessage(), true); // Show error message with red background color
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the request parameters
                Map<String, String> params = new HashMap<>();
                params.put("loanAmount", loanAmount);
                params.put("loanPeriod", loanPeriod);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, getView().findViewById(R.id.toast_message));
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, getView().findViewById(R.id.toast_message));
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}