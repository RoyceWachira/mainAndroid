package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText updateFirstName,updateLastName,updateUsername,updatePhoneNumber,updateMail;
    TextInputLayout updateGender;
    private Button editButton;
    AutoCompleteTextView autoCompleteTextView;

    String[] gender = {"Male", "Female", "Other"};
    ArrayAdapter<String> adapterGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_profile, container, false);

        updateMail=view.findViewById(R.id.updateMail);
        updateFirstName= view.findViewById(R.id.updateFirstName);
        updateLastName= view.findViewById(R.id.updateLastName);
        updateUsername= view.findViewById(R.id.updateUsername);
        updatePhoneNumber= view.findViewById(R.id.updatePhoneNumber);
        updateGender= view.findViewById(R.id.updateGender);

        autoCompleteTextView = view.findViewById(R.id.autoComplete_txt_update);
        adapterGender = new ArrayAdapter<>(getContext(), R.layout.list_gender, gender);
        autoCompleteTextView.setAdapter(adapterGender);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gender = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Gender:" + gender, Toast.LENGTH_SHORT).show();
            }
        });

        editButton= view.findViewById(R.id.btnUpdate);

        String username= SharedPrefManager.getInstance(getContext()).getKeyUserName();
        TextView headUserName = view.findViewById(R.id.headUserName);
        headUserName.setText(username);

        fetchUserData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName= updateFirstName.getText().toString().trim();
                String lastName= updateLastName.getText().toString().trim();
                String userName= updateUsername.getText().toString().trim();
                String phoneNumber= updatePhoneNumber.getText().toString().trim();
                String email= updateMail.getText().toString().trim();
                String gender= updateGender.getEditText().getText().toString().trim();

                updateUser(firstName, lastName, userName, phoneNumber, email, gender);
            }
        });

        return view;
    }

    private void updateUser(String firstName, String lastName, String userName, String phoneNumber, String email, String gender) {

        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        }

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(gender)) {
            showToast("Please fill in all the fields",true);
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address", true);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_USER+ "?user_id=" + userId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if (error==false) {
                                showToast(message, false);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
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
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("userName", userName);
                params.put("phoneNumber", phoneNumber);
                params.put("email", email);
                params.put("gender", gender);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void fetchUserData() {

        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_USER+ "?user_id=" + userId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (error==false) {
                                JSONObject userObject = jsonObject.getJSONObject("user");
                                String firstName = userObject.getString("first_name");
                                String lastName = userObject.getString("last_name");
                                String userName = userObject.getString("username");
                                String phoneNumber = userObject.getString("phone_number");
                                String email = userObject.getString("email");
                                String gender= userObject.getString("gender");

                                updateGender.setHint(gender);
                                updateFirstName.setText(firstName);
                                updateLastName.setText(lastName);
                                updateMail.setText(email);
                                updateUsername.setText(userName);
                                updatePhoneNumber.setText(phoneNumber);
                                showToast(message, false);

                            } else {
                                // User data retrieval failed
                                showToast(message, true);
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

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}