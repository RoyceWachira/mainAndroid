package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ChamaHomeFragment extends Fragment {

    private FloatingActionButton fab,fab2,fab1,fab3,fab4,fab5;
    private Animation fabOpen;
    private Animation fabClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean isOpen = false;
    private TextView chamaHeader,txtName,txtChama;
    private String chamaId;
    private CardView card2,card3,card4,card5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chama_home, container, false);

        card2= view.findViewById(R.id.card2);
        card3= view.findViewById(R.id.card3);

        Bundle arguments = getArguments();
        if(arguments != null) {
            chamaId = arguments.getString("chamaId");
            String chamaName = arguments.getString("chamaName");

            chamaHeader = view.findViewById(R.id.txtChamaTitle);
            chamaHeader.setText(chamaName);
            txtChama = view.findViewById(R.id.txtChama);
            txtChama.setText(chamaName);
        }

        String user= SharedPrefManager.getInstance(getContext()).getKeyUserName();
        txtName = view.findViewById(R.id.txtName);
        txtName.setText(user);

        // Initialize views
        fab = view.findViewById(R.id.chamaFloatingButton);
        fab1 = view.findViewById(R.id.floatingContribute);
        fab2 = view.findViewById(R.id.floatingWithdraw);
        fab3= view.findViewById(R.id.floatingFine);
        fab4= view.findViewById(R.id.floatingLoan);
        fab5= view.findViewById(R.id.floatingMeet);

        // Initialize animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);


        fab.setOnClickListener(v -> onFabClicked());

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMembers viewMembers= new ViewMembers();
                Bundle args = new Bundle();
                args.putString("chamaId", chamaId);
                viewMembers.setArguments(args);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chamaFrameLayout, viewMembers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        fetchTotalFunds();

        fetchTotalMembers();



        fetchTotalIndividualContributions();
        fetchTotalIndividualFines();
        fetchTotalIndividualLoans();

        return view;
    }

    private void onFabClicked() {
        if (isOpen) {
            fab.startAnimation(fabClose);
            fab1.startAnimation(toBottom);
            fab2.startAnimation(toBottom);
            fab3.startAnimation(toBottom);
            fab4.startAnimation(toBottom);
            fab5.startAnimation(toBottom);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);

            TextView labelContribute = getView().findViewById(R.id.labelContribute);
            TextView labelWithdraw = getView().findViewById(R.id.labelWithdraw);
            TextView labelLoan = getView().findViewById(R.id.labelLoan);
            TextView labelFine = getView().findViewById(R.id.labelFine);
            TextView labelMeet = getView().findViewById(R.id.labelMeet);

            labelContribute.setVisibility(View.INVISIBLE);
            labelWithdraw.setVisibility(View.INVISIBLE);
            labelFine.setVisibility(View.INVISIBLE);
            labelLoan.setVisibility(View.INVISIBLE);
            labelMeet.setVisibility(View.INVISIBLE);

        } else {
            fab.startAnimation(fabOpen);
            fab1.startAnimation(fromBottom);
            fab2.startAnimation(fromBottom);
            fab3.startAnimation(fromBottom);
            fab4.startAnimation(fromBottom);
            fab5.startAnimation(fromBottom);

            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);

            TextView labelContribute = getView().findViewById(R.id.labelContribute);
            TextView labelWithdraw = getView().findViewById(R.id.labelWithdraw);
            TextView labelLoan = getView().findViewById(R.id.labelLoan);
            TextView labelFine = getView().findViewById(R.id.labelFine);
            TextView labelMeet = getView().findViewById(R.id.labelMeet);

            labelContribute.setVisibility(View.VISIBLE);
            labelWithdraw.setVisibility(View.VISIBLE);
            labelFine.setVisibility(View.VISIBLE);
            labelLoan.setVisibility(View.VISIBLE);
            labelMeet.setVisibility(View.VISIBLE);
        }
        isOpen = !isOpen;
    }

    private void fetchTotalFunds() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_CHAMA_FUNDS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalFunds = jsonObject.getInt("totalFunds");

                    TextView fundsCountTextView = getView().findViewById(R.id.txtFunds);
                    fundsCountTextView.setText(String.valueOf(totalFunds));
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

    private void fetchTotalMembers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_CHAMA_MEMBERS+ "?chama_id=" + chamaId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalMembers = jsonObject.getInt("totalMembers");

                    TextView membersCountTextView = getView().findViewById(R.id.membersCountTextView);
                    membersCountTextView.setText(String.valueOf(totalMembers));
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

    private void fetchTotalIndividualContributions() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_INDIVIDUAL_CONTRIBUTIONS+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalMemberContributions = jsonObject.getInt("totalMemberContributions");

                    TextView contributionsCountTextView = getView().findViewById(R.id.txtCountContributions);
                    contributionsCountTextView.setText(String.valueOf(totalMemberContributions));
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

    private void fetchTotalIndividualFines() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_INDIVIDUAL_FINES+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalMemberFines = jsonObject.getInt("totalMemberFines");

                    TextView finesCountTextView = getView().findViewById(R.id.txtFinesCount);
                    finesCountTextView.setText(String.valueOf(totalMemberFines));
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

    private void fetchTotalIndividualLoans() {
        int userId = Integer.parseInt(SharedPrefManager.getInstance(getContext()).getUserId());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_TOTAL_INDIVIDUAL_LOANS+ "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int totalMemberLoans = jsonObject.getInt("totalMemberLoans");

                    TextView loansCountTextView = getView().findViewById(R.id.txtLoansCount);
                    loansCountTextView.setText(String.valueOf(totalMemberLoans));
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