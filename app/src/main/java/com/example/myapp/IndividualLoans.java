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

public class IndividualLoans extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Loans> loansList;
    private AdapterIndividualLoans adapterIndividualLoans;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_loans, container, false);

        cardView= view.findViewById(R.id.cardViewLoans);
        Bundle arguments = getArguments();

        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        loansList = new ArrayList<>();

        adapterIndividualLoans = new AdapterIndividualLoans(loansList, getContext());
        recyclerView.setAdapter(adapterIndividualLoans);

        viewLoans();

        return view;
    }

    private void viewLoans() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_MEMBER_LOANS+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray loansArray = jsonObject.getJSONArray("loans");

                        loansList.clear();

                        if (loansArray.length() > 0) {
                            TextView noLoans = getView().findViewById(R.id.noLoans);
                            noLoans.setVisibility(View.GONE);

                            for (int i = 0; i < loansArray.length(); i++) {
                                JSONObject loansObject = loansArray.getJSONObject(i);

                                String loanAmount = loansObject.getString("loan_amount");
                                String loanStatus = loansObject.getString("loan_status");
                                String loanId= String.valueOf(loansObject.getInt("loan_id"));
                                String amountPayable = loansObject.getString("amount_payable");
                                String dueAt = loansObject.getString("due_at");
                                String chamaId = String.valueOf(loansObject.getInt("chama_id"));

                                Loans loans = new Loans(loanAmount, loanId, loanStatus, amountPayable, dueAt,chamaId);
                                loansList.add(loans);
                            }

                            adapterIndividualLoans.setLoansList(loansList);
                            adapterIndividualLoans.notifyDataSetChanged();
                        }else {
                            TextView noLoans = getView().findViewById(R.id.noLoans);
                            noLoans.setVisibility(View.VISIBLE);
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