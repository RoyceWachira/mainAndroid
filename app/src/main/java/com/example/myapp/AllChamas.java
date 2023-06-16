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

public class AllChamas extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chamas> chamaList;
    private AdapterSearch adapterChama;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_chamas, container, false);

        cardView = view.findViewById(R.id.cardViewChamas);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        chamaList = new ArrayList<>();

        adapterChama = new AdapterSearch(chamaList, getContext());
        recyclerView.setAdapter(adapterChama);

        viewAllChamas();

        return view;
    }

    private void viewAllChamas() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_CHAMAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error") == false) {
                        JSONArray chamasArray = jsonObject.getJSONArray("chamas");

                        chamaList.clear();

                        if (chamasArray.length() > 0) {
                            TextView noChamas = getView().findViewById(R.id.nochamas);
                            noChamas.setVisibility(View.GONE);

                            for (int i = 0; i < chamasArray.length(); i++) {
                                JSONObject chamaObject = chamasArray.getJSONObject(i);

                                String chamaId = chamaObject.getString("chama_id");
                                String chamaName = chamaObject.getString("chama_name");
                                String description = chamaObject.getString("chama_description");
                                String contributionPeriod = chamaObject.getString("contribution_period");
                                String systemFlow = chamaObject.getString("system_flow");
                                String contributionTarget = chamaObject.getString("contribution_target");

                                Chamas chama = new Chamas(chamaId, chamaName, description, contributionPeriod, systemFlow, contributionTarget);
                                chamaList.add(chama);
                            }

                            adapterChama.notifyDataSetChanged();
                        } else {
                            TextView noChamas = getView().findViewById(R.id.nochamas);
                            noChamas.setVisibility(View.VISIBLE);
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
