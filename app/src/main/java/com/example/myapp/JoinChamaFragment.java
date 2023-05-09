package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinChamaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinChamaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinChamaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinChamaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinChamaFragment newInstance(String param1, String param2) {
        JoinChamaFragment fragment = new JoinChamaFragment();
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
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chamas> chamasList;
    private List<Chamas> filteredChamasList; // Add a new list to store filtered chamas
    private AdapterSearch adapterSearch;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_chama, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        chamasList = new ArrayList<>(); // Initialize the chamasList
        filteredChamasList = new ArrayList<>(); // Initialize the filteredChamasList

        adapterSearch = new AdapterSearch(chamasList, getContext());
        recyclerView.setAdapter(adapterSearch);

        fetchChamas();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterChamasList(newText);
                return true;
            }
        });

        return view;
    }

    public void fetchChamas() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_ALL_CHAMAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Context context = getContext(); // Get the fragment's context

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful error
                    if (jsonObject.getBoolean("error")==false) {
                        // Display a success message to the user
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        // Retrieve the chamas array from the response
                        JSONArray chamasArray = jsonObject.getJSONArray("chamas");

                        // Create a list to store the chamas
                        List<Chamas> chamasList = new ArrayList<>();

                        // Iterate over the chamas array and create Chamas objects
                        for (int i = 0; i < chamasArray.length(); i++) {
                            JSONObject chamaObject = chamasArray.getJSONObject(i);
                            int chamaId = chamaObject.getInt("chamaId");
                            String chamaName = chamaObject.getString("chamaName");
                            String chamaDescription = chamaObject.getString("chamaDescription");

                            // Create a Chamas object and add it to the list
                            Chamas chamas = new Chamas(chamaId, chamaName, chamaDescription);
                            chamasList.add(chamas);
                        }

                        // Pass the chamas list to the adapter and set it to the RecyclerView
                        AdapterSearch adapter = new AdapterSearch(chamasList, context);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Display an error message to the user
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        // Check if the error message indicates invalid credentials
                        if (jsonObject.getString("message").equals("Invalid Username or Password")) {
                            // Perform logout operation and redirect to the login screen
                            SharedPrefManager.getInstance(context).logout();
                            startActivity(new Intent(context, MainActivity.class));
                            getActivity().finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext(); // Get the fragment's context
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Context context = getContext(); // Get the fragment's context
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void filterChamasList(String query) {
        filteredChamasList.clear();

        for (Chamas chamas : chamasList) {
            String chamaName = String.valueOf(chamas.getChamaName());
            if (chamaName != null && chamaName.toLowerCase().contains(query.toLowerCase())) {
                filteredChamasList.add(chamas);
            }
        }

        adapterSearch.setChamasList(filteredChamasList);
        adapterSearch.notifyDataSetChanged();
    }



}