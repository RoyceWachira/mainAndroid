package com.example.myapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        holder.by.setText(meetings.getCreatedBy());
        holder.date.setText(meetings.getMeetingDate());
        holder.time.setText(meetings.getMeetingTime());
        holder.venue.setText(meetings.getMeetingVenue());
        holder.purpose.setText(meetings.getMeetingPurpose());

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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            purpose= itemView.findViewById(R.id.txtMeetPurpose);
            venue= itemView.findViewById(R.id.txtMeetVenue);
            date= itemView.findViewById(R.id.txtMeetDate);
            time=itemView.findViewById(R.id.txtMeetTime);
            by= itemView.findViewById(R.id.txtBy);
            cardMeetings= itemView.findViewById(R.id.cardMeetings);
        }

    }
}



