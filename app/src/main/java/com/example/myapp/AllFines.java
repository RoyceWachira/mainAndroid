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

public class AllFines extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Fines> finesList;
    private AdapterAllFines adapterAllFines;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_fines, container, false);

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

        adapterAllFines = new AdapterAllFines(finesList, getContext());
        recyclerView.setAdapter(adapterAllFines);

        viewAllFines();

        return view;
    }

    private void viewAllFines() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_FINES+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray finesArray = jsonObject.getJSONArray("allfines");

                        finesList.clear();

                        if (finesArray.length() > 0) {
                            TextView nofines = getView().findViewById(R.id.noFines);
                            nofines.setVisibility(View.GONE);

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

                            adapterAllFines.setFinesList(finesList);
                            adapterAllFines.notifyDataSetChanged();
                        }else {
                            TextView nofines = getView().findViewById(R.id.noFines);
                            nofines.setVisibility(View.VISIBLE);
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