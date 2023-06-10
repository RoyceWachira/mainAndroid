package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JoinRequests extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Requests> requestsList;
    private AdapterJoinRequests adapterJoinRequests;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_requests, container, false);

        cardView= view.findViewById(R.id.cardViewJoinRequests);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        requestsList = new ArrayList<>();

        adapterJoinRequests = new AdapterJoinRequests(requestsList, getContext());
        recyclerView.setAdapter(adapterJoinRequests);

        viewJoinRequested();

        return view;
    }

    private void viewJoinRequested() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_JOIN_REQUESTS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray requestsArray = jsonObject.getJSONArray("allrequests");

                        requestsList.clear();

                        if (requestsArray.length() > 0) {
                            TextView norequests = getView().findViewById(R.id.noJoinRequests);
                            norequests.setVisibility(View.GONE);

                            for (int i = 0; i < requestsArray.length(); i++) {
                                JSONObject requestsObject = requestsArray.getJSONObject(i);

                                String requestsStatus = requestsObject.getString("join_status");
                                String requestsId= requestsObject.getString("request_id");
                                String requestedAt = requestsObject.getString("requested_at");
                                String chamaId = String.valueOf(requestsObject.getInt("chama_id"));

                                Requests requests = new Requests(requestsId, requestsStatus, requestedAt,chamaId);
                                requestsList.add(requests);
                            }

                            adapterJoinRequests.setRequestsList(requestsList);
                            adapterJoinRequests.notifyDataSetChanged();
                        }else {
                            TextView norequests = getView().findViewById(R.id.noJoinRequests);
                            norequests.setVisibility(View.VISIBLE);
                            cardView.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }



}