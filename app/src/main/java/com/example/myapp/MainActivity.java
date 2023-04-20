package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUserName, editTextPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private TextView txtRegister;
    private TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName=(EditText) findViewById(R.id.editTextUsername);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        txtRegister=(TextView) findViewById(R.id.txtRegister);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        txtRegister.setOnClickListener(this::onClick);
        txtForgotPassword.setOnClickListener(this::onClick);
        btnLogin.setOnClickListener(this);

    }

    private void userLogin(){
        final String username= editTextUserName.getText().toString().trim();
        final String password= editTextPassword.getText().toString().trim();
        progressDialog.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if (error==false) {
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(jsonObject.getInt("userId"),
                                jsonObject.getString("username"), jsonObject.getString("email"),jsonObject.getString("role")
                        );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view){
        if(view==btnLogin)
            userLogin();
            startActivity(new Intent(this, HomeActivity.class));
        if (view==txtRegister)
            startActivity(new Intent(this, RegisterActivity.class));
        if (view==txtForgotPassword)
            startActivity(new Intent(this, ForgotPassword.class));

    }
}