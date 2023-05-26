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

public class IndividualFines extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Fines> finesList;
    private AdapterIndividualFines adapterIndividualFines;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_fines, container, false);

        cardView= view.findViewById(R.id.cardViewFines);
        Bundle arguments = getArguments();

        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        finesList = new ArrayList<>();

        adapterIndividualFines = new AdapterIndividualFines(finesList, getContext());
        recyclerView.setAdapter(adapterIndividualFines);

        viewFines();

        return view;
    }

    private void viewFines() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_MEMBER_FINES+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray finesArray = jsonObject.getJSONArray("fines");

                        finesList.clear();

                        if (finesArray.length() > 0) {
                            TextView noFines = getView().findViewById(R.id.noFines);
                            noFines.setVisibility(View.GONE);

                            for (int i = 0; i < finesArray.length(); i++) {
                                JSONObject finesObject = finesArray.getJSONObject(i);

                                String fineAmount = finesObject.getString("fine_amount");
                                String fineStatus = finesObject.getString("fine_status");
                                String fineId= String.valueOf(finesObject.getInt("fine_id"));
                                String fineReason = finesObject.getString("fine_reason");
                                String dateFined = finesObject.getString("date_fined");

                                Fines fines = new Fines(fineAmount, fineId, fineStatus, fineReason, dateFined);
                                finesList.add(fines);
                            }

                            adapterIndividualFines.setFinesList(finesList);
                            adapterIndividualFines.notifyDataSetChanged();
                        }else {
                            TextView noFines = getView().findViewById(R.id.noFines);
                            noFines.setVisibility(View.VISIBLE);
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