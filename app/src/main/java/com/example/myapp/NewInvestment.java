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


public class NewInvestment extends Fragment {

    private EditText invArea, invAmount, exReturn;
    private Button btnCreate;

    String[] duration ={"Short Term", "Long Term"};
    AutoCompleteTextView autoComplete_txtDuration;
    ArrayAdapter<String> adapterDuration;
    String chamaId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_investment, container, false);

        invAmount = view.findViewById(R.id.invAmount);
        invArea = view.findViewById(R.id.invArea);
        exReturn= view.findViewById(R.id.invExpectedRet);
        btnCreate = view.findViewById(R.id.btnCreate);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }


        autoComplete_txtDuration= view.findViewById(R.id.autoComplete_txtDuration);

        adapterDuration = new ArrayAdapter<>(getContext(), R.layout.list_gender, duration);

        autoComplete_txtDuration.setAdapter(adapterDuration);

        autoComplete_txtDuration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String invDuration = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Investment Duration:" + invDuration, Toast.LENGTH_SHORT).show();
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInvestment();
            }
        });
        return view;
    }





    private void createInvestment() {
        final String amount = invAmount.getText().toString().trim();
        final String area = invArea.getText().toString().trim();
        final String duration = autoComplete_txtDuration.getText().toString().trim();
        final String exp = exReturn.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(area) || TextUtils.isEmpty(duration) || TextUtils.isEmpty(exp) ) {
            showToast("Please fill in all the fields",true);
            return;
        }


        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NEW_INVESTMENT+ "?user_id=" + userId + "&chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RES",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (error) {
                        showToast(message, true);
                    } else {
                        showToast(message, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occured" +e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                Log.e("Error", "Error occurred", error);
                showToast("Error occurred: " + errorMessage, true);
            }
        }){
            @Override
            protected Map<String ,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("investmentArea",area);
                params.put("investmentAmount",amount);
                params.put("investmentDuration", String.valueOf(duration));
                params.put("expectedReturns",exp);

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