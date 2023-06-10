package com.example.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


public class NewLoan extends Fragment {

    private EditText txtloanAmount;
    private Button buttonRequestLoan;
    String chamaId,chamaFlow,chamaName;
    String[] loanRepayPeriod = {String.valueOf(30),String.valueOf(60)};
    AutoCompleteTextView autoComplete_txtPeriod;
    ArrayAdapter<String> adapterPeriod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_loan, container, false);

        txtloanAmount= view.findViewById(R.id.loanAmount);
        buttonRequestLoan=view.findViewById(R.id.btnRequestLoan);
        autoComplete_txtPeriod= view.findViewById(R.id.autoComplete_txtPeriod);

        Bundle arguments= getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
        }

        adapterPeriod = new ArrayAdapter<>(getContext(), R.layout.list_gender, loanRepayPeriod);
        autoComplete_txtPeriod.setAdapter(adapterPeriod);
        autoComplete_txtPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loanperiod = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Loan Repayment Period:" + loanperiod, Toast.LENGTH_SHORT).show();
            }
        });

        buttonRequestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLoan();
            }
        });
        return view;
    }





    private void requestLoan() {
        final String loanAmount = txtloanAmount.getText().toString().trim();
        final String loanPeriod = autoComplete_txtPeriod.getText().toString().trim();
        // Check if all fields are filled
        if (TextUtils.isEmpty(loanAmount) || TextUtils.isEmpty(loanPeriod) ) {
            showToast("Please fill in all the fields", true);
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Request Loan");
        builder.setMessage("Are you sure you want to request for sh." + loanAmount + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = null;
                userId = SharedPrefManager.getInstance(getContext()).getUserId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REQUEST_LOAN + "?user_id=" + userId + "&chama_id=" + chamaId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error) {
                                showToast(message, true);
                            } else {

                                showToast(message, false);

                                // Pass the contribution ID to the next fragment
                                ChamaHomeFragment chamaHomeFragment = new ChamaHomeFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("chamaId",chamaId);
                                bundle.putString("chamaName",chamaName);
                                bundle.putString("chamaFlow",chamaFlow);
                                chamaHomeFragment.setArguments(bundle);

                                // Replace the current fragment with the next fragment
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.chamaFrameLayout, chamaHomeFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast("Error Occurred: " + e.getMessage(), true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e("Error", "Error occurred", error);
                        showToast("Error occurred: " + errorMessage, true);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("loanAmount", loanAmount);
                        params.put("loanRepayPeriod", loanPeriod);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
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