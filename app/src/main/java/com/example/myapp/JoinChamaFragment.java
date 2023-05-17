package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_chama, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        chamasList = new ArrayList<>();
        filteredChamasList = new ArrayList<>();

        adapterSearch = new AdapterSearch(chamasList, getContext());
        recyclerView.setAdapter(adapterSearch);

        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println(true);
            fetchChamasNotJoined();

        }

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("List",chamasList.toString());
                Log.d("Search",newText);
                filterChamasList(newText);
                return true;
            }
        });

        return view;
    }

    private void fetchChamasNotJoined() {

        // Retrieve the user's ID or any identifier needed to fetch joined chamas

        String userId = null;
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {

            userId = SharedPrefManager.getInstance(getContext()).getUserId();

        } else {

        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_CHAMAS_NOT_JOINED + "?user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Check if the backend response indicates a successful request
                    if (jsonObject.getBoolean("error")==false) {
                        JSONArray chamasArray = jsonObject.getJSONArray("chamas");

                        chamasList.clear();

                        // Check if the user has joined any chamas
                        if (chamasArray.length() > 0) {
                            TextView noChamasTextView = getView().findViewById(R.id.unjoinedChamas);
                            noChamasTextView.setVisibility(View.GONE);

                            for (int i = 0; i < chamasArray.length(); i++) {
                                JSONObject chamaObject = chamasArray.getJSONObject(i);

                                String chamaName = chamaObject.getString("chama_name");
                                String chamaDescription = chamaObject.getString("chama_description");
                                Integer chamaId= chamaObject.getInt("chama_id");

                                Chamas chamas = new Chamas(chamaName, chamaDescription,chamaId);
                                chamasList.add(chamas);
                            }

                            adapterSearch.setChamasList(chamasList);
                            adapterSearch.notifyDataSetChanged();
                        } else {

                            TextView noChamasTextView = getView().findViewById(R.id.unjoinedChamas);
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

    private void filterChamasList(String query) {
        filteredChamasList.clear();

        if (query.isEmpty()) {
            filteredChamasList.addAll(chamasList);
            TextView noChamasTextView = getView().findViewById(R.id.unjoinedChamas);
            noChamasTextView.setVisibility(View.GONE);

        } else {
            // Filter chamas based on the search query
            for (Chamas chamas : chamasList) {
                String chamaName = chamas.getChamaName();
                if (chamaName != null && chamaName.toLowerCase().contains(query.toLowerCase())) {
                    filteredChamasList.add(chamas);
                }
            }
        }

        adapterSearch.setChamasList(filteredChamasList);
        adapterSearch.notifyDataSetChanged();

        if (filteredChamasList.isEmpty()) {
            TextView noChamasTextView = getView().findViewById(R.id.unjoinedChamas);
            noChamasTextView.setVisibility(View.VISIBLE);
        } else {
            TextView noChamasTextView = getView().findViewById(R.id.unjoinedChamas);
            noChamasTextView.setVisibility(View.GONE);
        }
    }




            }