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

public class AllInvestments extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Investments> investmentsList;
    private AdapterInvestments adapterInvestments;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_investments, container, false);

        cardView = view.findViewById(R.id.cardViewInvest);

        Bundle arguments = getArguments();
        if (arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        investmentsList = new ArrayList<>();

        adapterInvestments = new AdapterInvestments(investmentsList, getContext());
        recyclerView.setAdapter(adapterInvestments);

        viewAllInvestments();

        return view;
    }

    private void viewAllInvestments() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_ALL_INVESTMENTS + "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray investmentsArray = jsonObject.getJSONArray("allinvestments");

                        investmentsList.clear();

                        if (investmentsArray.length() > 0) {
                            TextView noInvestments = getView().findViewById(R.id.noinvestments);
                            noInvestments.setVisibility(View.GONE);

                            for (int i = 0; i < investmentsArray.length(); i++) {
                                JSONObject investmentsObject = investmentsArray.getJSONObject(i);

                                String investmentId = String.valueOf(investmentsObject.getInt("investment_id"));
                                String investmentAmount = investmentsObject.getString("investment_amount");
                                String investmentDate = investmentsObject.getString("investment_date");
                                String investmentArea = investmentsObject.getString("investment_area");
                                String expectedReturns = investmentsObject.getString("expected_returns");
                                String investmentDuration = investmentsObject.getString("investment_duration");

                                Investments investment = new Investments(investmentId, investmentAmount, investmentArea, investmentDate, investmentDuration, expectedReturns);
                                investmentsList.add(investment);
                            }

                            adapterInvestments.setInvestmentsList(investmentsList);
                            adapterInvestments.notifyDataSetChanged();
                        } else {
                            TextView noInvestments = getView().findViewById(R.id.noinvestments);
                            noInvestments.setVisibility(View.VISIBLE);
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
