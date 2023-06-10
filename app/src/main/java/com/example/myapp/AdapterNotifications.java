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

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyViewHolder>{
    private List<Notifications> notificationsList;
    private Context context;

    public AdapterNotifications(List<Notifications>notificationsList,Context context){
        this.notificationsList=notificationsList;
        this.context=context;
    }

    public void setNotificationsList(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;

    }

    @Override
    public AdapterNotifications.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications,parent,false);
        return new AdapterNotifications.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterNotifications.MyViewHolder holder, int position) {
        Notifications notifications = notificationsList.get(position);

        holder.textContent.setText(notifications.getNotificationContent());
        holder.textTitle.setText(notifications.getNotificationTitle());

    }

    @Override
    public int getItemCount() {
        int itemCount = notificationsList.size();
        Log.d("Adapter", "Notifications List Size: " + itemCount);
        return itemCount;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textContent;
        public CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card= itemView.findViewById(R.id.cardNote);
            textTitle = itemView.findViewById(R.id.txtNoticeTitle);
            textContent = itemView.findViewById(R.id.txtNoticeContent);
        }

    }
}


