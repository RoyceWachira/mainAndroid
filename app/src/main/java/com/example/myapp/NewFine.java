package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


public class NewFine extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Members> membersList;
    private AdapterSelectMembers adapterSelectMembers;
    private Button btnProceed;
    String chamaId,chamaName,chamaFlow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_fine, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
        }

        btnProceed= view.findViewById(R.id.btnProceed);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        membersList = new ArrayList<>();

        adapterSelectMembers = new AdapterSelectMembers(membersList, getContext());
        recyclerView.setAdapter(adapterSelectMembers);
        List<String> selectedMemberIds = adapterSelectMembers.getSelectedMemberIds();
        selectMembers();

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargeFine chargeFine= new ChargeFine();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                args.putString("chamaName",chamaName);
                args.putString("chamaFlow",chamaFlow);
                args.putStringArrayList("selectedMemberIds", (ArrayList<String>) selectedMemberIds);
                chargeFine.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, chargeFine);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void selectMembers() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_MEMBERS + "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("error") == false) {
                        JSONArray membersArray = jsonObject.getJSONArray("members");

                        membersList.clear();

                        if (membersArray.length() > 0) {
                            for (int i = 0; i < membersArray.length(); i++) {
                                JSONObject membersObject = membersArray.getJSONObject(i);
                                String loggedIn = SharedPrefManager.getInstance(getContext()).getUserId();
                                String firstName = membersObject.getString("first_name");
                                String lastName = membersObject.getString("last_name");
                                String userId = String.valueOf(membersObject.getInt("user_id"));
                                String userName = membersObject.getString("username");
                                String chamaRole = membersObject.getString("chama_role");
                                String email = membersObject.getString("email");
                                String dateJoined = membersObject.getString("date_joined");
                                String phoneNumber = membersObject.getString("phone_number");
                                String chamaId= membersObject.getString("chama_id");
                                String memberId= membersObject.getString("member_id");

                                Members members = new Members(chamaRole, userId, firstName, lastName, email, dateJoined, phoneNumber, userName,chamaId,memberId);
                                if(loggedIn!=userId) {
                                    membersList.add(members);
                                }
                            }

                            adapterSelectMembers.setMembersList(membersList);
                            adapterSelectMembers.notifyDataSetChanged();
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


