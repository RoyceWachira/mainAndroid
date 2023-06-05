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


public class NewContribution extends Fragment {

    private EditText editText;
    private Button btnContribute;
    String chamaId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_contribution, container, false);

        editText= view.findViewById(R.id.contributionAmount);
        btnContribute=view.findViewById(R.id.btnContribute);

        Bundle arguments= getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        btnContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeContribution();
            }
        });
        return view;
    }





    private void makeContribution() {
        final String contributionAmount = editText.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(contributionAmount)) {
            showToast("Please fill in all the fields", true);
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Contribute");
        builder.setMessage("Are you sure you want to contribute sh." + contributionAmount + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userId = null;
                userId = SharedPrefManager.getInstance(getContext()).getUserId();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MAKE_CONRIBUTION + "?user_id=" + userId + "&chama_id=" + chamaId, new Response.Listener<String>() {
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
                                int contributionId = messageObj.getInt("contribution_id");
                                int chamaId = messageObj.getInt("chama_id");
                                String contributionDate= messageObj.getString("contribution_date");
                                String nextContributionDate= messageObj.getString("next_contribution_date");
                                String contributionAmount= messageObj.getString("contribution_amount");
                                showToast("Contribution Successful", false);

                                // Pass the contribution ID to the next fragment
                                CompleteContribution completeContribution = new CompleteContribution();
                                Bundle bundle = new Bundle();
                                bundle.putInt("chama_id",chamaId);
                                bundle.putInt("contribution_id", contributionId);
                                bundle.putString("contribution_date",contributionDate);
                                bundle.putString("next_contribution_date",nextContributionDate);
                                bundle.putString("contribution_amount",contributionAmount);
                                completeContribution.setArguments(bundle);

                                // Replace the current fragment with the next fragment
                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.chamaFrameLayout, completeContribution);
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
                        params.put("contribution_amount", contributionAmount);
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