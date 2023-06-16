package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
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


public class PayFineFragment extends Fragment {

    private EditText payAmount;
    private Button pay;
    String chamaId,fineId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_fine, container, false);

        payAmount = view.findViewById(R.id.finePayAmount);
        pay= view.findViewById(R.id.payFine);

        Bundle arguments = getArguments();
        if (arguments != null) {
            fineId= arguments.getString("fineId");

        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payFine();
            }
        });
        return view;
    }

    private void payFine() {
        final String payamount = payAmount.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(payamount)) {
            showToast("Please fill in all the fields", true);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pay Loan");
        builder.setMessage("Are you sure you want to pay sh." + payamount + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PAY_FINE + "?fine_id=" + fineId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");
                            if(error) {
                                showToast(message,true);
                            }else {
                                sendNotification();

                                showToast(message, false);

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
                        params.put("paymentAmount", payamount);
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

    private void sendNotification() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        getName(String.valueOf(userId), new NameCallback() {
            @Override
            public void onNameReceived(String name) {
                String notificationContent = "Member " + name + " has charged a fine";
                String url = Constants.URL_CHARGE_FINE + "?chama_id=" + chamaId;

                // Create an instance of NotificationSender
                NotificationSender notificationSender = new NotificationSender();

                // Call the sendNotification method
                notificationSender.sendNotification(getContext(), String.valueOf(userId), notificationContent, url);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                // Display or log the error message
            }
        });
    }

    interface NameCallback {
        void onNameReceived(String name);
        void onError(String errorMessage);
    }


    private void getName(String userId, final NameCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_NAME + "?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.getString("name");
                    callback.onNameReceived(name);
                } catch (JSONException e) {
                    callback.onError("Error occurred: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.getMessage());
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
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
