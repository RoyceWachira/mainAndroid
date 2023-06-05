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

public class AllContributions extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Contributions> contributionsList;
    private AdapterAllContributions adapterAllContributions;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_contributions, container, false);

        cardView= view.findViewById(R.id.cardViewContributions);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        contributionsList = new ArrayList<>();

        adapterAllContributions = new AdapterAllContributions(contributionsList, getContext());
        recyclerView.setAdapter(adapterAllContributions);

        viewAllContributions();

        return view;
    }

    private void viewAllContributions() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_CONRIBUTIONS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray contributionsArray = jsonObject.getJSONArray("allcontributions");

                        contributionsList.clear();

                        if (contributionsArray.length() > 0) {
                            TextView noContributions = getView().findViewById(R.id.noContributions);
                            noContributions.setVisibility(View.GONE);

                            for (int i = 0; i < contributionsArray.length(); i++) {
                                JSONObject contributionsObject = contributionsArray.getJSONObject(i);

                                String contributionAmount = contributionsObject.getString("contribution_amount");
                                String contributionDate = contributionsObject.getString("contribution_date");
                                String contributionId= String.valueOf(contributionsObject.getInt("contribution_id"));

                                Contributions contributions = new Contributions(contributionAmount, contributionId, contributionDate);
                                contributionsList.add(contributions);
                            }

                            adapterAllContributions.setContributionsList(contributionsList);
                            adapterAllContributions.notifyDataSetChanged();
                        }else {
                            TextView noContributions = getView().findViewById(R.id.noContributions);
                            noContributions.setVisibility(View.VISIBLE);
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