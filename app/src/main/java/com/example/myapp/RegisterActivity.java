package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUserName, editTextEmail, editTextPassword, editFirstName, editLastName, editConfirmPassword, editPhoneNumber;
    private Button btnRegister;
    private ProgressDialog progressDialog;

    String[] gender = {"Male", "Female", "Other"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editEmail);
        editTextUserName = findViewById(R.id.editUsername);
        editTextPassword = findViewById(R.id.editPassword);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);

        btnRegister = findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(this);
        btnRegister.setOnClickListener(this);

        autoCompleteTextView = findViewById(R.id.autoComplete_txt);
        adapterGender = new ArrayAdapter<>(this, R.layout.list_gender, gender);
        autoCompleteTextView.setAdapter(adapterGender);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gender = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Gender:" + gender, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String userName = editTextUserName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String firstName = editFirstName.getText().toString().trim();
        final String lastName = editLastName.getText().toString().trim();
        final String confirmPassword = editConfirmPassword.getText().toString().trim();
        final String phoneNumber = editPhoneNumber.getText().toString().trim();
        final String gender = autoCompleteTextView.getText().toString().trim();

        TextInputLayout passwordInputLayout = findViewById(R.id.password_text_input);
        final ImageView passwordToggle = findViewById(R.id.password_toggle);
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.visible_password);
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.visible_password);
                }
            }
        });

        // Check if all fields are filled
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(gender)) {
            Toast.makeText(getApplicationContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String ,String>getParams() throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("userName",userName);
                params.put("email",email);
                params.put("password",password);
                params.put("firstName",firstName);
                params.put("lastName",lastName);
                params.put("confirmPassword",confirmPassword);
                params.put("phoneNumber",phoneNumber);
                params.put("gender", String.valueOf(gender));

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onClick(View view){
        if(view==btnRegister)
            registerUser();
    }


}