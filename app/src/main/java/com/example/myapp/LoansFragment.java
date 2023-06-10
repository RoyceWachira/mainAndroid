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

public class LoansFragment extends Fragment {

    String chamaId,chamaFlow,chamaName;
    Integer userId;
    private CardView myLoans,allLoans,loanRequests;
    private Button btnReqLoan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loans, container, false);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            chamaFlow= arguments.getString("chamaFlow");
            chamaName= arguments.getString("chamaName");
        }
        userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());

        myLoans= view.findViewById(R.id.cardmyLoans);
        allLoans= view.findViewById(R.id.cardAllLoans);
        loanRequests= view.findViewById(R.id.cardloanRequests);
        btnReqLoan= view.findViewById(R.id.btnReqLoan);

        myLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndividualLoans individualLoans= new IndividualLoans();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                individualLoans.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, individualLoans);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        isLeader();

        allLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllLoans allLoans= new AllLoans();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                allLoans.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, allLoans);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        loanRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoanRequests loanRequests= new LoanRequests();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                loanRequests.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, loanRequests);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnReqLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewLoan newLoan= new NewLoan();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                args.putString("chamaName",chamaName);
                args.putString("chamaFlow",chamaFlow);
                newLoan.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, newLoan);
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
                        allLoans= getView().findViewById(R.id.cardAllLoans);
                        allLoans.setVisibility(View.VISIBLE);
                        btnReqLoan=getView().findViewById(R.id.btnReqLoan);
                        btnReqLoan.setVisibility(View.VISIBLE);
                        loanRequests= getView().findViewById(R.id.cardloanRequests);
                        loanRequests.setVisibility(View.VISIBLE);
                    }else{
                        allLoans= getView().findViewById(R.id.cardAllLoans);
                        allLoans.setVisibility(View.INVISIBLE);
                        btnReqLoan=getView().findViewById(R.id.btnReqLoan);
                        btnReqLoan.setVisibility(View.INVISIBLE);
                        loanRequests= getView().findViewById(R.id.cardloanRequests);
                        loanRequests.setVisibility(View.INVISIBLE);
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