package com.example.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChamaNotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chama_notifications, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationsList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationsList);
        recyclerView.setAdapter(notificationAdapter);

    }

    private void addNotification(Notification notification) {
        notificationsList.add(notification);
        notificationAdapter.notifyDataSetChanged();
    }

    // Notification class to hold notification data
    private static class Notification {
        private String title;
        private String content;

        public Notification( String title, String content) {
            this.title = title;
            this.content = content;
        }


        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    // Adapter for the notification list
    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        private List<Notification> notifications;

        public NotificationAdapter(List<Notification> notifications) {
            this.notifications = notifications;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Notification notification = notifications.get(position);
            holder.textTitle.setText(notification.getTitle());
            holder.textContent.setText(notification.getContent());
        }

        @Override
        public int getItemCount() {
            int itemCount = notifications.size();
            Log.d("Adapter", "notifications List Size: " + itemCount);
            return itemCount;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textTitle;
            public TextView textContent;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textTitle = itemView.findViewById(R.id.txtNoticeTitle);
                textContent = itemView.findViewById(R.id.txtNoticeContent);
            }
        }
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent broadcastIntent) {
            // Extract the notification data from the broadcast intent
            String title = broadcastIntent.getStringExtra("title");
            String content = broadcastIntent.getStringExtra("content");

            Log.d("RTitle",title);
            Log.d("RContent",content);
            Notification notification = new Notification(title, content);

            Log.d("n", String.valueOf(notification));
            // Add the new notification to the list
            addNotification(notification);

        }
    };
    @Override
    public void onResume() {
        super.onResume();
        // Register the broadcast receiver to receive notification data
        IntentFilter filter = new IntentFilter("com.example.myapp.NOTIFY");
        requireContext().registerReceiver(notificationReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the broadcast receiver
        requireContext().unregisterReceiver(notificationReceiver);
    }
}
