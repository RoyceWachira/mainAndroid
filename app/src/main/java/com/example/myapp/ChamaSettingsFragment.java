package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChamaSettingsFragment extends Fragment {

    private CardView info,leave,requests,home;
    String chamaId,chamaFlow,chamaName;
    Integer userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chama_settings, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaName = arguments.getString("chamaName");
            chamaFlow= arguments.getString("chamaFlow");
        }
        userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());

        info= view.findViewById(R.id.cardChamaInfo);
        leave= view.findViewById(R.id.cardLeaveChama);
        requests= view.findViewById(R.id.cardjoinRequests);
        home=view.findViewById(R.id.cardToHome);

        isLeader();

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChamaDetails chamaDetails= new ChamaDetails();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                chamaDetails.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, chamaDetails);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinRequests joinRequests= new JoinRequests();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                joinRequests.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, joinRequests);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Leave Chama");
                builder.setMessage("Are you sure you want to leave this Chama?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = SharedPrefManager.getInstance(getContext()).getUserId();
                        String url = Constants.URL_LEAVE_CHAMA + "?user_id=" + userId + "&chama_id=" + chamaId;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Exit");
                builder.setMessage("Are you sure you want to exit to main page?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
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
                        requests= getView().findViewById(R.id.cardjoinRequests);
                        requests.setVisibility(View.VISIBLE);
                    }else{
                        requests= getView().findViewById(R.id.cardjoinRequests);
                        requests.setVisibility(View.INVISIBLE);
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