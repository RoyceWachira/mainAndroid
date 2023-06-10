package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Notifications {
    @SerializedName("notification_id") private String notificationId;
    @SerializedName("chama_id") private String chamaId;
    @SerializedName("user_id") private String userId;
    @SerializedName("notification_content") private String notificationContent;
    @SerializedName("notification_title") private String notificationTitle;
    @SerializedName("created_at") private String createdAt;



    public Notifications(String notificationId, String notificationTitle, String notificationContent,String createdAt, String chamaId, String userId) {
        this.chamaId=chamaId;
        this.userId= userId;
        this.notificationId=notificationId;
        this.notificationContent=notificationContent;
        this.notificationTitle=notificationTitle;
        this.createdAt=createdAt;
    }

    public String getChamaId(){return chamaId;}

    public String getUserId() {return userId; }

    public String getNotificationId() {
        return notificationId;
    }

    public String getNotificationContent(){return notificationContent;}

    public String getNotificationTitle(){return notificationTitle;}

    public String getCreatedAt(){return createdAt;}


}

