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

public class AllUsers extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userList;
    private AdapterUser adapterUser;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        cardView = view.findViewById(R.id.cardViewUsers);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        userList = new ArrayList<>();

        adapterUser = new AdapterUser(userList, getContext());
        recyclerView.setAdapter(adapterUser);

        viewAllUsers();

        return view;
    }

    private void viewAllUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_ALL_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error") == false) {
                        JSONArray usersArray = jsonObject.getJSONArray("users");

                        userList.clear();

                        if (usersArray.length() > 0) {
                            TextView noUsers = getView().findViewById(R.id.nousers);
                            noUsers.setVisibility(View.GONE);

                            for (int i = 0; i < usersArray.length(); i++) {
                                JSONObject userObject = usersArray.getJSONObject(i);

                                int userId = userObject.getInt("user_id");
                                String userName = userObject.getString("username");
                                String userEmail = userObject.getString("email");

                                User user = new User(userId, userName, userEmail);
                                userList.add(user);
                            }

                            adapterUser.notifyDataSetChanged();
                        } else {
                            TextView noUsers = getView().findViewById(R.id.nousers);
                            noUsers.setVisibility(View.VISIBLE);
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
