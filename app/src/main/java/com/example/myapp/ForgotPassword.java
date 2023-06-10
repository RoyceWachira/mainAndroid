package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private Button btnReset;
    private ProgressDialog progressDialog;
    private TextView txtRegister;
    private TextView txtBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        btnReset = findViewById(R.id.btnReset);
        editTextEmail = findViewById(R.id.Email_reset);
        txtBack= findViewById(R.id.txtBack);
        txtRegister= findViewById(R.id.txtRegister);

        progressDialog = new ProgressDialog(this);
        txtRegister.setOnClickListener(this::onClick);
        txtBack.setOnClickListener(this::onClick);
        btnReset.setOnClickListener(this);

    }



    private void resetPassword(){
        final String email= editTextEmail.getText().toString().trim();
        progressDialog.show();

        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address", true);
            return;
        }

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    String message=jsonObject.getString("message");
                    if (error==false) {
                        showToast(message,false);
                    }else{
                        showToast(message,true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error occurred: " + e.getMessage(), true);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error occurred: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String ,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, findViewById(R.id.toast_message));
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, findViewById(R.id.toast_message));
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View view) {
        if(view==btnReset)
            resetPassword();
        if (view==txtBack)
            startActivity(new Intent(this, MainActivity.class));
        if (view==txtRegister)
            startActivity(new Intent(this, RegisterActivity.class));
    }
}
