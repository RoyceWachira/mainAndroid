package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChamaDetails extends Fragment {

    private String chamaId;
    private Integer userId;
    private Button btnEditChama;
    private TextView headName,headDesc,chamaFlow,chamaTarget,chamaPeriod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chama_details, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }
        userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());

        headDesc= view.findViewById(R.id.headChamaDesc);
        headName= view.findViewById(R.id.headChamaName);
        chamaFlow= view.findViewById(R.id.txtFlow);
        chamaPeriod= view.findViewById(R.id.txtPeriod);
        chamaTarget= view.findViewById(R.id.txtTarget);

        isLeader();
        getChama();

        btnEditChama= view.findViewById(R.id.btnEditChama);

        btnEditChama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditChama editChama= new EditChama();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                editChama.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, editChama);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    private void getChama() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_CHAMA + "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("error") == false) {
                        JSONObject chamaObject = jsonObject.getJSONObject("chama");

                        String chamaName = chamaObject.getString("chama_name");
                        headName.setText(chamaName);
                        String chamaDescription = chamaObject.getString("chama_description");
                        headDesc.setText(chamaDescription);
                        String chamaId= String.valueOf(chamaObject.getInt("chama_id"));
                        String systemFlow= chamaObject.getString("system_flow");
                        chamaFlow.setText(systemFlow);
                        String contributionPeriod= chamaObject.getString("contribution_period");
                        chamaPeriod.setText(contributionPeriod);
                        String contributionTarget= chamaObject.getString("contribution_target");
                        chamaTarget.setText(contributionTarget);
                    }
                } catch (JSONException e) {
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                showToast(error.getMessage(),true);
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void isLeader() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_IS_LEADER+ "?chama_id=" + chamaId+ "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if(error==false){
                        btnEditChama= getView().findViewById(R.id.btnEditChama);
                        btnEditChama.setVisibility(View.VISIBLE);

                    }else{
                        btnEditChama=getView().findViewById(R.id.btnEditChama);
                        btnEditChama.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                showToast(error.getMessage(),true);
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
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