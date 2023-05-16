package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean isOpen = false;
    private Button quickJoin;
    private AdapterjoinedChamas adapterJoinedChamas;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chamas> chamasList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.joinedChamasRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        chamasList = new ArrayList<>();
        adapterJoinedChamas = new AdapterjoinedChamas(chamasList, getContext());
        recyclerView.setAdapter(adapterJoinedChamas);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        // Initialize views
        fab = view.findViewById(R.id.floatingButton);
        fab1 = view.findViewById(R.id.floatingButton2);
        fab2 = view.findViewById(R.id.floatingButton3);

        quickJoin= view.findViewById(R.id.quickJoin);

        // Initialize animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);


        quickJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinChamaFragment joinChamaFragment = new JoinChamaFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, joinChamaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fab.setOnClickListener(v -> onFabClicked());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChamaFragment createChamaFragment= new CreateChamaFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, createChamaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinChamaFragment joinChamaFragment = new JoinChamaFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, joinChamaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });

        fetchChamasCount();
        fetchUsersCount();
        fetchJoinedChamas();

        return view;
    }

    private void onFabClicked() {
        if (isOpen) {
            fab.startAnimation(fabClose);
            fab1.startAnimation(toBottom);
            fab2.startAnimation(toBottom);
            fab1.setClickable(false);
            fab2.setClickable(false);

            TextView label2 = getView().findViewById(R.id.label2);
            TextView label3 = getView().findViewById(R.id.label3);
            label2.setVisibility(View.INVISIBLE);
            label3.setVisibility(View.INVISIBLE);
        } else {
            fab.startAnimation(fabOpen);
            fab1.startAnimation(fromBottom);
            fab2.startAnimation(fromBottom);
            fab1.setClickable(true);
            fab2.setClickable(true);

            TextView label2 = getView().findViewById(R.id.label2);
            TextView label3 = getView().findViewById(R.id.label3);
            label2.setVisibility(View.VISIBLE);
            label3.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }

    private void fetchChamasCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_CHAMAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalCount = jsonObject.getInt("totalChamas");

                    // Update the TextView in your layout with the total count
                    TextView chamasCountTextView = getView().findViewById(R.id.chamasCountTextView);
                    chamasCountTextView.setText(String.valueOf(totalCount));
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

    private void fetchUsersCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalCount = jsonObject.getInt("totalUsers");

                    TextView usersCountTextView = getView().findViewById(R.id.usersCountTextView);
                    usersCountTextView.setText(String.valueOf(totalCount));
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

    private void fetchJoinedChamas() {

        // Retrieve the user's ID or any identifier needed to fetch joined chamas

        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        } else {

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_USER_JOINED_CHAMAS + "?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray chamasArray = jsonObject.getJSONArray("chamas");

                        // Check if the user has joined any chamas
                        if (chamasArray.length() > 0) {
                            TextView noChamasTextView = getView().findViewById(R.id.noChamasTextView);
                            noChamasTextView.setVisibility(View.GONE);

                            List<Chamas> joinedChamasList = new ArrayList<>();

                            for (int i = 0; i < chamasArray.length(); i++) {
                                JSONObject chamaObject = chamasArray.getJSONObject(i);

                                String chamaName = chamaObject.getString("chama_name");
                                String chamaDescription = chamaObject.getString("chama_description");

                                Chamas chamas = new Chamas(chamaName, chamaDescription);
                                joinedChamasList.add(chamas);
                            }

                            adapterJoinedChamas.setChamasList(joinedChamasList);
                            adapterJoinedChamas.notifyDataSetChanged();
                        } else {

                            TextView noChamasTextView = getView().findViewById(R.id.noChamasTextView);
                            noChamasTextView.setVisibility(View.VISIBLE);
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