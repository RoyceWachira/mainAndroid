package com.example.myapp;

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


public class EditFine extends Fragment {

    private EditText fineReason, fineAmount;
    private Button btnEditFine;
    String chamaId,fineId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_fine, container, false);

        fineAmount = view.findViewById(R.id.editfineAmount);
        fineReason = view.findViewById(R.id.editfineReason);
        btnEditFine = view.findViewById(R.id.btnEditFine);

        Bundle arguments = getArguments();
        if (arguments != null) {
            chamaId = arguments.getString("chamaId");
            fineId= arguments.getString("fineId");

        }

        fetchfineData();

        btnEditFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount= fineAmount.getText().toString().trim();
                String reason= fineReason.getText().toString().trim();
                editFine(amount,reason);
            }
        });
        return view;
    }

    private void fetchfineData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_FINE+ "?fine_id=" + fineId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error==false) {
                        JSONObject meetingObject = jsonObject.getJSONObject("fine");

                        String fAmount = meetingObject.getString("fine_amount");
                        fineAmount.setText(fAmount);
                        String reason = meetingObject.getString("fine_reason");
                        fineReason.setText(reason);

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

    private void editFine( String fineAmount, String fineReason) {

        if (TextUtils.isEmpty(fineAmount) || TextUtils.isEmpty(fineReason) ) {
            showToast("Please fill in all the fields",true);
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT_FINE+ "?fine_id=" + fineId, new Response.Listener<String>() {
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
                params.put("fineAmount", fineAmount);
                params.put("fineReason", fineReason);
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