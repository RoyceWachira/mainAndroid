package com.example.myapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class NewWithdrawal extends Fragment {

    private EditText withdrawReason,withdrawAmount;
    private Button btnmakeWithdrawal;
    String chamaId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_withdrawal, container, false);

        withdrawAmount= view.findViewById(R.id.withdrawalAmount);
        withdrawReason= view.findViewById(R.id.withdrawalReason);
        btnmakeWithdrawal=view.findViewById(R.id.btnWithdrawal);

        Bundle arguments= getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        btnmakeWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeWithdrawal();
            }
        });
        return view;
    }





    private void makeWithdrawal() {
        final String withdrawalAmount = withdrawAmount.getText().toString().trim();
        final String withdrawalReason = withdrawReason.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(withdrawalAmount) || TextUtils.isEmpty(withdrawalReason) ) {
            showToast("Please fill in all the fields", true);
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Withdraw");
        builder.setMessage("Are you sure you want to withdraw sh." + withdrawalAmount + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = null;
                userId = SharedPrefManager.getInstance(getContext()).getUserId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MAKE_WITHDRAWAL + "?user_id=" + userId + "&chama_id=" + chamaId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error) {
                                showToast(message, true);
                            } else {

                                JSONObject messageObj = jsonObject.getJSONObject("message");
                                int withdrawalId = messageObj.getInt("withdrawal_id");
                                int chamaId = messageObj.getInt("chama_id");
                                String withdrawalReason= messageObj.getString("withdrawal_reason");
                                String withdrawalAmount= messageObj.getString("withdrawal_amount");
                                showToast("Withdrawal Successful", false);

                                // Pass the contribution ID to the next fragment
                                CompleteWithdrawal completeWithdrawal = new CompleteWithdrawal();
                                Bundle bundle = new Bundle();
                                bundle.putInt("chama_id",chamaId);
                                bundle.putInt("withdrawalId", withdrawalId);
                                bundle.putString("withdrawalReason",withdrawalReason);
                                bundle.putString("withdrawalAmount",withdrawalAmount);
                                completeWithdrawal.setArguments(bundle);

                                // Replace the current fragment with the next fragment
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.chamaFrameLayout, completeWithdrawal);
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
                        params.put("withdrawalAmount", withdrawalAmount);
                        params.put("withdrawalReason", withdrawalReason);
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