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

public class AllMeetings extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Meetings> meetingsList;
    private AdapterMeetings adapterMeetings;
    private String chamaId;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_meetings, container, false);

        cardView= view.findViewById(R.id.cardViewMeet);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        meetingsList = new ArrayList<>();

        adapterMeetings = new AdapterMeetings(meetingsList, getContext());
        recyclerView.setAdapter(adapterMeetings);

        viewAllMeetings();

        return view;
    }

    private void viewAllMeetings() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_MEETINGS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray meetingsArray = jsonObject.getJSONArray("allMeetings");

                        meetingsList.clear();

                        if (meetingsArray.length() > 0) {
                            TextView noMeetings = getView().findViewById(R.id.noMeetings);
                            noMeetings.setVisibility(View.GONE);

                            for (int i = 0; i < meetingsArray.length(); i++) {
                                JSONObject meetingsObject = meetingsArray.getJSONObject(i);

                                String date = meetingsObject.getString("meeting_date");
                                String purpose = meetingsObject.getString("meeting_purpose");
                                String time= meetingsObject.getString("meeting_time");
                                String venue = meetingsObject.getString("meeting_venue");
                                String by = meetingsObject.getString("created_by");
                                String id = meetingsObject.getString("meeting_id");

                                Meetings meetings = new Meetings(date, purpose, id, time, venue,by);
                                meetingsList.add(meetings);
                            }

                            adapterMeetings.setMeetingsList(meetingsList);
                            adapterMeetings.notifyDataSetChanged();
                        }else {
                            TextView noMeetings = getView().findViewById(R.id.noMeetings);
                            noMeetings.setVisibility(View.VISIBLE);
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