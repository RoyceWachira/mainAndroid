package com.example.myapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterMeetings extends RecyclerView.Adapter<AdapterMeetings.MyViewHolder>{
    private List<Meetings> meetingsList;
    private Context context;

    public AdapterMeetings(List<Meetings>meetingsList,Context context){
        this.meetingsList=meetingsList;
        this.context=context;
    }

    public void setMeetingsList(List<Meetings> meetingsList) {
        this.meetingsList = meetingsList;

    }

    @Override
    public AdapterMeetings.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.meetings,parent,false);
        return new AdapterMeetings.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterMeetings.MyViewHolder holder, int position) {
        Meetings meetings = meetingsList.get(position);

        holder.delete.setVisibility(meetings.getDeleteButtonVisibility());
        holder.edit.setVisibility(meetings.getEditButtonVisibility());
        isChairOrVice(holder.getAdapterPosition());
        holder.by.setText(meetings.getCreatedBy());
        holder.date.setText(meetings.getMeetingDate());
        holder.time.setText(meetings.getMeetingTime());
        holder.venue.setText(meetings.getMeetingVenue());
        holder.purpose.setText(meetings.getMeetingPurpose());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeeting(holder.getAdapterPosition());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditMeeting editMeeting = new EditMeeting();
                Bundle bundle = new Bundle();
                bundle.putString("meetingId", meetings.getMeetingId());
                editMeeting.setArguments(bundle);
                // Replace the current fragment with the new fragment
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.chamaFrameLayout, editMeeting)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        int itemCount = meetingsList.size();
        Log.d("Adapter", "Meetings List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardMeetings;
        TextView purpose,venue,date,time,by;
        Button edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            edit= itemView.findViewById(R.id.editButton);
            delete= itemView.findViewById(R.id.deleteButton);
            purpose= itemView.findViewById(R.id.txtMeetPurpose);
            venue= itemView.findViewById(R.id.txtMeetVenue);
            date= itemView.findViewById(R.id.txtMeetDate);
            time=itemView.findViewById(R.id.txtMeetTime);
            by= itemView.findViewById(R.id.txtBy);
            cardMeetings= itemView.findViewById(R.id.cardMeetings);
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
        String chamaId = meetingsList.get(position).getChamaId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_IS_CHAIR_OR_VICE + "?chama_id=" + chamaId + "&user_id=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (error == false) {
                        meetingsList.get(position).setDeleteButtonVisibility(View.VISIBLE);
                        meetingsList.get(position).seteditButtonVisibility(View.VISIBLE);
                    } else {
                        meetingsList.get(position).setDeleteButtonVisibility(View.GONE);
                        meetingsList.get(position).seteditButtonVisibility(View.GONE);
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

    private void deleteMeeting(int position) {
        String meetingId = meetingsList.get(position).getMeetingId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DELETE_MEETING+ "?meeting_id=" + meetingId , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String message = jsonObject.getString("message");

                    if (!error) {
                        showToast(message, false);
                    } else {
                        showToast(message, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Error Occurred", true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(error.getMessage(), true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("meeting_id", meetingId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}



