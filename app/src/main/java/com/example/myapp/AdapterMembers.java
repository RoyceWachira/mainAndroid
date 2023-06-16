package com.example.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AdapterMembers extends RecyclerView.Adapter<AdapterMembers.MyViewHolder>{
    private List<Members> membersList;
    private Context context;

    public AdapterMembers(List<Members>membersList,Context context){
        this.membersList=membersList;
        this.context=context;
    }

    public void setMembersList(List<Members> membersList) {
        this.membersList = membersList;

    }

    @Override
    public AdapterMembers.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.members,parent,false);
        return new AdapterMembers.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterMembers.MyViewHolder holder, int position) {
        Members member = membersList.get(position);

        holder.deleteButton.setVisibility(member.getDeleteButtonVisibility());
        holder.memberUserName.setText(member.getUserName());
        holder.fName.setText(member.getFirstName());
        holder.bind(member);
        isChairOrVice(holder.getAdapterPosition());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDetails memberDetails = new MemberDetails();
                Bundle bundle = new Bundle();
                bundle.putString("username", member.getUserName());
                bundle.putString("firstName", member.getFirstName());
                bundle.putString("chamaRole", member.getChamaRole());
                bundle.putString("dateJoined", member.getDateJoined());
                bundle.putString("email", member.getEmail());
                bundle.putString("phoneNumber", member.getPhoneNumber());
                bundle.putString("chamaId", member.getChamaId());
                bundle.putString("memberId",member.getMemberId());
                memberDetails.setArguments(bundle);

                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, memberDetails)
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove Member");
                builder.setMessage("Are you sure you want to remove this Member?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = membersList.get(holder.getAdapterPosition()).getUserId();
                        String chamaId = membersList.get(holder.getAdapterPosition()).getChamaId();
                        String url = Constants.URL_REMOVE_FROM_CHAMA + "?user_id=" + userId + "&chama_id=" + chamaId;

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean error = jsonObject.getBoolean("error");
                                    String message = jsonObject.getString("message");

                                    if (error) {
                                        showToast(message, true);
                                    } else {
                                        showToast(message, false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showToast("Error Occurred: " + e.getMessage(), true);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String errorMessage = error != null && error.getMessage() != null ? error.getMessage() : "Unknown error";
                                Log.e("Error", "Error occurred", error);
                                showToast("Error occurred: " + errorMessage, true);
                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });

                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = membersList.size();
        Log.d("Adapter", "Members List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardMember;
        TextView memberUserName,fName;
        Button viewButton,deleteButton;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton= itemView.findViewById(R.id.deleteButton);
            viewButton= itemView.findViewById(R.id.viewButton);
            cardMember=itemView.findViewById(R.id.cardMember);
            memberUserName= itemView.findViewById(R.id.memberUsername);
            fName= itemView.findViewById(R.id.fName);
            imageView=itemView.findViewById(R.id.leader);
        }
        public void bind(Members member) {
            if (member.getChamaRole().equals("Chairperson") || member.getChamaRole().equals("Vice ChairPerson") || member.getChamaRole().equals("Treasurer") || member.getChamaRole().equals("Secretary") ) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    private void showToast(String message, boolean isError) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout;

        if (isError) {
            layout = inflater.inflate(R.layout.custom_toast_error, null);
        } else {
            layout = inflater.inflate(R.layout.custom_toast_success, null);
        }

        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void isChairOrVice(int position) {
        String userId = SharedPrefManager.getInstance(context).getUserId();
        String chamaId = membersList.get(position).getChamaId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_IS_CHAIR_OR_VICE + "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error == false) {
                        membersList.get(position).setDeleteButtonVisibility(View.VISIBLE);
                    } else {
                        membersList.get(position).setDeleteButtonVisibility(View.GONE);
                    }
                    notifyItemChanged(position);
                } catch (JSONException e) {
                    showToast("Error occurred: " + e.getMessage(), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.getMessage(), true);
            }
        });

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }





}


