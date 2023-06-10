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

public class Allwithdrawals extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Withdrawals> withdrawalsList;
    private AdapterAllWithdrawals adapterAllWithdrawals;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allwithdrawals, container, false);

        cardView= view.findViewById(R.id.cardViewWithdrawals);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        withdrawalsList = new ArrayList<>();

        adapterAllWithdrawals = new AdapterAllWithdrawals(withdrawalsList, getContext());
        recyclerView.setAdapter(adapterAllWithdrawals);

        viewAllWithdrawals();

        return view;
    }

    private void viewAllWithdrawals() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_WITHDRAWALS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray withdrawalsArray = jsonObject.getJSONArray("allwithdrawals");

                        withdrawalsList.clear();

                        if (withdrawalsArray.length() > 0) {
                            TextView nowithdrawals = getView().findViewById(R.id.nowithdrawals);
                            nowithdrawals.setVisibility(View.GONE);

                            for (int i = 0; i < withdrawalsArray.length(); i++) {
                                JSONObject withdrawalsObject = withdrawalsArray.getJSONObject(i);

                                String withdrawalsAmount = withdrawalsObject.getString("withdrawal_amount");
                                String withdrawalsReason = withdrawalsObject.getString("withdrawal_reason");
                                String withdrawalsId= String.valueOf(withdrawalsObject.getInt("withdrawal_id"));

                                Withdrawals withdrawals = new Withdrawals(withdrawalsAmount, withdrawalsId, withdrawalsReason);
                                withdrawalsList.add(withdrawals);
                            }

                            adapterAllWithdrawals.setWithdrawalsList(withdrawalsList);
                            adapterAllWithdrawals.notifyDataSetChanged();
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