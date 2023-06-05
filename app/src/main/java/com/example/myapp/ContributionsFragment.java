package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ContributionsFragment extends Fragment {

    String chamaId;
    Integer userId;
    private CardView oneCont,allCont;
    private Button btnNewContribution;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contributions, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
        }
        userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());

        oneCont= view.findViewById(R.id.cardOneCont);
        allCont= view.findViewById(R.id.cardAllCont);
        btnNewContribution= view.findViewById(R.id.btnNewCont);

        oneCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndividualContributions individualContributions= new IndividualContributions();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                individualContributions.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, individualContributions);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        isLeader();

        allCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllContributions allContributions= new AllContributions();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                allContributions.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, allContributions);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnNewContribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewContribution newContribution= new NewContribution();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                newContribution.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, newContribution);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        return view;
    }

    private void isLeader() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_IS_LEADER+ "?chama_id=" + chamaId+ "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if(error==false){
                        allCont= getView().findViewById(R.id.cardAllCont);
                        allCont.setVisibility(View.VISIBLE);
                    }else{
                        allCont= getView().findViewById(R.id.cardAllCont);
                        allCont.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Context context = getContext();
                showToast(error.getMessage(),true);
            }
        });

        Context context = getContext();
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = getLayoutInflater();
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, getView().findViewById(R.id.toast_message));
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, getView().findViewById(R.id.toast_message));
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}