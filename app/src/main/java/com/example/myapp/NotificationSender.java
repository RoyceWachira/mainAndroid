package com.example.myapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationSender  {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CHANNEL_ID = "chama_notifications";
    private static final int NOTIFICATION_ID = 1;
    private OkHttpClient client = new OkHttpClient();

    public void sendNotification(Context context,final String userId, final String notificationContent, final String url) {
        // Create the JSON request body
        String jsonBody = "{\"userId\":\"" + userId + "\",\"notificationContent\":\"" + notificationContent + "\"}";
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        // Create the POST request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Create a new thread to send the request
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Send the request and handle the response
                try {
                    Response response = client.newCall(request).execute();
                    showNotification(context,"New Chama Notifications",notificationContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private void showNotification(Context context, String title, String content) {
        // Create the notification channel (only required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Chama Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for Chama");
            channel.enableLights(true);
            channel.setLightColor(Color.BLACK);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

}
