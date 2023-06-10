package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class ChamaNotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Notifications> notificationsList;
    private AdapterNotifications adapterNotifications;
    private String chamaId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chama_notifications, container, false);


        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        notificationsList = new ArrayList<>();

        adapterNotifications = new AdapterNotifications(notificationsList, getContext());
        recyclerView.setAdapter(adapterNotifications);

        viewNotifications();

        return view;
    }

    private void viewNotifications() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_NOTIFICATIONS + "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray notificationsArray = jsonObject.getJSONArray("allNotifications");

                        notificationsList.clear();

                        if (notificationsArray.length() > 0) {
                            for (int i = 0; i < notificationsArray.length(); i++) {
                                JSONObject notificationsObject = notificationsArray.getJSONObject(i);

                                String notificationId = notificationsObject.getString("notification_id");
                                String notificationContent = notificationsObject.getString("notification_content");
                                String userId= String.valueOf(notificationsObject.getInt("user_id"));
                                String chamaId= notificationsObject.getString("chama_id");
                                String notificationTitle= notificationsObject.getString("notification_title");
                                String createdAt= notificationsObject.getString("created_at");


                                Notifications notifications = new Notifications(notificationId, notificationTitle, notificationContent, createdAt, chamaId, userId);
                                notificationsList.add(notifications);
                            }

                            adapterNotifications.setNotificationsList(notificationsList);
                            adapterNotifications.notifyDataSetChanged();
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
