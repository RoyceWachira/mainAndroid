package com.example.myapp;

import com.google.gson.annotations.SerializedName;

public class Meetings{

    @SerializedName("meeting_purpose") private String meetingPurpose;
    @SerializedName("meeting_id") private String meetingId;
    @SerializedName("meeting_venue") private String meetingVenue;
    @SerializedName("meeting_time") private String meetingTime;
    @SerializedName("meeting_date") private String meetingDate;
    @SerializedName("created_by") private String createdBy;

    public Meetings(String meetingDate, String meetingPurpose, String meetingId,String meetingTime,String meetingVenue,String createdBy) {

        this.meetingDate=meetingDate;
        this.meetingVenue=meetingVenue;
        this.meetingPurpose=meetingPurpose;
        this.meetingTime=meetingTime;
        this.createdBy=createdBy;
        this.meetingId=meetingId;
    }


    public String getMeetingPurpose() {
        return meetingPurpose;
    }

    public String getMeetingId(){return meetingId;}

    public String getMeetingVenue() {
        return meetingVenue;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }


    public String setMeetingId() {
        return meetingId;
    }


}
