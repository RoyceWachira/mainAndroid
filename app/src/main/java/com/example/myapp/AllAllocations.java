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

public class AllAllocations extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Allocations> allocationsList;
    private AdapterAllocations adapterAllocations;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_allocations, container, false);

        cardView= view.findViewById(R.id.cardViewWithdrawals);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        allocationsList = new ArrayList<>();

        adapterAllocations = new AdapterAllocations(allocationsList, getContext());
        recyclerView.setAdapter(adapterAllocations);

        viewAllAllocations();

        return view;
    }

    private void viewAllAllocations() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_ALL_ALLOCATIONS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray allocationsArray = jsonObject.getJSONArray("allallocations");

                        allocationsList.clear();

                        if (allocationsArray.length() > 0) {
                            TextView noallocations = getView().findViewById(R.id.noallocations);
                            noallocations.setVisibility(View.GONE);

                            for (int i = 0; i < allocationsArray.length(); i++) {
                                JSONObject allocationsObject = allocationsArray.getJSONObject(i);

                                String allocationsAmount = allocationsObject.getString("allocation_amount");
                                String allocationsDate = allocationsObject.getString("allocation_date");
                                String allocationsId= String.valueOf(allocationsObject.getInt("allocation_id"));
                                String memberId = allocationsObject.getString("member_id");
                                String chamaId = allocationsObject.getString("chama_id");

                                Allocations allocations = new Allocations(memberId, allocationsId, allocationsAmount, allocationsDate, chamaId);
                                allocationsList.add(allocations);
                            }

                            adapterAllocations.setAllocationsList(allocationsList);
                            adapterAllocations.notifyDataSetChanged();
                        }else {
                            TextView nowithdrawals = getView().findViewById(R.id.nowithdrawals);
                            nowithdrawals.setVisibility(View.VISIBLE);
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