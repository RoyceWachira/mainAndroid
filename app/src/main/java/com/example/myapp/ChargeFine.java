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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChargeFine extends Fragment {

    private EditText fineReason, fineAmount;
    private Button btnCharge;
    private AdapterSelectMembers adapterSelectMembers;
    String chamaId,chamaName,chamaFlow;
    List<String> selectedMemberIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_fine, container, false);

        fineAmount = view.findViewById(R.id.fineAmount);
        fineReason = view.findViewById(R.id.fineReason);
        btnCharge = view.findViewById(R.id.btnCharge);

        Bundle arguments = getArguments();
        if (arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
            selectedMemberIds = arguments.getStringArrayList("selectedMemberIds");
            Log.d("Ids", String.valueOf(selectedMemberIds));

        }

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeFine();
            }
        });
        return view;
    }

    private void chargeFine() {
        final String fineamount = fineAmount.getText().toString().trim();
        final String finereason = fineReason.getText().toString().trim();

        String[] userIdsArray = selectedMemberIds.toArray(new String[0]);
        String userIds = TextUtils.join(",", userIdsArray);
        Log.d("uIds",userIds);

        // Check if all fields are filled
        if (TextUtils.isEmpty(fineamount) || TextUtils.isEmpty(finereason)) {
            showToast("Please fill in all the fields", true);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Charge Fine");
        builder.setMessage("Are you sure you want to charge sh." + fineamount + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHARGE_FINE + "?chama_id=" + chamaId, new Response.Listener<String>() {
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
                                JSONArray jsonArray = jsonResponse.getJSONArray("message");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Extract the values from each JSON object
                                int fineId = jsonObject.getInt("fine_id");
                                int chamaId = jsonObject.getInt("chama_id");
                                String fineReason = jsonObject.getString("fine_reason");
                                String fineAmount = jsonObject.getString("fine_amount");
                                String dateFined = jsonObject.getString("date_fined");
                                String fineStatus = jsonObject.getString("fine_status");
                                showToast("Charge Successful", false);

                                // Pass the values to the next fragment
                                CompleteFine completeFine = new CompleteFine();
                                Bundle bundle = new Bundle();
                                bundle.putInt("chamaId", chamaId);
                                bundle.putString("chamaName",chamaName);
                                bundle.putString("chamaFlow",chamaFlow);
                                bundle.putInt("fineId", fineId);
                                bundle.putString("fineReason", fineReason);
                                bundle.putString("fineAmount", fineAmount);
                                bundle.putString("dateFined", dateFined);
                                bundle.putString("fineStatus", fineStatus);
                                completeFine.setArguments(bundle);

                                Log.d("fineAmount", fineAmount);

                                // Replace the current fragment with the next fragment
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.chamaFrameLayout, completeFine);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

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
                        params.put("fineAmount", fineamount);
                        params.put("fineReason", finereason);
                        params.put("userIds",userIds);
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
