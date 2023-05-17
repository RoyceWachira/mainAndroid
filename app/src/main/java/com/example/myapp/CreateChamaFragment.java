package com.example.myapp;

import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateChamaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateChamaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateChamaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateChamaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateChamaFragment newInstance(String param1, String param2) {
        CreateChamaFragment fragment = new CreateChamaFragment();
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

    private EditText editChamaName, editChamaDesc;
    private Button btnCreate;
    private ProgressDialog progressDialog;

    String[] contributionPeriod = {String.valueOf(15), String.valueOf(30), String.valueOf(45),String.valueOf(60)};
    String[] systemFlow= {"merry-go-round","linear"};
    AutoCompleteTextView autoComplete_txtPeriod,autoComplete_txtFlow;
    ArrayAdapter<String> adapterPeriod,adapterFlow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_chama, container, false);

        editChamaDesc = view.findViewById(R.id.editChamaDesc);
        editChamaName = view.findViewById(R.id.editChamaName);

        btnCreate = view.findViewById(R.id.btnCreate);

        autoComplete_txtFlow = view.findViewById(R.id.autoComplete_txtFlow);
        autoComplete_txtPeriod= view.findViewById(R.id.autoComplete_txtPeriod);

        adapterPeriod = new ArrayAdapter<>(getContext(), R.layout.list_gender, contributionPeriod);
        adapterFlow= new ArrayAdapter<>(getContext(),R.layout.list_gender,systemFlow);

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

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChama();
            }
        });
        return view;
    }





    private void createChama() {
        final String chamaName = editChamaName.getText().toString().trim();
        final String chamaDesc = editChamaDesc.getText().toString().trim();
        final String contributionPeriod = autoComplete_txtPeriod.getText().toString().trim();
        final String systemFlow = autoComplete_txtFlow.getText().toString().trim();

        // Check if all fields are filled
        if (TextUtils.isEmpty(chamaName) || TextUtils.isEmpty(chamaDesc) || TextUtils.isEmpty(systemFlow) || TextUtils.isEmpty(contributionPeriod) ) {
            showToast("Please fill in all the fields",true);
            return;
        }


        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_NEW_CHAMA+ "?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (error) {
                        showToast(message, true);
                    } else {
                        showToast(message, false);
                        HomeFragment homeFragment= new HomeFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occured" +e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                Log.e("Error", "Error occurred", error);
                showToast("Error occurred: " + errorMessage, true);
            }
        }){
            @Override
            protected Map<String ,String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("chama_name",chamaName);
                params.put("description",chamaDesc);
                params.put("contribution_period", String.valueOf(contributionPeriod));
                params.put("system_flow", String.valueOf(systemFlow));

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