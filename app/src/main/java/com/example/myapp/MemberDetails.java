package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

public class MemberDetails extends Fragment {

    private TextView txtUserName,txtfname,txtRole,txtmail,txtphoneNumber,txtDate;
    private Button chair,vice,sec,treasure,demote;
    String chamaId,memberId, chamaRole;
    Integer userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_member_details, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            String fname = arguments.getString("firstName");
            String uname = arguments.getString("username");
            String role = arguments.getString("chamaRole");
            String phoneNo = arguments.getString("phoneNumber");
            String mail = arguments.getString("email");
            String dateJoined = arguments.getString("dateJoined");
            chamaId = arguments.getString("chamaId");
            memberId= arguments.getString("memberId");

            txtUserName = view.findViewById(R.id.headUserName);
            txtUserName.setText(uname);

            txtfname = view.findViewById(R.id.txtfname);
            txtfname.setText(fname);

            txtRole = view.findViewById(R.id.headChamaRole);
            txtRole.setText(role);

            txtmail = view.findViewById(R.id.mail);
            txtmail.setText(mail);

            txtphoneNumber = view.findViewById(R.id.phone);
            txtphoneNumber.setText(phoneNo);

            txtDate= view.findViewById(R.id.joined);
            txtDate.setText(dateJoined);
        }

        userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());

        chair=view.findViewById(R.id.btnChair);
        vice= view.findViewById(R.id.btnViceChair);
        sec= view.findViewById(R.id.btnSecretary);
        treasure= view.findViewById(R.id.btnTreasurer);
        demote= view.findViewById(R.id.btnDemote);

        isChairOrVice();

        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChairperson();
            }
        });

        vice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaRole="Vice ChairPerson";
                changeRole();
            }
        });

        treasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaRole="Treasurer";
                changeRole();
            }
        });

        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaRole="Secretary";
                changeRole();
            }
        });

        demote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demote();
            }
        });

        return view;
    }

    private void isChairOrVice() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_IS_CHAIR_OR_VICE+ "?chama_id=" + chamaId+ "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if(error==false){
                        chair= getView().findViewById(R.id.btnChair);
                        chair.setVisibility(View.VISIBLE);
                        vice=getView().findViewById(R.id.btnViceChair);
                        vice.setVisibility(View.VISIBLE);
                        sec=getView().findViewById(R.id.btnSecretary);
                        sec.setVisibility(View.VISIBLE);
                        treasure=getView().findViewById(R.id.btnTreasurer);
                        treasure.setVisibility(View.VISIBLE);
                        demote=getView().findViewById(R.id.btnDemote);
                        demote.setVisibility(View.VISIBLE);
                    }else{
                        chair= getView().findViewById(R.id.btnChair);
                        chair.setVisibility(View.INVISIBLE);
                        vice=getView().findViewById(R.id.btnViceChair);
                        vice.setVisibility(View.INVISIBLE);
                        sec=getView().findViewById(R.id.btnSecretary);
                        sec.setVisibility(View.INVISIBLE);
                        treasure=getView().findViewById(R.id.btnTreasurer);
                        treasure.setVisibility(View.INVISIBLE);
                        demote=getView().findViewById(R.id.btnDemote);
                        demote.setVisibility(View.INVISIBLE);
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

    private void changeRole() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHANGE_ROLE+ "?chama_id=" + chamaId + "&user_id=" + userId + "&member_id=" + memberId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chamaId", chamaId);
                params.put("chamaRole", chamaRole);
                params.put("memberId",memberId);
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void demote() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DEMOTE+ "?chama_id=" + chamaId + "&user_id=" + userId + "&member_id=" + memberId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chamaId", chamaId);
                params.put("memberId",memberId);
                params.put("userId", String.valueOf(userId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void makeChairperson() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MAKE_CHAIR+ "?chama_id=" + chamaId + "&user_id=" + userId + "&member_id=" + memberId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("chamaId", chamaId);
                params.put("memberId",memberId);
                params.put("userId", String.valueOf(userId));
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