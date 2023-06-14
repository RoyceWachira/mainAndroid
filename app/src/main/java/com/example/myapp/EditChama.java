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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditChama extends Fragment {

    private EditText editChamaName, editChamaDesc, editContTarget;
    TextInputLayout editFlow,editPeriod;
    private Button btnUpdate;
    String chamaId;

    String[] contributionPeriod = {String.valueOf(15), String.valueOf(30), String.valueOf(45), String.valueOf(60)};
    String[] systemFlow = {"merry-go-round", "linear"};
    AutoCompleteTextView autoComplete_txtPeriod, autoComplete_txtFlow;
    ArrayAdapter<String> adapterPeriod, adapterFlow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_chama, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }
        editChamaDesc = view.findViewById(R.id.editChamaDesc);
        editChamaName = view.findViewById(R.id.editChamaName);
        editContTarget = view.findViewById(R.id.editContTarget);
        editFlow=view.findViewById(R.id.editFlow);
        editPeriod= view.findViewById(R.id.editPeriod);
        btnUpdate = view.findViewById(R.id.btnUpdateChama);

        autoComplete_txtFlow = view.findViewById(R.id.autoComplete_txtFlow);
        autoComplete_txtPeriod = view.findViewById(R.id.autoComplete_txtPeriod);

        adapterPeriod = new ArrayAdapter<>(getContext(), R.layout.list_gender, contributionPeriod);
        adapterFlow = new ArrayAdapter<>(getContext(), R.layout.list_gender, systemFlow);

        autoComplete_txtPeriod.setAdapter(adapterPeriod);
        autoComplete_txtFlow.setAdapter(adapterFlow);

        autoComplete_txtPeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contributionPeriod = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Contribution Period:" + contributionPeriod, Toast.LENGTH_SHORT).show();
            }
        });

        autoComplete_txtFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String systemFlow = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "System Flow:" + systemFlow, Toast.LENGTH_SHORT).show();
            }
        });

        fetchChamaData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chamaName= editChamaName.getText().toString().trim();
                String chamaDescription= editChamaDesc.getText().toString().trim();
                String contributionTarget= editContTarget.getText().toString().trim();
                String contPeriod= editPeriod.getEditText().getText().toString().trim();
                String sysFlow= editFlow.getEditText().getText().toString().trim();

                editChama(chamaName, chamaDescription, contPeriod, contributionTarget, sysFlow);
            }
        });
        return view;
    }

    private void fetchChamaData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_CHAMA+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error==false) {
                        JSONObject chamaObject = jsonObject.getJSONObject("chama");

                        String chamaName = chamaObject.getString("chama_name");
                        editChamaName.setText(chamaName);
                        String chamaDescription = chamaObject.getString("chama_description");
                        editChamaDesc.setText(chamaDescription);
                        String systemFlow= chamaObject.getString("system_flow");
                        editFlow.setHint(systemFlow);
                        String contributionPeriod= chamaObject.getString("contribution_period");
                        editPeriod.setHint(contributionPeriod);
                        String contributionTarget= chamaObject.getString("contribution_target");
                        editContTarget.setText(contributionTarget);

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

    private void editChama( String chamaName, String chamaDescription, String contributionPeriod, String contributionTarget, String systemFlow) {

        if (TextUtils.isEmpty(chamaName) || TextUtils.isEmpty(chamaDescription) || TextUtils.isEmpty(contributionPeriod) || TextUtils.isEmpty(contributionPeriod) || TextUtils.isEmpty(contributionTarget) || TextUtils.isEmpty(systemFlow)) {
            showToast("Please fill in all the fields",true);
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_CHAMA+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");
                    if (error==false) {
                        showToast(message, false);
                        ChamaDetails chamaDetails= new ChamaDetails();
                        Bundle args = new Bundle();
                        args.putString("chamaId", chamaId);
                        chamaDetails.setArguments(args);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.chamaFrameLayout, chamaDetails);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
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
                params.put("chamaName", chamaName);
                params.put("chamaDescription", chamaDescription);
                params.put("contributionPeriod", contributionPeriod);
                params.put("contributionTarget", contributionTarget);
                params.put("systemFlow", systemFlow);
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