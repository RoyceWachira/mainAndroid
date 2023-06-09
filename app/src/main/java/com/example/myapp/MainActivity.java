package com.example.myapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginUserName, loginPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private TextView txtRegister;
    private TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("chama_notifications", "Chama Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        loginUserName = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        txtRegister.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void userLogin() {
        final String username = loginUserName.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();

        TextInputLayout passwordInputLayout = findViewById(R.id.password_text_input);
        final ImageView passwordToggle = findViewById(R.id.password_toggle);
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.visible_password);
                } else {
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.visible_password);
                }
            }
        });
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error) {
                                showToast(message, true); // Show error message with red background color
                            } else {
                                // Handle successful login
                                int userId = jsonObject.getInt("userId");
                                String username = jsonObject.getString("username");
                                String role = jsonObject.getString("role");
                                String sId= jsonObject.getString("sId");

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(userId, username, role,sId);

                                showToast(message, false); // Show success message with green background color
                                finish();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
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
                        progressDialog.dismiss();
                        String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                        Log.e("LoginError", "Error occurred", error);
                        showToast("Error occurred: " + errorMessage, true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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


    @Override
    public void onClick(View view){
        if(view==btnLogin)
            userLogin();

        if (view==txtRegister)
            startActivity(new Intent(this, RegisterActivity.class));
        if (view==txtForgotPassword)
            startActivity(new Intent(this, ForgotPassword.class));

    }
}